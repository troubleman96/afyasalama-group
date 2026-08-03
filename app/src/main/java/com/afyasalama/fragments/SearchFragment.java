package com.afyasalama.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.afyasalama.DrugDetailActivity;
import com.afyasalama.R;
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

public class SearchFragment extends Fragment {
    private EditText etSearch;
    private RecyclerView rvDrugs;
    private DrugAdapter adapter;
    private ProgressBar pbSearch;
    private List<DrugLabel> drugList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        etSearch = view.findViewById(R.id.et_drug_search);
        rvDrugs = view.findViewById(R.id.rv_drugs);
        pbSearch = view.findViewById(R.id.pb_search);

        rvDrugs.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new DrugAdapter(drugList, drug -> {
            Intent intent = new Intent(requireActivity(), DrugDetailActivity.class);
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

        return view;
    }

    private void performSearch(String query) {
        if (query.isEmpty()) return;

        pbSearch.setVisibility(View.VISIBLE);
        String searchQuery = "openfda.brand_name:\"" + query + "\"";

        RetrofitClient.getApiService().searchDrugs(searchQuery, 10).enqueue(new Callback<DrugResponse>() {
            @Override
            public void onResponse(Call<DrugResponse> call, Response<DrugResponse> response) {
                if (!isAdded()) return;
                pbSearch.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                    drugList.clear();
                    drugList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                } else {
                    drugList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(requireContext(), "No results found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DrugResponse> call, Throwable t) {
                if (!isAdded()) return;
                pbSearch.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
