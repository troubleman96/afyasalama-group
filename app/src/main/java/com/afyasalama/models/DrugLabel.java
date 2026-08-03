package com.afyasalama.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DrugLabel {
    @SerializedName("openfda")
    private OpenFda openFda;

    @SerializedName("indications_and_usage")
    private List<String> usage;

    @SerializedName("dosage_and_administration")
    private List<String> dosage;

    @SerializedName("adverse_reactions")
    private List<String> sideEffects;

    public String getBrandName() {
        if (openFda != null && openFda.brandNames != null && !openFda.brandNames.isEmpty()) {
            return openFda.brandNames.get(0);
        }
        return "N/A";
    }

    public String getGenericName() {
        if (openFda != null && openFda.genericNames != null && !openFda.genericNames.isEmpty()) {
            return openFda.genericNames.get(0);
        }
        return "N/A";
    }

    public String getUsage() {
        return (usage != null && !usage.isEmpty()) ? usage.get(0) : "Information not available.";
    }

    public String getDosage() {
        return (dosage != null && !dosage.isEmpty()) ? dosage.get(0) : "Information not available.";
    }

    public String getSideEffects() {
        return (sideEffects != null && !sideEffects.isEmpty()) ? sideEffects.get(0) : "Information not available.";
    }

    public static class OpenFda {
        @SerializedName("brand_name")
        public List<String> brandNames;

        @SerializedName("generic_name")
        public List<String> genericNames;
    }
}
