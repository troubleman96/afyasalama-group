package com.afyasalama.utils;

public class WaterGoalManager {
    private static final int BASE_GOAL = 2000; // ml

    public static int calculateGoal(float temperatureCelsius, int steps) {
        int goal = BASE_GOAL;

        // Temperature adjustment: +50ml for every 1°C above 25°C
        if (temperatureCelsius > 25) {
            int extraTemp = (int) ((temperatureCelsius - 25) * 50);
            goal += extraTemp;
        }

        // Activity adjustment: +100ml for every 1000 steps
        int extraSteps = (steps / 1000) * 100;
        goal += extraSteps;

        return goal;
    }
}
