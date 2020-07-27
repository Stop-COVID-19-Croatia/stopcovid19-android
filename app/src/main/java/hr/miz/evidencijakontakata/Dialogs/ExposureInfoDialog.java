package hr.miz.evidencijakontakata.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import hr.miz.evidencijakontakata.Models.ExposureSummaryModel;
import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.databinding.ExposureInfoDialogBinding;

public class ExposureInfoDialog extends Dialog {
    private ExposureInfoDialogBinding binding;
    private ExposureSummaryModel exposureSummary;
    private Context context;

    private ExposureInfoDialog(@NonNull Context context, ExposureSummaryModel exposureSummary) {
        super(context);
        this.context = context;
        this.exposureSummary = exposureSummary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ExposureInfoDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setup();
    }

    private void setup() {
        binding.btnOk.setOnClickListener(v -> dismiss());
        binding.ivClose.setOnClickListener(v -> dismiss());
        if(exposureSummary != null) {
            binding.tvTitle.setText(Html.fromHtml(getContext().getString(R.string.exposure_info_title)));
            binding.tvExposureInfo.setText(getContext().getString(R.string.exposure_info, exposureSummary.getExposureDate()));
            if (exposureSummary.isHighRisk()) {
                setupHighRisk();
            } else if (exposureSummary.isMediumRisk()){
                setupMediumRisk();
            } else {
                setupLowRisk();
            }
        }
    }

    private void setupHighRisk() {
        binding.ivWarning.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.icon_highrisi));
        binding.tvHighRisk.setVisibility(View.VISIBLE);
        binding.tvRecommendation.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_grey));
        //binding.tvRecommendation.setText(Html.fromHtml(getContext().getString(R.string.next_steps_high_risk)));
        binding.tvRecommendation.setText(getContext().getString(R.string.high_risk_long));
    }

    private void setupLowRisk() {
        binding.ivWarning.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.icon_risk));
        binding.tvHighRisk.setVisibility(View.GONE);
        binding.tvRecommendation.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_grey_outline));
        //binding.tvRecommendation.setText(Html.fromHtml(getContext().getString(R.string.next_steps_low_risk)));
        binding.tvRecommendation.setText(getContext().getString(R.string.medium_risk_long));
    }

    private void setupMediumRisk() {
        binding.ivWarning.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.icon_risk));
        binding.tvHighRisk.setVisibility(View.GONE);
        binding.tvRecommendation.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_grey_outline));
        //binding.tvRecommendation.setText(Html.fromHtml(getContext().getString(R.string.next_steps_low_risk)));
        binding.tvRecommendation.setText(getContext().getString(R.string.low_risk_long));
    }

    public static void start(Context context, ExposureSummaryModel summary) {
        new ExposureInfoDialog(context, summary).show();
    }
}
