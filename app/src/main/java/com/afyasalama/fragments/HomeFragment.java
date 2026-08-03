package com.afyasalama.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.afyasalama.AddMedicationActivity;
import com.afyasalama.R;
import com.afyasalama.adapters.MedicationAdapter;
import com.afyasalama.database.DatabaseHelper;
import com.afyasalama.models.Medication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rvMedications;
    private MedicationAdapter adapter;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        rvMedications = view.findViewById(R.id.rv_medications);
        rvMedications.setLayoutManager(new LinearLayoutManager(requireContext()));

        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add_med);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AddMedicationActivity.class));
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMedications();
    }

    private void loadMedications() {
        List<Medication> medicationList = dbHelper.getAllMedications();
        adapter = new MedicationAdapter(medicationList);
        rvMedications.setAdapter(adapter);
    }
}
