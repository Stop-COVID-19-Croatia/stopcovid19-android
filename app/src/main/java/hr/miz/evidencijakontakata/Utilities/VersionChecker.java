package hr.miz.evidencijakontakata.Utilities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;
import hr.miz.evidencijakontakata.ErrorHandling.CustomError;
import hr.miz.evidencijakontakata.Services.CallBackInterface.IResponseCallback;

import hr.miz.evidencijakontakata.Services.UserService;
import okhttp3.ResponseBody;

public class VersionChecker {
    private static final String PLAY_STORE_APP_PAGE = "https://play.google.com/store/apps/details?id=hr.miz.evidencijakontakata&hl=en";
    private static final Pattern VERSION_SEEKER_REGEX = Pattern.compile("(?<=\"htlgb\">)(\\d*)\\.(\\d*)(\\.(\\d*)?)?(\\.(\\d*)?)?(?=<)");

    public static void check(IVersionListener listener) {
        if (listener != null) {
            UserService.checkGooglePlay(PLAY_STORE_APP_PAGE, new IResponseCallback<ResponseBody>() {
                @Override
                public void onSuccess(ResponseBody response) {
                    try {
                        String googlePage = response.string();
                        Matcher m = VERSION_SEEKER_REGEX.matcher(googlePage);
                        if (m.find()) {
                            listener.onVersionCheckResponse(checkForUpdate(m.group()));
                        } else {
                            listener.onVersionCheckResponse(false);
                        }
                    } catch (Exception e) {
                        listener.onVersionCheckResponse(false);
                    }
                }

                @Override
                public void onError(CustomError exception) {
                    listener.onVersionCheckResponse(false);
                }
            });
        }
    }

    private static boolean checkForUpdate(String minClientVersion) {
        String currentVersion;
        try {
            PackageInfo pInfo = CroatiaExposureNotificationApp.getInstance().getPackageManager().getPackageInfo(CroatiaExposureNotificationApp.getInstance().getPackageName(), 0);
            currentVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

        return compareVersionNames(currentVersion, minClientVersion) < 0;
    }

    private static int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;
        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i++) {
            int oldVersionPart = Integer.parseInt(oldNumbers[i]);
            int newVersionPart = Integer.parseInt(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length) ? 1 : -1;
        }

        return res;
    }

    public interface IVersionListener {
        void onVersionCheckResponse(boolean newVersionFound);
    }
}
