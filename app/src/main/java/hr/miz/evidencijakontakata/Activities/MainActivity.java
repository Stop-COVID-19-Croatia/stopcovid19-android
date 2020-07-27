package hr.miz.evidencijakontakata.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.nearby.exposurenotification.TemporaryExposureKey;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;
import hr.miz.evidencijakontakata.Fragments.ExposuresFragment;
import hr.miz.evidencijakontakata.Fragments.NotifyOthersFragment;
import hr.miz.evidencijakontakata.Listeners.IExposureListener;
import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.Utilities.CustomTypefaceSpan;
import hr.miz.evidencijakontakata.Utilities.ExposureNotifications.ExposureNotificationWrapper;
import hr.miz.evidencijakontakata.Utilities.LanguageUtil;
import hr.miz.evidencijakontakata.Utilities.Util;
import hr.miz.evidencijakontakata.databinding.ActivityMainBinding;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static hr.miz.evidencijakontakata.Utilities.Util.getAnimatedFragmentTransaction;
import static hr.miz.evidencijakontakata.Utilities.Util.isBluetoothOn;

public class MainActivity extends AppCompatActivity implements IExposureListener {
    private IExposureStatusChanged exposureListener;
    private ActivityMainBinding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.setLanguageInit(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setNavigation();
        loadInitialFragment();
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setNavigationMenuFont();
    }

    private void loadInitialFragment() {
        loadFragment(new ExposuresFragment(), R.id.llPage01, false);
        loadFragment(new NotifyOthersFragment(), R.id.llPage02, false);
    }

    public void setNavigation() {
        binding.navigation.inflateMenu(R.menu.navigation);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (binding.navigation.getSelectedItemId() == item.getItemId()) {
                return false;
            }

            switch (item.getItemId()) {
                case R.id.navigation_notification:
                    binding.lpOrders.setCurrentItem(0);
                    return true;
                case R.id.navigation_notify_others:
                    binding.lpOrders.setCurrentItem(1);
                    return true;
            }

            return false;
        }
    };

    protected void loadFragment(Fragment fragment, @IdRes int containerId, boolean animated) {
        cleanUpFragments(null);
        replaceFragment(fragment, containerId, false, animated);
    }

    public void replaceFragment(Fragment fragment, @IdRes int containerId, boolean addToBackStack, boolean animated) {
        FragmentTransaction transaction = animated ? getAnimatedFragmentTransaction(getSupportFragmentManager().beginTransaction()) : getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(containerId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void cleanUpFragments(@Nullable String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : fragments) {
            if (tag == null || (fragment.getTag() != null && fragment.getTag().equals(tag))) {
                fragmentTransaction.remove(fragment);
                fragmentManager.popBackStack();
            }
        }
        fragmentTransaction.commit();
    }

    private void setNavigationMenuFont() {
        Menu m = binding.navigation.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto_regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public void startExposureNotifications() {
        if(Util.isBluetoothOn()) {
            ExposureNotificationWrapper.get().startApi(this);
        } else {
            restartExposureNotification();
        }
    }

    public void stopExposureNotifications() {
        ExposureNotificationWrapper.get().stopApi(this);
    }

    public void restartExposureNotification() {
        ExposureNotificationWrapper.get().stopApi(new IExposureListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onStopped() {
                ExposureNotificationWrapper.get().startApi(MainActivity.this);
            }

            @Override
            public void onApiException(ApiException apiException, int requestCode) {

            }

            @Override
            public void onExposureKeysRetrieved(List<TemporaryExposureKey> keys) {

            }
        }, false);
    }

    public void checkExposureEnabled() {
        if(isBluetoothOn()) {
            ExposureNotificationWrapper.get().checkEnabled(this);
        } else {
            onStopped();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ExposureNotificationWrapper.REQUEST_CODE_START_EXPOSURE_NOTIFICATION && resultCode == Activity.RESULT_OK) {
            startExposureNotifications();
        } else if (requestCode == ExposureNotificationWrapper.REQUEST_CODE_GET_TEMP_EXPOSURE_KEY_HISTORY && resultCode == Activity.RESULT_OK) {
            startExposureNotifications();
        }
    }

    public void setExposureListener(IExposureStatusChanged exposureListener) {
        this.exposureListener = exposureListener;
    }

    @Override
    public void onStarted() {
        if (exposureListener != null) {
            exposureListener.onExposureStatusChanged(true);
        }
    }

    @Override
    public void onStopped() {
        if (exposureListener != null) {
            exposureListener.onExposureStatusChanged(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(locationReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(bluetoothReceiver);
        unregisterReceiver(locationReceiver);
    }

    @Override
    public void onApiException(ApiException apiException, int requestCode) {
        try {
            apiException.getStatus().startResolutionForResult(this, requestCode);
        } catch (IntentSender.SendIntentException e) {
            Toast.makeText(CroatiaExposureNotificationApp.getInstance(), getString(R.string.internal_api_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onExposureKeysRetrieved(List<TemporaryExposureKey> keys) {

    }

    public interface IExposureStatusChanged {
        void onExposureStatusChanged(boolean exposureEnabled);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LanguageUtil.setLanguageInit(this);
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive (Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    checkExposureEnabled();
                }
            } catch (Exception e) {
                checkExposureEnabled();
            }
        }
    };

    private final BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        public void onReceive (Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(action)) {
                    checkExposureEnabled();
                }
            } catch (Exception e) {
                checkExposureEnabled();
            }
        }
    };
}
