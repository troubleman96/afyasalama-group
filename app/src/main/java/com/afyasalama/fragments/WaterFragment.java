package com.afyasalama.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.afyasalama.R;
import com.afyasalama.database.DatabaseHelper;
import com.afyasalama.utils.WaterGoalManager;

public class WaterFragment extends Fragment implements SensorEventListener {

    private static final int ACTIVITY_RECOGNITION_REQUEST_CODE = 200;
    private DatabaseHelper dbHelper;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int currentSteps = 0;
    private float currentTemp = 25.0f; // Mock temperature
    private int dailyGoal = 2000;

    private ProgressBar pbWater;
    private TextView tvCurrentMl, tvGoalMl, tvInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        pbWater = view.findViewById(R.id.pb_water);
        tvCurrentMl = view.findViewById(R.id.tv_current_ml);
        tvGoalMl = view.findViewById(R.id.tv_goal_ml);
        tvInfo = view.findViewById(R.id.tv_info);

        view.findViewById(R.id.btn_add_250).setOnClickListener(v -> addWater(250));
        view.findViewById(R.id.btn_add_500).setOnClickListener(v -> addWater(500));

        checkPermissions();
        updateUI();

        return view;
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_RECOGNITION_REQUEST_CODE);
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
    public void onResume() {
        super.onResume();
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
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
                Toast.makeText(requireContext(), "Step tracking enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
