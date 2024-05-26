package com.example.daybuddy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "MY_CHANNEL_ID";
    public static final String NOTIFICATION_TEXT_KEY = "notification_text_key";

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the notification text from the Intent
        String notificationText = intent.getStringExtra(NOTIFICATION_TEXT_KEY);
        if (notificationText == null) {
            notificationText = "Default notification text";
        }

        // Check if the app has permission to play sound
        boolean hasSoundPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE)
                == PackageManager.PERMISSION_GRANTED;

        // Create the notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification Channel";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Set sound for the channel if permission is granted
            if (hasSoundPermission) {
                Uri soundUri = Settings.System.DEFAULT_NOTIFICATION_URI;
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                channel.setSound(soundUri, attributes);
            } else {
                // Set silent sound if permission is not granted
                channel.setSound(null, null);
            }

            // Enable vibration
            channel.enableVibration(true);

            // Create the notification channel
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon) // your app icon
                .setContentTitle("Scheduled Notification")
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Set priority to high
                .setDefaults(NotificationCompat.DEFAULT_ALL) // Use default sound and vibration
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true);

        // Send the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}
