package hr.miz.evidencijakontakata.Utilities;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import hr.miz.evidencijakontakata.Activities.MainActivity;
import hr.miz.evidencijakontakata.R;

public class NotificationHelper {
    private static final String MAIN_CHANNEL = "mainChannel";

    private static final int SERVICE_NOTIFICATION_ID = 1234;
    private static final int BLUETOOTH_NOTIFICATION_ID = 2345;

    public static void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, MAIN_CHANNEL);
        notificationBuilder.setSmallIcon(R.drawable.icon_shield);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setOnlyAlertOnce(true);
        notificationBuilder.setContentIntent(constructPendingIntent(context, MainActivity.class));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(MAIN_CHANNEL, "App", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(MAIN_CHANNEL);
        }
        notificationManager.cancel(SERVICE_NOTIFICATION_ID);
        notificationManager.notify(BLUETOOTH_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent constructPendingIntent(Context context, Class<? extends  Activity> activityClass) {
        Intent launchIntent = new Intent(context, activityClass);
        launchIntent.setAction(Intent.ACTION_MAIN);
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
