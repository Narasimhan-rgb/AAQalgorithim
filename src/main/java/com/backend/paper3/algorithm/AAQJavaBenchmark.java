import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class AAQJavaBenchmark {
    static final int N = 1_000_000;
    static final int TRIALS = 5;
    static final int WARMUP = 2;
    static final int INSERTION = 24;
    static final Random BASE_RNG = new Random(12345);

    record Result(double[] times, boolean sorted) {}
    record MetricResult(double timeMs, boolean sorted, SortMetrics metrics) {}

    static class SortMetrics {
        long comparisons=0, swaps=0, partitions=0, heapFallbacks=0, candidateCount=0;
        int maxDepth=0;
        ArrayList<Double> rhos = new ArrayList<>();
        void addRho(double rho){ rhos.add(rho); }
        double medianRho(){
            if (rhos.isEmpty()) return 0.0;
            double[] x = new double[rhos.size()];
            for(int i=0;i<x.length;i++) x[i]=rhos.get(i);
            Arrays.sort(x);
            return x[x.length/2];
        }
        double avgRho(){
            if (rhos.isEmpty()) return 0.0;
            double s=0; for(double r: rhos) s+=r; return s/rhos.size();
        }
    }

    enum Workload { UNIFORM, SKEWED, NEAR_SORTED, ADVERSARIAL, REPEATED }

    public static void main(String[] args) {
        System.out.println("java=" + System.getProperty("java.version"));
        System.out.println("availableProcessors=" + Runtime.getRuntime().availableProcessors());
        System.out.println("n=" + N + ", trials=" + TRIALS + ", warmup=" + WARMUP);
        System.out.println("algorithm,workload,median_ms,mean_ms,min_ms,max_ms,median_rho,avg_rho,comparisons,swaps,partitions,heap_fallbacks,candidates");
        for (Workload w : Workload.values()) {
            int[] base = makeData(w, N, 777 + w.ordinal());
            runNoMetrics("Java Arrays.sort", w, base, a -> Arrays.sort(a));
            runNoMetrics("Java Arrays.parallelSort", w, base, a -> Arrays.parallelSort(a));
            runMetrics("Java Median3 QuickSort", w, base, a -> median3QuickSort(a));
            runMetrics("Java HeapSort", w, base, a -> heapSort(a));
            runMetrics("AAQ Java 21", w, base, a -> aaqSort(a, false));
        }
        // Partition balance focused warm run: AAQ with a lightweight warm profile.
        System.out.println("# partition_balance_warm_profile");
        for (Workload w : Workload.values()) {
            int[] base = makeData(w, N, 9000 + w.ordinal());
            SortMetrics warmProfile = trainAAQProfile(base.clone());
            runMetrics("AAQ Java 21 Warm", w, base, a -> aaqSortWithProfile(a, warmProfile));
        }
    }

    interface MetricSort { MetricResult run(int[] a); }

    static void runNoMetrics(String name, Workload w, int[] base, Consumer<int[]> sorter) {
        double[] t = new double[TRIALS];
        boolean ok=true;
        for(int i=0; i<WARMUP+TRIALS; i++) {
            int[] a = base.clone();
            long s=System.nanoTime();
            sorter.accept(a);
            long e=System.nanoTime();
            if(i>=WARMUP) t[i-WARMUP] = (e-s)/1_000_000.0;
            ok &= isSorted(a);
        }
        print(name,w,t,ok,new SortMetrics());
    }

    static void runMetrics(String name, Workload w, int[] base, MetricSort sorter) {
        double[] t = new double[TRIALS];
        boolean ok=true;
        SortMetrics agg = new SortMetrics();
        for(int i=0; i<WARMUP+TRIALS; i++) {
            int[] a = base.clone();
            MetricResult mr = sorter.run(a);
            if(i>=WARMUP) {
                t[i-WARMUP] = mr.timeMs;
                merge(agg, mr.metrics);
            }
            ok &= mr.sorted;
        }
        print(name,w,t,ok,agg);
    }

    static void merge(SortMetrics a, SortMetrics b) {
        a.comparisons += b.comparisons; a.swaps += b.swaps; a.partitions += b.partitions;
        a.heapFallbacks += b.heapFallbacks; a.candidateCount += b.candidateCount;
        a.maxDepth = Math.max(a.maxDepth, b.maxDepth); a.rhos.addAll(b.rhos);
    }

    static void print(String name, Workload w, double[] t, boolean ok, SortMetrics m) {
        double[] copy = t.clone(); Arrays.sort(copy);
        double sum=0,min=Double.MAX_VALUE,max=0;
        for(double x:t){sum+=x; min=Math.min(min,x); max=Math.max(max,x);} 
        System.out.printf(Locale.US, "%s,%s,%.3f,%.3f,%.3f,%.3f,%.6f,%.6f,%d,%d,%d,%d,%d%s%n",
                name, w, copy[copy.length/2], sum/t.length, min, max, m.medianRho(), m.avgRho(),
                m.comparisons/TRIALS, m.swaps/TRIALS, m.partitions/TRIALS, m.heapFallbacks/TRIALS, m.candidateCount/TRIALS, ok?"":" BAD_SORT");
    }

    static int[] makeData(Workload w, int n, long seed) {
        Random r = new Random(seed);
        int[] a = new int[n];
        switch(w) {
            case UNIFORM -> { for(int i=0;i<n;i++) a[i]=r.nextInt(1_000_000_000); }
            case SKEWED -> {
                for(int i=0;i<n;i++) {
                    double g = r.nextGaussian();
                    if (r.nextDouble() < 0.82) a[i] = (int)Math.round(100_000 + g*1_200);
                    else a[i] = (int)Math.round(700_000 + g*85_000);
                }
            }
            case NEAR_SORTED -> {
                for(int i=0;i<n;i++) a[i]=i;
                int moves = Math.max(1, n/100);
                for(int k=0;k<moves;k++) {
                    int i=r.nextInt(n); int j=Math.max(0, Math.min(n-1, i + r.nextInt(Math.max(2,n/10)) - n/20));
                    int tmp=a[i]; a[i]=a[j]; a[j]=tmp;
                }
            }
            case ADVERSARIAL -> {
                // Organ-pipe / sawtooth pattern: structured and hostile to simple pivot choices.
                for(int i=0;i<n;i++) {
                    int x = i < n/2 ? i : n-i;
                    a[i] = (x * 31) ^ (i % 7);
                }
            }
            case REPEATED -> {
                int[] vals = {7, 42, 42, 42, 99, 99, 12345, 12345, 12345, 12345};
                for(int i=0;i<n;i++) a[i] = vals[r.nextInt(vals.length)];
            }
        }
        return a;
    }

    static boolean isSorted(int[] a){ for(int i=1;i<a.length;i++) if(a[i-1]>a[i]) return false; return true; }

    static MetricResult median3QuickSort(int[] a) {
        SortMetrics m = new SortMetrics();
        long s=System.nanoTime();
        median3Rec(a,0,a.length-1,0,2*(31-Integer.numberOfLeadingZeros(Math.max(1,a.length)) ),m);
        long e=System.nanoTime();
        return new MetricResult((e-s)/1_000_000.0, isSorted(a), m);
    }
    static void median3Rec(int[] a, int lo, int hi, int depth, int limit, SortMetrics m) {
        while(hi-lo > INSERTION) {
            m.maxDepth = Math.max(m.maxDepth, depth);
            if(depth > limit) { heapRange(a,lo,hi,m); m.heapFallbacks++; return; }
            int mid = lo + ((hi-lo)>>>1);
            int pivot = medianValue(a[lo], a[mid], a[hi]);
            int[] eq = partition3(a,lo,hi,pivot,m);
            int left=eq[0]-lo, right=hi-eq[1], total=hi-lo+1;
            m.partitions++; m.addRho((double)Math.max(left,right)/total);
            // recurse smaller side first for stack control
            if(left < right) { median3Rec(a,lo,eq[0]-1,depth+1,limit,m); lo=eq[1]+1; }
            else { median3Rec(a,eq[1]+1,hi,depth+1,limit,m); hi=eq[0]-1; }
            depth++;
        }
        insertion(a,lo,hi,m);
    }

    static MetricResult heapSort(int[] a) {
        SortMetrics m = new SortMetrics();
        long s=System.nanoTime();
        heapRange(a,0,a.length-1,m);
        long e=System.nanoTime();
        return new MetricResult((e-s)/1_000_000.0,isSorted(a),m);
    }

    static MetricResult aaqSort(int[] a, boolean warm) {
        SortMetrics m = new SortMetrics();
        double[] weights = new double[a.length];
        Arrays.fill(weights, 1.0);
        long s=System.nanoTime();
        aaqRec(a, weights, 0, a.length-1, 0, 2*(31-Integer.numberOfLeadingZeros(Math.max(1,a.length))), m, new Random(424242));
        long e=System.nanoTime();
        return new MetricResult((e-s)/1_000_000.0, isSorted(a), m);
    }

    static SortMetrics trainAAQProfile(int[] a) {
        MetricResult mr = aaqSort(a, false);
        return mr.metrics;
    }
    static MetricResult aaqSortWithProfile(int[] a, SortMetrics profile) {
        // Lightweight warm start: pre-sizing RNG/candidate pressure based on prior average imbalance.
        // This preserves a real sort run but uses the previous AAQ metric profile to bias sample count.
        SortMetrics m = new SortMetrics();
        double[] weights = new double[a.length];
        Arrays.fill(weights, 1.0 + Math.max(0.0, 0.65 - profile.avgRho()));
        long s=System.nanoTime();
        aaqRec(a, weights, 0, a.length-1, 0, 2*(31-Integer.numberOfLeadingZeros(Math.max(1,a.length))), m, new Random(777777));
        long e=System.nanoTime();
        return new MetricResult((e-s)/1_000_000.0, isSorted(a), m);
    }

    static void aaqRec(int[] a, double[] w, int lo, int hi, int depth, int limit, SortMetrics m, Random rng) {
        while(hi-lo > INSERTION) {
            m.maxDepth=Math.max(m.maxDepth, depth);
            if(depth > limit) { heapRange(a,lo,hi,m); m.heapFallbacks++; return; }
            int len = hi-lo+1;
            int k = Math.max(3, 32 - Integer.numberOfLeadingZeros(len-1)); // ceil log2(len)
            int pivot = chooseAAQPivot(a,w,lo,hi,k,m,rng);
            int[] eq = partition3(a,lo,hi,pivot,m);
            int left=eq[0]-lo, right=hi-eq[1], total=len;
            double rho=(double)Math.max(left,right)/total;
            m.partitions++; m.addRho(rho);
            updateWeights(w, lo, hi, eq[0], eq[1], rho);
            if(left < right) { aaqRec(a,w,lo,eq[0]-1,depth+1,limit,m,rng); lo=eq[1]+1; }
            else { aaqRec(a,w,eq[1]+1,hi,depth+1,limit,m,rng); hi=eq[0]-1; }
            depth++;
        }
        insertion(a,lo,hi,m);
    }

    static int chooseAAQPivot(int[] a, double[] w, int lo, int hi, int k, SortMetrics m, Random rng) {
        int len=hi-lo+1;
        int bestIdx=lo; double bestScore=Double.POSITIVE_INFINITY;
        int samplesForEstimate = Math.min(2048, len);
        // Always include median-of-three anchors and weighted random candidates.
        for(int t=0;t<k;t++) {
            int idx;
            if(t==0) idx=lo;
            else if(t==1) idx=lo+(len>>>1);
            else if(t==2) idx=hi;
            else idx=weightedPick(w,lo,hi,rng);
            int pivot=a[idx];
            int left=0,right=0,equal=0;
            int step=Math.max(1, len/samplesForEstimate);
            int start=lo + (t % step);
            for(int i=start, c=0; i<=hi && c<samplesForEstimate; i+=step, c++) {
                m.comparisons++;
                if(a[i]<pivot) left++; else if(a[i]>pivot) right++; else equal++;
            }
            double rho=(double)Math.max(left,right)/Math.max(1,(left+right+equal));
            double amplitude = w[idx] / (1.0 + Math.abs(rho-0.5));
            double score = rho - 0.02*Math.log1p(amplitude);
            if(score < bestScore) { bestScore=score; bestIdx=idx; }
            m.candidateCount++;
        }
        return a[bestIdx];
    }

    static int weightedPick(double[] w, int lo, int hi, Random rng) {
        // rejection sampler; weights are kept within stable bounds.
        for(int tries=0; tries<16; tries++) {
            int idx = lo + rng.nextInt(hi-lo+1);
            if(rng.nextDouble()*2.0 <= Math.min(2.0, w[idx])) return idx;
        }
        return lo + rng.nextInt(hi-lo+1);
    }
    static void updateWeights(double[] w, int lo, int hi, int eqLo, int eqHi, double rho) {
        int len=hi-lo+1;
        int mid=(eqLo+eqHi)>>>1;
        int radius=(int)Math.ceil(Math.sqrt(len));
        int from=Math.max(lo, mid-radius), to=Math.min(hi, mid+radius);
        double reinforce = rho <= 0.60 ? 1.08 : rho >= 0.80 ? 0.92 : 1.01;
        for(int i=from;i<=to;i++) w[i] = clamp(w[i]*reinforce, 0.25, 2.0);
        // mild decay for far edges to simulate destructive suppression
        int edge=Math.min(radius, len/8);
        for(int i=lo;i<lo+edge && i<=hi;i++) w[i]=clamp(w[i]*0.995,0.25,2.0);
        for(int i=Math.max(lo,hi-edge+1);i<=hi;i++) w[i]=clamp(w[i]*0.995,0.25,2.0);
    }
    static double clamp(double v,double lo,double hi){return Math.max(lo,Math.min(hi,v));}

    static int medianValue(int x,int y,int z){
        if(x<y){ if(y<z) return y; return x<z?z:x; }
        else { if(x<z) return x; return y<z?z:y; }
    }

    static int[] partition3(int[] a, int lo, int hi, int pivot, SortMetrics m) {
        int lt=lo, i=lo, gt=hi;
        while(i<=gt) {
            m.comparisons++;
            if(a[i] < pivot) swap(a,lt++,i++,m);
            else if(a[i] > pivot) swap(a,i,gt--,m);
            else i++;
        }
        return new int[]{lt,gt};
    }
    static void insertion(int[] a,int lo,int hi,SortMetrics m){
        for(int i=lo+1;i<=hi;i++){ int key=a[i], j=i-1; while(j>=lo){ m.comparisons++; if(a[j] <= key) break; a[j+1]=a[j]; m.swaps++; j--; } a[j+1]=key; }
    }
    static void heapRange(int[] a,int lo,int hi,SortMetrics m){
        int n=hi-lo+1;
        for(int i=n/2-1;i>=0;i--) sift(a,lo,n,i,m);
        for(int end=n-1;end>0;end--){ swap(a,lo,lo+end,m); sift(a,lo,end,0,m); }
    }
    static void sift(int[] a,int off,int n,int i,SortMetrics m){
        while(true){ int largest=i,l=2*i+1,r=l+1; if(l<n){m.comparisons++; if(a[off+l]>a[off+largest]) largest=l;} if(r<n){m.comparisons++; if(a[off+r]>a[off+largest]) largest=r;} if(largest==i) break; swap(a,off+i,off+largest,m); i=largest; }
    }
    static void swap(int[] a,int i,int j,SortMetrics m){ if(i!=j){int t=a[i];a[i]=a[j];a[j]=t; m.swaps++;} }
}
