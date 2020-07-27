/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package hr.miz.evidencijakontakata.Utilities.ExposureNotifications;

import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.nearby.exposurenotification.ExposureNotificationStatusCodes;
import com.google.android.gms.nearby.exposurenotification.ExposureSummary;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.util.ArrayList;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;
import hr.miz.evidencijakontakata.Listeners.IExposureListener;
import hr.miz.evidencijakontakata.Models.ExposureBatch;
import hr.miz.evidencijakontakata.Models.ExposureUrlModel;
import hr.miz.evidencijakontakata.R;

public class ExposureNotificationWrapper extends ExposureNotificationClientWrapper {
    public static final int REQUEST_CODE_START_EXPOSURE_NOTIFICATION = 1111;
    public static final int REQUEST_CODE_GET_TEMP_EXPOSURE_KEY_HISTORY = 2222;

    private static ExposureNotificationWrapper instance;

    public ExposureNotificationWrapper() {
        super(CroatiaExposureNotificationApp.getInstance());
    }

    public static ExposureNotificationWrapper get() {
        if (instance == null) {
            instance = new ExposureNotificationWrapper();
        }

        return instance;
    }

    public void startApi(IExposureListener listener) {
        start()
                .addOnSuccessListener(aVoid -> onStarted(listener))
                .addOnFailureListener(e -> handleException(e, listener, REQUEST_CODE_START_EXPOSURE_NOTIFICATION));
    }

    public void requestTokenHistory(final IExposureListener listener) {
        getTemporaryExposureKeyHistory()
                .addOnSuccessListener(listener::onExposureKeysRetrieved)
                .addOnFailureListener(e -> handleException(e, listener, REQUEST_CODE_GET_TEMP_EXPOSURE_KEY_HISTORY));
    }

    public void stopApi(final IExposureListener listener) {
        stop().addOnCompleteListener(task -> checkEnabled(listener, true));
    }

    public void stopApi(final IExposureListener listener, boolean cancelDownloadWorker) {
        stop().addOnCompleteListener(task -> checkEnabled(listener, cancelDownloadWorker));
    }

    public void checkEnabled(final IExposureListener listener) {
        checkEnabled(listener, true);
    }

    public void checkEnabled(final IExposureListener listener, boolean cancelDownloadWorker) {
        if(listener != null) {
            exposureNotificationClient.isEnabled().addOnCompleteListener(task -> {
                try {
                    if (task.getResult()) {
                        onStarted(listener);
                    } else {
                        onStopped(listener, cancelDownloadWorker);
                    }
                } catch (Exception e) {
                    onStopped(listener, cancelDownloadWorker);
                }
            }).addOnFailureListener(e -> onStopped(listener, cancelDownloadWorker));
        }
    }

    void getFullExposureSummary(String token, OnSuccessListener<ExposureSummary> onSuccessListener) {
        getExposureSummary(token).addOnSuccessListener(onSuccessListener);
    }

    public static void provideKeyFiles(ArrayList<File> files, OnSuccessListener<String> onSuccessListener, OnFailureListener onFailureListener) {
        get().provideDiagnosisKeys(files, generateRandomToken())
                .addOnSuccessListener(aVoid -> {
                    ExposureUrlModel.saveList();
                    onSuccessListener.onSuccess(ExposureBatch.idFromUrl(files.get(0).getAbsolutePath()));
                })
                .addOnFailureListener(onFailureListener);
    }

    private void handleException(Exception e, IExposureListener listener, int requestCode) {
        if(e instanceof ApiException && ((ApiException) e).getStatusCode() == ExposureNotificationStatusCodes.RESOLUTION_REQUIRED) {
            listener.onApiException(((ApiException) e), requestCode);
        } else {
            Toast.makeText(CroatiaExposureNotificationApp.getInstance(), CroatiaExposureNotificationApp.getStr(R.string.internal_api_error), Toast.LENGTH_SHORT).show();
        }
    }

    private static String generateRandomToken() {
        return String.valueOf(Math.round(Math.random() * Long.MAX_VALUE));
    }

    private void onStarted(@Nullable IExposureListener listener) {
        DownloadWorker.schedule();
        if(listener != null) {
            listener.onStarted();
        }
    }
    
    private void onStopped(@Nullable IExposureListener listener, boolean cancelDownloadWorker) {
        if(cancelDownloadWorker) {
            DownloadWorker.cancel();
        }
        if(listener != null) {
            listener.onStopped();
        }
    }
}
