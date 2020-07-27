package hr.miz.evidencijakontakata.Models;


import java.util.ArrayList;
import java.util.List;

import hr.miz.evidencijakontakata.BuildConfig;

public class DiagnosisModel {
    private static final String COUNTRY_CODE = "HR";

    public ArrayList<TemporaryExposureKey> temporaryExposureKeys;
    public ArrayList<String> regions;
    public String appPackageName;
    //public String platform;
    //public String deviceVerificationPayload;
    //public String verificationPayload;
    //public String padding;

    private DiagnosisModel() {
        temporaryExposureKeys = new ArrayList<>();
        regions = new ArrayList<>();
    }

    public DiagnosisModel(List<com.google.android.gms.nearby.exposurenotification.TemporaryExposureKey> exposureKeys) {
        temporaryExposureKeys = new ArrayList<>();
        if(exposureKeys != null) {
            for (com.google.android.gms.nearby.exposurenotification.TemporaryExposureKey tempKey : exposureKeys) {
                temporaryExposureKeys.add(new TemporaryExposureKey(tempKey));
            }
        }
        regions = new ArrayList<>();
        regions.add(COUNTRY_CODE);
        appPackageName = BuildConfig.APPLICATION_ID;
        //platform = "android";
        //deviceVerificationPayload = "";
        //verificationPayload = "";
        //padding = "";
    }

    public static DiagnosisModel getTestModel() {
        int maxDays = 14;
        int maxRollingPeriod = 144;
        int maxTransRisk = 7;
        int keySamplesCount = 3;

        DiagnosisModel test = new DiagnosisModel();
        test.temporaryExposureKeys = new ArrayList<>();
        for(int i = 0; i < keySamplesCount; i++) {
            String randKey = "test";
            long randRollingStart = (int) ((System.currentTimeMillis() - (Math.round(Math.random() * maxDays) * 86400000)) / 600000);
            int randRollingPeriod = (int) Math.round(Math.random() * maxRollingPeriod);
            int randTransRisk = (int) Math.round(Math.random() * maxTransRisk);
            test.temporaryExposureKeys.add(new TemporaryExposureKey(randKey,  randRollingStart, randRollingPeriod, randTransRisk));
        }

        test.regions = new ArrayList<>();
        test.regions.add(COUNTRY_CODE);
        test.appPackageName = BuildConfig.APPLICATION_ID;
        //test.platform = "android";
        //test.deviceVerificationPayload = "";
        //test.verificationPayload = "";
        //test.padding = "";
        return test;
    }

    public TemporaryExposureKey getRiskiestKey() {
        TemporaryExposureKey riskiestTek = null;
        for (TemporaryExposureKey tek : temporaryExposureKeys) {
            if(riskiestTek == null || riskiestTek.transmissionRisk < tek.transmissionRisk) {
                riskiestTek = tek;
            }
        }

        return riskiestTek;
    }
}
