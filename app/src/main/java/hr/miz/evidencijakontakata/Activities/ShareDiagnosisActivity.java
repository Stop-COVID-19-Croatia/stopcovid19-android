package hr.miz.evidencijakontakata.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.nearby.exposurenotification.TemporaryExposureKey;

import java.util.Calendar;
import java.util.List;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;
import hr.miz.evidencijakontakata.ErrorHandling.CustomError;
import hr.miz.evidencijakontakata.ErrorHandling.ErrorHandling;
import hr.miz.evidencijakontakata.Listeners.IExposureListener;
import hr.miz.evidencijakontakata.Models.DiagnosisModel;
import hr.miz.evidencijakontakata.Models.PositiveTestManager;
import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.Services.CallBackInterface.IResponseCallback;
import hr.miz.evidencijakontakata.Services.UserService;
import hr.miz.evidencijakontakata.Utilities.ExposureNotifications.ExposureNotificationWrapper;
import hr.miz.evidencijakontakata.Utilities.Util;
import hr.miz.evidencijakontakata.databinding.FragmentShareDiagnosisEditBinding;

public class ShareDiagnosisActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, IExposureListener {
    private static final int CODE_LENGTH = 8;
    private FragmentShareDiagnosisEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentShareDiagnosisEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.shareNextButton.setOnClickListener(view -> ExposureNotificationWrapper.get().requestTokenHistory(this));
        binding.home.setOnClickListener(view -> onBackPressed());
        binding.btnDate.setOnClickListener(v -> startDatePicker());
        binding.shareTestIdentifier.addTextChangedListener(shareTestIdentifierTextWatcher);
        binding.mainContainer.setOnClickListener(v -> Util.hideKeyboard(binding.tvShareId,this));
    }

    private void startDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void shareExposureInfo(DiagnosisModel diagnosisModel) {
        ProgressBarDialog.showDialog(getSupportFragmentManager(), "");
        UserService.diagnosisKeys(binding.shareTestIdentifier.getText().toString().trim(), diagnosisModel, new IResponseCallback() {
            @Override
            public void onSuccess(Object response) {
                ProgressBarDialog.dismissDialog();
                PositiveTestManager.get().add();
                Toast.makeText(CroatiaExposureNotificationApp.getInstance(), R.string.exposure_info_upload_successful, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onError(CustomError exception) {
                ProgressBarDialog.dismissDialog();
                ErrorHandling.handlingError(ShareDiagnosisActivity.this, exception, () -> shareExposureInfo(diagnosisModel));
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        binding.shareTestDate.setText(Util.dayDateFormat(year, month, dayOfMonth));
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onApiException(ApiException apiException, int requestCode) {
        try {
            apiException.getStatus().startResolutionForResult(this, requestCode);
        } catch (IntentSender.SendIntentException e) {
            Toast.makeText(CroatiaExposureNotificationApp.getInstance(), getString(R.string.internal_api_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onExposureKeysRetrieved(List<TemporaryExposureKey> keys) {
        DiagnosisModel diagnosisModel = new DiagnosisModel(keys);
        shareExposureInfo(diagnosisModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ExposureNotificationWrapper.REQUEST_CODE_GET_TEMP_EXPOSURE_KEY_HISTORY && resultCode == Activity.RESULT_OK) {
            ExposureNotificationWrapper.get().requestTokenHistory(this);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    TextWatcher shareTestIdentifierTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().length() == CODE_LENGTH) {
                setBtnEnabling(binding.shareNextButton, 1, true);
            } else {
                setBtnEnabling(binding.shareNextButton, .5f, false);
            }
        }
    };

    private void setBtnEnabling(View view, float alpha, boolean isEnabled) {
        view.setAlpha(alpha);
        view.setEnabled(isEnabled);
        view.setClickable(isEnabled);
    }
}
