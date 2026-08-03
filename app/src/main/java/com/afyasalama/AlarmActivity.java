package com.afyasalama;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.afyasalama.utils.ShakeDetector;

public class AlarmActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Ringtone mRingtone;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Ensure screen turns on and shows over lock screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        }
        
        setContentView(R.layout.activity_alarm);

        String medName = getIntent().getStringExtra("med_name");
        String dosage = getIntent().getStringExtra("dosage");

        TextView tvName = findViewById(R.id.tv_alarm_med_name);
        TextView tvDosage = findViewById(R.id.tv_alarm_dosage);

        tvName.setText(medName);
        tvDosage.setText("Dosage: " + dosage);

        // Start Alarm Sound
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        mRingtone = RingtoneManager.getRingtone(this, alarmUri);
        mRingtone.play();

        // Start Vibration
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (mVibrator != null) {
            long[] pattern = {0, 500, 500};
            mVibrator.vibrate(pattern, 0);
        }

        // Initialize Shake Detection
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if (count >= 3) { // Require 3 shakes
                    stopAlarm();
                }
            }
        });
    }

    private void stopAlarm() {
        if (mRingtone != null && mRingtone.isPlaying()) {
            mRingtone.stop();
        }
        if (mVibrator != null) {
            mVibrator.cancel();
        }
        Toast.makeText(this, "Medication Taken!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mRingtone != null && mRingtone.isPlaying()) {
            mRingtone.stop();
        }
        if (mVibrator != null) {
            mVibrator.cancel();
        }
        super.onDestroy();
    }
}
