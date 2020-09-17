package hr.miz.evidencijakontakata.Utilities.ExposureNotifications;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;
import hr.miz.evidencijakontakata.Services.Client.ApiClient;

public class DownloadWorker extends Worker {
    private static final int WORKER_INTERVAL_DELAY = 4;
    private static boolean didCheck = false;

    static void schedule() {
        Constraints workConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        int initialDelay = (int) (Math.random() * WORKER_INTERVAL_DELAY * 60);
        PeriodicWorkRequest downloadWork = new PeriodicWorkRequest.Builder(DownloadWorker.class, WORKER_INTERVAL_DELAY, TimeUnit.HOURS)
                .setConstraints(workConstraints)
                .setInitialDelay(initialDelay, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(CroatiaExposureNotificationApp.getInstance()).enqueueUniquePeriodicWork(DownloadWorker.class.getName(), ExistingPeriodicWorkPolicy.KEEP , downloadWork);
        forceCheck();
    }

    static void cancel() {
        WorkManager.getInstance(CroatiaExposureNotificationApp.getInstance()).cancelAllWorkByTag(DownloadWorker.class.getName());
    }

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NotNull
    @Override
    public Result doWork() {
        ApiClient.renew();
        ExposureProvider.initCheck();
        return Result.success();
    }

    private static void forceCheck() {
        if (!didCheck) {
            didCheck = true;
            ExposureProvider.initCheck();
        }
    }
}