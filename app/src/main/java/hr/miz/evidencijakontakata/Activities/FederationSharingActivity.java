package hr.miz.evidencijakontakata.Activities;

import android.os.Bundle;

import androidx.core.content.ContextCompat;

import hr.miz.evidencijakontakata.Dialogs.ActionDialog;
import hr.miz.evidencijakontakata.Models.DiagnosisModel;
import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.databinding.ActivityFederationConsentBinding;

public class FederationSharingActivity extends BaseActivity implements ActionDialog.IExposureActivationDialog {
    private ActivityFederationConsentBinding binding;
    private boolean federationSharing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFederationConsentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        federationSharing = DiagnosisModel.consentToFederation();
        binding.btnSwitch.setOnClickListener(v -> startConsentDialog());
        binding.home.setOnClickListener(v -> finish());
        setFederationSharingState(federationSharing);
    }

    private void startConsentDialog() {
        if(federationSharing) {
            setFederationSharingState(false);
        } else {
            ActionDialog.start(this, getString(R.string.keys_exchange_consent_title), getString(R.string.keys_exchange_consent_description), getString(R.string.accept), this);
        }
    }

    private void setFederationSharingState(boolean sharingState) {
        federationSharing = sharingState;
        DiagnosisModel.saveConsentToFederation(sharingState);
        binding.switchOnOff.setChecked(sharingState);
        binding.rlSwitch.setBackground(ContextCompat.getDrawable(this, sharingState ? R.drawable.button_green : R.drawable.button_grey));
        binding.tvOn.setTextColor(ContextCompat.getColor(this, sharingState ? R.color.white : R.color.grey_text_color));
        binding.tvOn.setText(sharingState ? R.string.on : R.string.off);
    }

    @Override
    public void onDialogAction(boolean accept) {
        if(accept) {
            setFederationSharingState(true);
        }
    }
}
