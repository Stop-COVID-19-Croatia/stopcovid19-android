package hr.miz.evidencijakontakata.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnectivityUtil {
    private static final String STRING_WIFI = "WIFI";
    private static final String STRING_MOBILE = "MOBILE";

    public static boolean isNetworkAvailable(Context context) {
        boolean wifiDataAvailable = false;
        boolean mobileDataAvailable = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            if (networkInfo.getTypeName().equalsIgnoreCase(STRING_WIFI))
                if (networkInfo.isConnected())
                    wifiDataAvailable = true;
            if (networkInfo.getTypeName().equalsIgnoreCase(STRING_MOBILE))
                if (networkInfo.isConnected())
                    mobileDataAvailable = true;
        }
        return wifiDataAvailable || mobileDataAvailable;
    }
}
