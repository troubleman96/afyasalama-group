package com.afyasalama;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.afyasalama.database.DatabaseHelper;
import com.afyasalama.models.Medication;
import com.afyasalama.utils.AlarmHelper;
import java.util.Calendar;
import java.util.Locale;

public class AddMedicationActivity extends AppCompatActivity {
    private EditText etName, etDosage;
    private TextView tvTime, tvTitle;
    private String selectedTime = "";
    private DatabaseHelper dbHelper;
    private Medication editMedication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        dbHelper = new DatabaseHelper(this);
        tvTitle = findViewById(R.id.tv_add_med_title);
        etName = findViewById(R.id.et_med_name);
        etDosage = findViewById(R.id.et_dosage);
        tvTime = findViewById(R.id.tv_selected_time);

        // Check if we are in Edit Mode
        if (getIntent().hasExtra("medication")) {
            editMedication = (Medication) getIntent().getSerializableExtra("medication");
            if (editMedication != null) {
                tvTitle.setText("Edit Medication");
                etName.setText(editMedication.getName());
                etDosage.setText(editMedication.getDosage());
                selectedTime = editMedication.getTime();
                tvTime.setText("Reminder set for: " + selectedTime);
            }
        }

        Button btnPickTime = findViewById(R.id.btn_pick_time);
        btnPickTime.setOnClickListener(v -> showTimePicker());

        Button btnSave = findViewById(R.id.btn_save_med);
        btnSave.setOnClickListener(v -> saveMedication());
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        if (editMedication != null) {
            String[] parts = selectedTime.split(":");
            hour = Integer.parseInt(parts[0]);
            minute = Integer.parseInt(parts[1]);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
            tvTime.setText("Reminder set for: " + selectedTime);
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void saveMedication() {
        String name = etName.getText().toString();
        String dosage = etDosage.getText().toString();

        if (name.isEmpty() || dosage.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editMedication == null) {
            // Add New
            Medication med = new Medication(0, name, dosage, selectedTime);
            long id = dbHelper.addMedication(med);
            med.setId((int) id);
            AlarmHelper.setAlarm(this, med);
            Toast.makeText(this, "Medication added!", Toast.LENGTH_SHORT).show();
        } else {
            // Update Existing
            editMedication.setName(name);
            editMedication.setDosage(dosage);
            editMedication.setTime(selectedTime);
            dbHelper.updateMedication(editMedication);
            
            // Re-schedule alarm (it will overwrite the old one since PendingIntent ID is the same)
            AlarmHelper.setAlarm(this, editMedication);
            Toast.makeText(this, "Medication updated!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
