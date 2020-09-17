package hr.miz.evidencijakontakata.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;
import hr.miz.evidencijakontakata.R;

public class LanguageUtil {
    private static final String keyLanguage = "Language";
    private static final String keyFlag = "Flag";

    public static void setLanguageInit(Context context) {
        if (!LanguageUtil.getLanguage().isEmpty()) {
            setLanguage(context, getLanguage());
        } else {
            setLanguage(context, context.getString(R.string.croatian_language_short));
        }
    }

    public static void setLanguage(Context context, String language) {
        setLocale(context, language);
        SharedPreferences.Editor editor = CroatiaExposureNotificationApp.sharedPref.edit();
        editor.putString(keyLanguage, language);
        editor.commit();
    }

    public static String getLanguage() {
        return CroatiaExposureNotificationApp.sharedPref.getString(keyLanguage, "");
    }

    private static void setLocale(Context context, String language) {
        Locale locale = new Locale(language, language);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static String getImageLanguage() {
        if (LanguageUtil.getLanguage().isEmpty()) {
            return Locale.getDefault().getLanguage();
        }
        return LanguageUtil.getLanguage();
    }

    public static void setLanguageFlag(Context context, Boolean flag) {
        SharedPreferences.Editor editor = CroatiaExposureNotificationApp.sharedPref.edit();
        editor.putBoolean(keyFlag, flag);
        editor.commit();
    }

    public static Boolean getLanguageFlag() {
        return CroatiaExposureNotificationApp.sharedPref.getBoolean(keyFlag, false);
    }
}
