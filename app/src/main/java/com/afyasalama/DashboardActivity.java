package com.afyasalama;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.afyasalama.adapters.MedicationAdapter;
import com.afyasalama.database.DatabaseHelper;
import com.afyasalama.models.Medication;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView rvMedications;
    private MedicationAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextView tvWaterProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DatabaseHelper(this);
        rvMedications = findViewById(R.id.rv_medications);
        rvMedications.setLayoutManager(new LinearLayoutManager(this));
        tvWaterProgress = findViewById(R.id.tv_dashboard_water);

        MaterialCardView cardWater = findViewById(R.id.card_water);
        cardWater.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, WaterIntakeActivity.class));
        });

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
        updateWaterProgress();
    }

    private void updateWaterProgress() {
        int todayIntake = dbHelper.getTodayTotalIntake();
        // For dashboard, we use a default goal or a simple summary
        tvWaterProgress.setText("Today's Progress: " + todayIntake + "ml");
    }

    private void loadMedications() {
        List<Medication> medicationList = dbHelper.getAllMedications();
        adapter = new MedicationAdapter(medicationList);
        rvMedications.setAdapter(adapter);
    }
}
