package hr.miz.evidencijakontakata.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.nearby.exposurenotification.TemporaryExposureKey;

import java.util.List;

import hr.miz.evidencijakontakata.Listeners.IExposureListener;
import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.Utilities.ExposureNotifications.ExposureNotificationWrapper;
import hr.miz.evidencijakontakata.Utilities.LanguageUtil;
import hr.miz.evidencijakontakata.Utilities.VersionChecker;
import hr.miz.evidencijakontakata.databinding.ActivitySplashBinding;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SplashActivity extends BaseActivity {
    private static final int SPLASH_TIME_OUT = 1000;

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (LanguageUtil.getImageLanguage().equals("hr")) {
            binding.ivSplash.setImageDrawable(getDrawable(R.drawable.splash_screen));
        } else {
            binding.ivSplash.setImageDrawable(getDrawable(R.drawable.splash_screen_en));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(this::checkVersion, SPLASH_TIME_OUT);
    }

    private void checkVersion()
    {
        VersionChecker.check(newVersionFound -> {
            if (newVersionFound) {
                startNewActivity(UpdateActivity.class, null, false);
            } else {
                startNextActivity();
            }
        });
    }

    private void startNextActivity() {
        if (!LanguageUtil.getLanguageFlag()) {
            startNewActivity(LanguageActivity.class, null, false);
        } else {
            ExposureNotificationWrapper.get().checkEnabled(new IExposureListener() {
                @Override
                public void onStarted() {
                    startNewActivity(MainActivity.class, null, false);
                }

                @Override
                public void onStopped() {
                    startNewActivity(AppInfoActivity.class, null, false);
                }

                @Override
                public void onApiException(ApiException apiException, int requestCode) {

                }

                @Override
                public void onExposureKeysRetrieved(List<TemporaryExposureKey> keys) {

                }
            });
        }
    }
}
