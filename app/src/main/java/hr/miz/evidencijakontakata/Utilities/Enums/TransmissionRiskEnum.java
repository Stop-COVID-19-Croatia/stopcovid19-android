package hr.miz.evidencijakontakata.Utilities.Enums;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;
import hr.miz.evidencijakontakata.R;

public enum TransmissionRiskEnum {
    LowRisk(50),
    MediumRisk(501),
    HighRisk(769);

    int value;

    TransmissionRiskEnum(int i) {
        this.value = i;
    }

    public static String getRiskTitle(int riskLevel) {
        if (riskLevel < MediumRisk.value) {
            return CroatiaExposureNotificationApp.getStr(R.string.low_risk);
        }
        if (riskLevel < HighRisk.value) {
            return CroatiaExposureNotificationApp.getStr(R.string.medium_risk);
        }
        return CroatiaExposureNotificationApp.getStr(R.string.high_risk);
    }

    public static String getRiskShortDescription(int riskLevel) {
        if (riskLevel < MediumRisk.value) {
            return CroatiaExposureNotificationApp.getStr(R.string.low_risk_short);
        }
        if (riskLevel < HighRisk.value) {
            return CroatiaExposureNotificationApp.getStr(R.string.medium_risk_short);
        }
        return CroatiaExposureNotificationApp.getStr(R.string.high_risk_short);
    }

    public static String getRiskLongDescription(int riskLevel) {
        if (isHighRisk(riskLevel)) {
            return CroatiaExposureNotificationApp.getStr(R.string.low_risk_long);
        }
        if (isMediumRisk(riskLevel)) {
            return CroatiaExposureNotificationApp.getStr(R.string.medium_risk_long);
        }
        return CroatiaExposureNotificationApp.getStr(R.string.high_risk_long);
    }

    public static boolean isHighRisk(int value) {
        return value >= HighRisk.value;
    }

    public static boolean isMediumRisk(int value) {
        return value >= MediumRisk.value && value < HighRisk.value;
    }

    public static boolean isLowRisk(int value) {
        return value >= LowRisk.value && value < MediumRisk.value;
    }

    public static boolean isRisky(int value) {
        return value >= LowRisk.value;
    }
}
