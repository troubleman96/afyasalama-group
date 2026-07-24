package com.afyasalama;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.afyasalama.models.DrugLabel;
import com.google.gson.Gson;

public class DrugDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_details);

        String drugJson = getIntent().getStringExtra("drug_json");
        DrugLabel drug = new Gson().fromJson(drugJson, DrugLabel.class);

        TextView tvBrand = findViewById(R.id.tv_detail_brand_name);
        TextView tvGeneric = findViewById(R.id.tv_detail_generic_name);
        TextView tvUsage = findViewById(R.id.tv_detail_usage);
        TextView tvDosage = findViewById(R.id.tv_detail_dosage);
        TextView tvSideEffects = findViewById(R.id.tv_detail_side_effects);

        tvBrand.setText(drug.getBrandName());
        tvGeneric.setText(drug.getGenericName());
        tvUsage.setText(drug.getUsage());
        tvDosage.setText(drug.getDosage());
        tvSideEffects.setText(drug.getSideEffects());
    }
}
