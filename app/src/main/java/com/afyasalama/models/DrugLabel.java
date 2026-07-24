package com.afyasalama.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DrugLabel {
    @SerializedName("brand_name")
    private List<String> brandNames;

    @SerializedName("generic_name")
    private List<String> genericNames;

    @SerializedName("indications_and_usage")
    private List<String> usage;

    @SerializedName("dosage_and_administration")
    private List<String> dosage;

    @SerializedName("adverse_reactions")
    private List<String> sideEffects;

    public String getBrandName() {
        return (brandNames != null && !brandNames.isEmpty()) ? brandNames.get(0) : "N/A";
    }

    public String getGenericName() {
        return (genericNames != null && !genericNames.isEmpty()) ? genericNames.get(0) : "N/A";
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
}
