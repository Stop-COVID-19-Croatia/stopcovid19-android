package hr.miz.evidencijakontakata.Listeners;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.nearby.exposurenotification.TemporaryExposureKey;

import java.util.List;

public interface IExposureListener {
    void onStarted();
    void onStopped();
    void onApiException(ApiException apiException, int requestCode);
    void onExposureKeysRetrieved(List<TemporaryExposureKey> keys);
}
