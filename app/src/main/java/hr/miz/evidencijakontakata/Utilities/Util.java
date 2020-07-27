package hr.miz.evidencijakontakata.Utilities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;
import hr.miz.evidencijakontakata.Listeners.INoNetworkListener;
import hr.miz.evidencijakontakata.R;

public class Util {
    private static final String dayDateFormat = "dd.MM.yyyy";
    private static final String dayDateTimeFormat = "dd.MM.yyyy HH.mm.ss";

    public static FragmentTransaction getAnimatedFragmentTransaction(FragmentTransaction fragmentTransaction) {
        return fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit);
    }

    private static String dateFormat(Long date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, CroatiaExposureNotificationApp.getInstance().getResources().getConfiguration().locale);
        return sdf.format(date);
    }

    public static String dayDateFormat(int year, int month, int dayOfMonth) {
        return dayDateFormat(Util.convertToDate(year, month, dayOfMonth).getTime());
    }

    public static String dayDateFormat(long date) {
        return dateFormat(date, dayDateFormat);
    }

    public static String dayDateTimeFormat(long date) {
        return dateFormat(date, dayDateTimeFormat);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date convertToDate(int year, int month, int dayOfMonth) {
        String givenDateString = String.format("%s-%s-%s", year, month+1, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(givenDateString);
        } catch (Exception ignore) { }
        return new Date(System.currentTimeMillis());
    }

    public static void hideKeyboard(View view, Context context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void startErrorDialog(String error, Context context) {
        startMessageDialog(context,  context.getString(R.string.error), error, null);
    }

    public static void startMessageDialog(Context context, String title, String message, final INoNetworkListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_error, null, false);
        TextView tvTitle = (TextView) dialogView.findViewById(R.id.titleError);
        TextView tvMessage = (TextView) dialogView.findViewById(R.id.errors);
        Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        tvTitle.setText(title);
        tvMessage.setMovementMethod(new ScrollingMovementMethod());
        tvMessage.setText(message);
        buttonCancel.setText(context.getString(R.string.close));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickRefresh();
                }
                alertDialog.dismiss();
            }
        });
    }

    public static boolean checkForUpdate(Context context, String minClientVersion) {
        PackageInfo pInfo = new PackageInfo();
        String currentVersion = "";
        Boolean result = false;

        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(compareVersionNames(currentVersion,minClientVersion)<0){
            result = true;
        }else{
            result = false;
        }

        return result;
    }

    public static int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;
        
        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        // To avoid IndexOutOfBounds
        int maxIndex = Math.min(oldNumbers.length-1, newNumbers.length-1);

        for (int i = 0; i < maxIndex; i ++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

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
            res = (oldNumbers.length > newNumbers.length)?1:-1;
        }

        return res;
    }

    public static boolean isBluetoothOn() {
        try {
            LocationManager man = (LocationManager) CroatiaExposureNotificationApp.getInstance().getSystemService(Context.LOCATION_SERVICE);
            return BluetoothAdapter.getDefaultAdapter().isEnabled() && BluetoothAdapter.getDefaultAdapter().getState() == BluetoothAdapter.STATE_ON && (man == null || LocationManagerCompat.isLocationEnabled(man));
        } catch (Exception e) {
            return false;
        }
    }
}
