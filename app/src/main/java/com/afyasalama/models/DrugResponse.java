package com.afyasalama.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DrugResponse {
    @SerializedName("results")
    private List<DrugLabel> results;

    public List<DrugLabel> getResults() {
        return results;
    }
}
