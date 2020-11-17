package hr.miz.evidencijakontakata.Activities;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.nearby.exposurenotification.TemporaryExposureKey;

import java.util.List;

import hr.miz.evidencijakontakata.Listeners.IExposureListener;
import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.Utilities.ExposureNotifications.ExposureNotificationWrapper;
import hr.miz.evidencijakontakata.Utilities.LanguageUtil;
import hr.miz.evidencijakontakata.databinding.ActivityLanguageBinding;

public class LanguageActivity extends BaseActivity {

    private ActivityLanguageBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.context = this;
        LanguageUtil.setLanguageInit(context);
        LanguageUtil.setLanguageFlag(context, true);
        setup();
    }

    private void setup() {

        if (LanguageUtil.getLanguage().equals("hr")){
            binding.ivTitle.setImageDrawable(getDrawable(R.drawable.logo));
            binding.ivMz.setImageDrawable(getDrawable(R.drawable.ministvarstvo_logo_hr));
        }else{
            binding.ivTitle.setImageDrawable(getDrawable(R.drawable.logo_en));
            binding.ivMz.setImageDrawable(getDrawable(R.drawable.ministvarstvo_logo_en));
        }
        setupLanguage();

        binding.rlCroatian.setOnClickListener(view -> changeLanguage(LanguageActivity.this.getString(R.string.croatian_language_short)));
        binding.rlEnglish.setOnClickListener(view -> changeLanguage(LanguageActivity.this.getString(R.string.english_language_short)));
        binding.llStart.setOnClickListener(view -> startNextActivity());
    }

    private void setupLanguage() {
        if (LanguageUtil.getLanguage().equals("hr")) {
            binding.rlCroatian.setBackground(ContextCompat.getDrawable(context, R.drawable.button_selected_language));
            binding.rlEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.button_language));
        } else {
            binding.rlCroatian.setBackground(ContextCompat.getDrawable(context, R.drawable.button_language));
            binding.rlEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.button_selected_language));
        }
    }

    private void startNextActivity() {
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

    private void changeLanguage(String locale) {
        LanguageUtil.setLanguage(this, locale);
        recreate();
    }

    @Override
    public void recreate() {
        startActivity(getIntent());
        finish();
    }
}
