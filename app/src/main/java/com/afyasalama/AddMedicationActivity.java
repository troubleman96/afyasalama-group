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
    private TextView tvTime;
    private String selectedTime = "";
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        dbHelper = new DatabaseHelper(this);
        etName = findViewById(R.id.et_med_name);
        etDosage = findViewById(R.id.et_dosage);
        tvTime = findViewById(R.id.tv_selected_time);

        Button btnPickTime = findViewById(R.id.btn_pick_time);
        btnPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        Button btnSave = findViewById(R.id.btn_save_med);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedication();
            }
        });
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                tvTime.setText("Reminder set for: " + selectedTime);
            }
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

        Medication med = new Medication(0, name, dosage, selectedTime);
        long id = dbHelper.addMedication(med);
        med.setId((int) id);

        AlarmHelper.setAlarm(this, med);

        Toast.makeText(this, "Medication added!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
