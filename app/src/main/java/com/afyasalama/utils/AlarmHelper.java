package com.afyasalama.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.afyasalama.models.Medication;
import com.afyasalama.receivers.AlarmReceiver;
import java.util.Calendar;

public class AlarmHelper {
    private static final String TAG = "AlarmHelper";

    public static void setAlarm(Context context, Medication med) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("med_name", med.getName());
        intent.putExtra("dosage", med.getDosage());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, med.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String[] timeParts = med.getTime().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (alarmManager != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                    Log.e(TAG, "Cannot schedule exact alarms. Permission missing.");
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                Log.d(TAG, "Alarm set for: " + calendar.getTime().toString() + " (Med: " + med.getName() + ")");
            } catch (SecurityException e) {
                Log.e(TAG, "SecurityException: " + e.getMessage());
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    public static void cancelAlarm(Context context, int medId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, medId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d(TAG, "Alarm cancelled for Med ID: " + medId);
        }
    }
}
