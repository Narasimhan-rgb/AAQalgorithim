package com.backend.paper3.util;

import java.util.Comparator;

public class SmartComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return 0;
        }
        if (s1 == null) {
            return -1;
        }
        if (s2 == null) {
            return 1;
        }

        try {
            double d1 = Double.parseDouble(s1.trim());
            double d2 = Double.parseDouble(s2.trim());
            return Double.compare(d1, d2);
        } catch (NumberFormatException e) {
            return s1.compareToIgnoreCase(s2);
        }
    }
}
