package hr.miz.evidencijakontakata.Utilities.ExposureNotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.nearby.exposurenotification.ExposureNotificationClient;

import hr.miz.evidencijakontakata.Models.ExposureSummaryModel;

import static com.google.android.gms.nearby.exposurenotification.ExposureNotificationClient.EXTRA_TOKEN;


public class ExposureNotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ExposureNotificationClient.ACTION_EXPOSURE_STATE_UPDATED.equals(intent.getAction())) {
            String token = intent.getStringExtra(EXTRA_TOKEN);
            ExposureNotificationWrapper.get().getFullExposureSummary(token, exposureSummary -> {
                ExposureSummaryModel currentExposure = new ExposureSummaryModel(exposureSummary);
                if(currentExposure.isRisky()) {
                    currentExposure.save(token);
                    currentExposure.showNotification(context);
                }
            });
        }
    }
}
