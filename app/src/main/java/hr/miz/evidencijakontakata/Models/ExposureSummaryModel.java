package hr.miz.evidencijakontakata.Models;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.android.gms.nearby.exposurenotification.ExposureSummary;

import java.sql.Date;
import java.util.Calendar;

import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.Utilities.Enums.TransmissionRiskEnum;
import hr.miz.evidencijakontakata.Utilities.NotificationHelper;
import hr.miz.evidencijakontakata.Utilities.Util;

public class ExposureSummaryModel {
    public int[] attenuationDurationInMinutes;
    public int daysSinceLastExposure;
    public int matchedKeyCount;
    public int maxRiskScore;
    public int summationRiskScore;

    private ExposureSummaryModel() {}

    public ExposureSummaryModel(ExposureSummary exposureSummary) {
        attenuationDurationInMinutes = exposureSummary.getAttenuationDurationsInMinutes();
        daysSinceLastExposure = exposureSummary.getDaysSinceLastExposure();
        matchedKeyCount = exposureSummary.getMatchedKeyCount();
        maxRiskScore = exposureSummary.getMaximumRiskScore();
        summationRiskScore = exposureSummary.getSummationRiskScore();
    }

    public void save(String token) {
        ExposureSummaryCollection.getInstance().addNewSummary(token, this);
    }

    public static ExposureSummaryModel getForTest(int days, int keys, int maxR, int sum) {
        ExposureSummaryModel e = new ExposureSummaryModel();
        e.daysSinceLastExposure = days;
        e.matchedKeyCount = keys;
        e.attenuationDurationInMinutes = new int[]{12,32, 100, 120, 0};
        e.maxRiskScore = maxR;
        e.summationRiskScore = sum;
        return e;
    }

    public void showNotification(Context context) {
        if(matchedKeyCount > 0) {
            NotificationHelper.showNotification(context, TransmissionRiskEnum.getRiskTitle(this.summationRiskScore), TransmissionRiskEnum.getRiskShortDescription(this.summationRiskScore));
        }
    }

    public String getExposureDate() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date(System.currentTimeMillis()));
        Calendar exposureDate = Calendar.getInstance();
        exposureDate.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE) - daysSinceLastExposure, 0, 0, 0);
        return Util.dayDateFormat(exposureDate.getTimeInMillis());
    }

    @SuppressLint("DefaultLocale")
    public String getDaysSinceLastExposure(Context context) {
            return String.format("%d %s",daysSinceLastExposure, daysSinceLastExposure == 1 ? context.getString(R.string.day) : context.getString(R.string.days));
    }

    public boolean isHighRisk() {
        return TransmissionRiskEnum.isHighRisk(summationRiskScore);
    }

    public boolean isMediumRisk() {
        return TransmissionRiskEnum.isMediumRisk(summationRiskScore);
    }

    public boolean isLowRisk() {
        return TransmissionRiskEnum.isLowRisk(summationRiskScore);
    }

    public boolean isRisky() {
        return TransmissionRiskEnum.isRisky(summationRiskScore);
    }
}
