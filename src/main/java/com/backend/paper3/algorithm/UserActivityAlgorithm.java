package com.backend.paper3.algorithm;

public class UserActivityAlgorithm {

    public static double calculateScore(
            int uploads,
            int logins
    ) {

        return (uploads * 0.6)
                + (logins * 0.4);
    }
}