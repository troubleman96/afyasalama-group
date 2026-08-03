package com.afyasalama.models;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("main")
    private Main main;

    public float getTemperature() {
        return (main != null) ? main.temp : 25.0f;
    }

    public static class Main {
        @SerializedName("temp")
        public float temp;
    }
}
