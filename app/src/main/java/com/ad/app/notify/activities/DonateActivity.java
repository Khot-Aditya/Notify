package com.ad.app.notify.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ad.app.notify.R;
import com.ad.app.notify.utils.Utils;

public class DonateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        new Utils(this).log("onCreate");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        new Utils(this).log("onBackPressed");
        overridePendingTransition(R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
    }
}