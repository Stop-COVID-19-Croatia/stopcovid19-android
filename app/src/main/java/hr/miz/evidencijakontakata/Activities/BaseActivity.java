package hr.miz.evidencijakontakata.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hr.miz.evidencijakontakata.Utilities.LanguageUtil;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.setLanguage(this,LanguageUtil.getLanguage());
    }

    public void startNewActivity(Class<?> activityClass, Bundle extras, boolean addToBackStack) {
        Intent intent = new Intent(this, activityClass);
        if(!addToBackStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        if(extras != null) {
            intent.putExtras(extras);
        }

        startActivity(intent);
        if(!addToBackStack) {
            finish();
        }
    }

    public void startWebView(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
