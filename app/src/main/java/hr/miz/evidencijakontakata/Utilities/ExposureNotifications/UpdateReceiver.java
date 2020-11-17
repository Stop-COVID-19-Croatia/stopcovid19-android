package hr.miz.evidencijakontakata.Utilities.ExposureNotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null && intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {
            ExposureNotificationClientWrapper.get(context).exposureNotificationClient.isEnabled().addOnSuccessListener(this::reSchedule);
        }
    }

    private void reSchedule(boolean enabled) {
        if(enabled) {
            DownloadWorker.reSchedule();
        }
    }
}
