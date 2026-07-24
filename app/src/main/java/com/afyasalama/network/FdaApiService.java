package com.afyasalama.network;

import com.afyasalama.models.DrugResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FdaApiService {
    @GET("drug/label.json")
    Call<DrugResponse> searchDrugs(@Query("search") String query, @Query("limit") int limit);
}
