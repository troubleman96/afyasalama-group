package com.afyasalama.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.afyasalama.receivers.WaterReminderReceiver;
import java.util.Calendar;

public class WaterReminderHelper {

    public static void scheduleWaterReminders(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WaterReminderReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Schedule every 1 hour for better visibility during testing
        long interval = AlarmManager.INTERVAL_HOUR;
        long triggerTime = System.currentTimeMillis() + 1000 * 30; // First reminder in 30 seconds for immediate testing

        if (am != null) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, interval, pi);
        }
    }
}
