package hr.miz.evidencijakontakata.Models;

import android.content.Context;

import com.google.android.gms.nearby.exposurenotification.ExposureSummary;

import java.sql.Date;
import java.util.Calendar;
import java.util.Objects;

import hr.miz.evidencijakontakata.Utilities.Enums.TransmissionRiskEnum;
import hr.miz.evidencijakontakata.Utilities.ExposureNotifications.ExposureConfigurations;
import hr.miz.evidencijakontakata.Utilities.NotificationHelper;
import hr.miz.evidencijakontakata.Utilities.Util;

public class ExposureSummaryModel {
    public String token;
    public int[] attenuationDurationInMinutes;
    public int daysSinceLastExposure;
    public int matchedKeyCount;
    public int maxRiskScore;
    public int summationRiskScore;
    private Long date;

    public ExposureSummaryModel(ExposureSummary exposureSummary, String token) {
        this.token = token;
        this.attenuationDurationInMinutes = exposureSummary.getAttenuationDurationsInMinutes();
        this.daysSinceLastExposure = exposureSummary.getDaysSinceLastExposure();
        this.setDate();
        this.matchedKeyCount = exposureSummary.getMatchedKeyCount();
        this.maxRiskScore = exposureSummary.getMaximumRiskScore();
        this.summationRiskScore = exposureSummary.getSummationRiskScore();
    }

    public void save() {
        ExposureSummaryCollection.getInstance().addSummary(this);
    }

    public void showNotification(Context context) {
        if(matchedKeyCount > 0) {
            NotificationHelper.showNotification(context, TransmissionRiskEnum.getRiskTitle(this.summationRiskScore), TransmissionRiskEnum.getRiskShortDescription(this.summationRiskScore));
        }
    }

    private void setDate() {
        if(this.date == null) {
            if(daysSinceLastExposure < 0 || daysSinceLastExposure > 15) {
                date = 1L;
            } else {
                Calendar today = Calendar.getInstance();
                today.setTime(new Date(System.currentTimeMillis()));
                Calendar exposureDate = Calendar.getInstance();
                exposureDate.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE) - daysSinceLastExposure, 0, 0, 0);
                this.date = exposureDate.getTimeInMillis();
            }
        }
    }

    public String getExposureDate() {
        return Util.dayDateFormat(date);
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

    private long getDate() {
        this.setDate();
        return date;
    }

    public boolean isValid() {
        return System.currentTimeMillis() - getDate() <= ExposureConfigurations.EXPOSURE_VALIDITY_LIFETIME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        String token;
        if (o instanceof ExposureSummaryModel) {
            token = ((ExposureSummaryModel) o).token;
        } else if(o instanceof String) {
            token = (String) o;
        } else {
            return false;
        }
        return this.token.equals(token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
