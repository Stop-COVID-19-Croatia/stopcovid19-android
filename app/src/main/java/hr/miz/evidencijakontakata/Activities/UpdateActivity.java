package hr.miz.evidencijakontakata.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.Utilities.LanguageUtil;
import hr.miz.evidencijakontakata.databinding.ActivityUpdateBinding;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class UpdateActivity extends AppCompatActivity {
    private ActivityUpdateBinding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvUpgrade.setOnClickListener(this::openGooglePlay);
        if (LanguageUtil.getImageLanguage().equals("hr")) {
            binding.ivLogo.setImageDrawable(getDrawable(R.drawable.ministvarstvo_logo_hr));
        } else {
            binding.ivLogo.setImageDrawable(getDrawable(R.drawable.ministvarstvo_logo_en));
        }
    }

    public void openGooglePlay(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setData(Uri.parse(getString(R.string.google_play_link)));
        startActivity(intent);
        finish();
    }
}
