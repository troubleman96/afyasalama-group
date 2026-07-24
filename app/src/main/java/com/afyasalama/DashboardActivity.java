package com.afyasalama;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.afyasalama.adapters.MedicationAdapter;
import com.afyasalama.database.DatabaseHelper;
import com.afyasalama.models.Medication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView rvMedications;
    private MedicationAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DatabaseHelper(this);
        rvMedications = findViewById(R.id.rv_medications);
        rvMedications.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabAdd = findViewById(R.id.fab_add_med);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AddMedicationActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedications();
    }

    private void loadMedications() {
        List<Medication> medicationList = dbHelper.getAllMedications();
        adapter = new MedicationAdapter(medicationList);
        rvMedications.setAdapter(adapter);
    }
}
