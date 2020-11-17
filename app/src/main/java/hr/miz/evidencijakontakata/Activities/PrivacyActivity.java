package hr.miz.evidencijakontakata.Activities;

import android.os.Bundle;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;
import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.Utilities.LanguageUtil;
import hr.miz.evidencijakontakata.databinding.ActivityPrivacyBinding;

public class PrivacyActivity extends BaseActivity {

    private ActivityPrivacyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.ivMzLogo.setImageDrawable(getDrawable(LanguageUtil.getLanguage().equals("hr") ? R.drawable.ministvarstvo_logo_hr : R.drawable.ministvarstvo_logo_en));
        binding.home.setOnClickListener(view -> finish());
        binding.tvTermsAndConditions.setOnClickListener(view -> startWebView(CroatiaExposureNotificationApp.getStr(R.string.terms_url)));
        binding.tvPrivacyPolicy.setOnClickListener(view -> startWebView(getString(R.string.privacy_url)));
        binding.tvAccessibilityPolicy.setOnClickListener(view -> startWebView(getString(R.string.accessibility_url)));
        binding.tvLicense.setOnClickListener(view -> startWebView(getString(R.string.license_url)));
    }
}
