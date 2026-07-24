package com.afyasalama;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.afyasalama.database.DatabaseHelper;
import com.afyasalama.utils.WaterGoalManager;

public class WaterIntakeActivity extends AppCompatActivity implements SensorEventListener {

    private static final int ACTIVITY_RECOGNITION_REQUEST_CODE = 200;
    private DatabaseHelper dbHelper;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int currentSteps = 0;
    private float currentTemp = 25.0f; // Mock temperature
    private int dailyGoal = 2000;

    private ProgressBar pbWater;
    private TextView tvCurrentMl, tvGoalMl, tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_intake);

        dbHelper = new DatabaseHelper(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        pbWater = findViewById(R.id.pb_water);
        tvCurrentMl = findViewById(R.id.tv_current_ml);
        tvGoalMl = findViewById(R.id.tv_goal_ml);
        tvInfo = findViewById(R.id.tv_info);

        findViewById(R.id.btn_add_250).setOnClickListener(v -> addWater(250));
        findViewById(R.id.btn_add_500).setOnClickListener(v -> addWater(500));

        checkPermissions();
        updateUI();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_RECOGNITION_REQUEST_CODE);
            }
        }
    }

    private void addWater(int amount) {
        dbHelper.addWaterIntake(amount);
        updateUI();
    }

    private void updateUI() {
        int currentIntake = dbHelper.getTodayTotalIntake();
        dailyGoal = WaterGoalManager.calculateGoal(currentTemp, currentSteps);

        tvCurrentMl.setText(currentIntake + " ml");
        tvGoalMl.setText("Goal: " + dailyGoal + " ml");
        tvInfo.setText("Temp: " + currentTemp + "°C | Steps: " + currentSteps);

        int progress = (int) (((float) currentIntake / dailyGoal) * 100);
        pbWater.setProgress(Math.min(progress, 100));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            currentSteps = (int) event.values[0];
            updateUI();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTIVITY_RECOGNITION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Step tracking enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
