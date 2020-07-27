package hr.miz.evidencijakontakata.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.nearby.exposurenotification.TemporaryExposureKey;

import java.util.List;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;
import hr.miz.evidencijakontakata.Listeners.IExposureListener;
import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.Utilities.ExposureNotifications.ExposureNotificationWrapper;
import hr.miz.evidencijakontakata.databinding.ActivityAppInfoBinding;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AppInfoActivity extends AppCompatActivity implements IExposureListener {

    private ActivityAppInfoBinding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ExposureNotificationWrapper.get().checkEnabled(this);
        setup();
    }

    private void setup() {
        binding.llServiceTurnOn.setOnClickListener(v -> toggleExposureService());
        binding.btnProceed.setOnClickListener(v -> startMainActivity());
    }

    private void toggleExposureService() {
        if (binding.swServiceTurnOn.isChecked()) {
            stopExposureNotifications();
        } else {
            startExposureNotifications();
        }
    }

    public void exposureStatusSetupView(boolean isEnabled) {
        binding.swServiceTurnOn.setChecked(isEnabled);
        binding.swServiceTurnOn.setText(isEnabled ? R.string.on : R.string.off);
    }

    private void startMainActivity() {
        Intent intent = new Intent(AppInfoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void startExposureNotifications() {
        ExposureNotificationWrapper.get().startApi(this);
    }

    public void stopExposureNotifications() {
        ExposureNotificationWrapper.get().stopApi(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ExposureNotificationWrapper.REQUEST_CODE_START_EXPOSURE_NOTIFICATION && resultCode == Activity.RESULT_OK) {
            startExposureNotifications();
        }
    }

    @Override
    public void onStarted() {
        exposureStatusSetupView(true);
    }

    @Override
    public void onStopped() {
        exposureStatusSetupView(false);
    }

    @Override
    public void onApiException(ApiException apiException, int requestCode) {
        try {
            apiException.getStatus().startResolutionForResult(this, requestCode);
        } catch (IntentSender.SendIntentException e) {
            Toast.makeText(CroatiaExposureNotificationApp.getInstance(), R.string.internal_api_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onExposureKeysRetrieved(List<TemporaryExposureKey> keys) {

    }
}
