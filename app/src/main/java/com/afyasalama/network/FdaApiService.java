package com.afyasalama.network;

import com.afyasalama.models.DrugResponse;
import com.afyasalama.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FdaApiService {
    @GET("drug/label.json")
    Call<DrugResponse> searchDrugs(@Query("search") String query, @Query("limit") int limit);

    @GET("https://api.openweathermap.org/data/2.5/weather")
    Call<WeatherResponse> getWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}
