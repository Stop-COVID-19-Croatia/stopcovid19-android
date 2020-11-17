package hr.miz.evidencijakontakata.Models;


import java.util.ArrayList;
import java.util.List;

import hr.miz.evidencijakontakata.BuildConfig;
import hr.miz.evidencijakontakata.Utilities.StorageUtils;

public class DiagnosisModel {
    public static final String DOMESTIC_COUNTRY_CODE = "HR";
    private static final String CODE_CONSENT_TO_FEDERATION = "codeConsentToFederation";

    public ArrayList<TemporaryExposureKey> temporaryExposureKeys;
    public ArrayList<String> regions;
    public boolean consentToFederation;
    public String appPackageName;

    public DiagnosisModel(List<com.google.android.gms.nearby.exposurenotification.TemporaryExposureKey> exposureKeys) {
        temporaryExposureKeys = new ArrayList<>();
        if(exposureKeys != null) {
            for (com.google.android.gms.nearby.exposurenotification.TemporaryExposureKey tempKey : exposureKeys) {
                temporaryExposureKeys.add(new TemporaryExposureKey(tempKey));
            }
        }
        regions = new ArrayList<>();
        regions.add(DOMESTIC_COUNTRY_CODE);
        consentToFederation = StorageUtils.getBoolean(CODE_CONSENT_TO_FEDERATION, false);
        appPackageName = BuildConfig.APPLICATION_ID;
    }

    public static boolean consentToFederation() {
        return StorageUtils.getBoolean(CODE_CONSENT_TO_FEDERATION, false);
    }

    public static void saveConsentToFederation(boolean consent) {
        StorageUtils.saveBoolean(CODE_CONSENT_TO_FEDERATION, consent);
    }
}
