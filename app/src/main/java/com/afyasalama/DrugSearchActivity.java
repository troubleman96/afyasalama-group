package com.afyasalama;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.afyasalama.adapters.DrugAdapter;
import com.afyasalama.models.DrugLabel;
import com.afyasalama.models.DrugResponse;
import com.afyasalama.network.RetrofitClient;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrugSearchActivity extends AppCompatActivity {
    private EditText etSearch;
    private RecyclerView rvDrugs;
    private DrugAdapter adapter;
    private ProgressBar pbSearch;
    private List<DrugLabel> drugList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_search);

        etSearch = findViewById(R.id.et_drug_search);
        rvDrugs = findViewById(R.id.rv_drugs);
        pbSearch = findViewById(R.id.pb_search);

        rvDrugs.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DrugAdapter(drugList, drug -> {
            Intent intent = new Intent(DrugSearchActivity.this, DrugDetailActivity.class);
            intent.putExtra("drug_json", new Gson().toJson(drug));
            startActivity(intent);
        });
        rvDrugs.setAdapter(adapter);

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void performSearch(String query) {
        if (query.isEmpty()) return;

        pbSearch.setVisibility(View.VISIBLE);
        String searchQuery = "openfda.brand_name:\"" + query + "\"";

        RetrofitClient.getApiService().searchDrugs(searchQuery, 10).enqueue(new Callback<DrugResponse>() {
            @Override
            public void onResponse(Call<DrugResponse> call, Response<DrugResponse> response) {
                pbSearch.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    drugList.clear();
                    drugList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DrugSearchActivity.this, "No results found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DrugResponse> call, Throwable t) {
                pbSearch.setVisibility(View.GONE);
                Toast.makeText(DrugSearchActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
