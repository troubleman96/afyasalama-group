package com.afyasalama.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.afyasalama.models.WeatherResponse;
import com.afyasalama.network.RetrofitClient;
import com.afyasalama.utils.WaterGoalManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaterFragment extends Fragment implements SensorEventListener {

    private static final String TAG = "WaterFragment";
    private static final int PERMISSION_REQUEST_CODE = 300;
    private static final String WEATHER_API_KEY = "YOUR_API_KEY_HERE"; // Placeholder

    private DatabaseHelper dbHelper;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private FusedLocationProviderClient fusedLocationClient;

    private int currentStepsDelta = 0;
    private int sensorCumulativeSteps = 0;
    private float currentTemp = 25.0f;
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        pbWater = view.findViewById(R.id.pb_water);
        tvCurrentMl = view.findViewById(R.id.tv_current_ml);
        tvGoalMl = view.findViewById(R.id.tv_goal_ml);
        tvInfo = view.findViewById(R.id.tv_info);

        view.findViewById(R.id.btn_add_250).setOnClickListener(v -> addWater(250));
        view.findViewById(R.id.btn_add_500).setOnClickListener(v -> addWater(500));

        checkPermissions();
        fetchLocationAndWeather();
        updateUI();

        return view;
    }

    private void checkPermissions() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions = new String[]{
                    Manifest.permission.ACTIVITY_RECOGNITION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
        }

        boolean allGranted = true;
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), p) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    private void fetchLocationAndWeather() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                if (location != null) {
                    getWeather(location.getLatitude(), location.getLongitude());
                } else {
                    Log.w(TAG, "Location is null");
                }
            });
        }
    }

    private void getWeather(double lat, double lon) {
        if (WEATHER_API_KEY.equals("YOUR_API_KEY_HERE")) return;

        RetrofitClient.getApiService().getWeather(lat, lon, WEATHER_API_KEY, "metric").enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    currentTemp = response.body().getTemperature();
                    updateUI();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e(TAG, "Weather fetch failed: " + t.getMessage());
            }
        });
    }

    private void addWater(int amount) {
        dbHelper.addWaterIntake(amount);
        updateUI();
    }

    private void updateUI() {
        int currentIntake = dbHelper.getTodayTotalIntake();
        dailyGoal = WaterGoalManager.calculateGoal(currentTemp, currentStepsDelta);

        tvCurrentMl.setText(String.format(Locale.getDefault(), "%d ml", currentIntake));
        tvGoalMl.setText(String.format(Locale.getDefault(), "Goal: %d ml", dailyGoal));
        tvInfo.setText(String.format(Locale.getDefault(), "Temp: %.1f°C | Steps: %d", currentTemp, currentStepsDelta));

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
            int sensorValue = (int) event.values[0];
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            
            int baseline = dbHelper.getStepsBaseline(today);
            if (baseline == -1) {
                // First time sensor seen today, record baseline
                dbHelper.setStepsBaseline(today, sensorValue);
                currentStepsDelta = 0;
            } else {
                currentStepsDelta = sensorValue - baseline;
                // Handle sensor resets/reboots
                if (currentStepsDelta < 0) {
                    dbHelper.setStepsBaseline(today, sensorValue);
                    currentStepsDelta = 0;
                }
            }
            updateUI();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int res : grantResults) {
                if (res != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                fetchLocationAndWeather();
                Toast.makeText(requireContext(), "Permissions granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
