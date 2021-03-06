package com.ad.app.notify.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ad.app.notify.BuildConfig;
import com.ad.app.notify.R;
import com.ad.app.notify.utils.Utils;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        new Utils(this).log("onCreate");

        TextInputEditText edt_FeedbackSubject = (TextInputEditText) findViewById(R.id.edt_FeedbackSubject);
        TextInputEditText edt_FeedbackBody = (TextInputEditText) findViewById(R.id.edt_FeedbackBody);
        TextView txt_DeviceDetails = (TextView) findViewById(R.id.txt_DeviceDetails);
        MaterialCheckBox checkBox_DeviceDetails = (MaterialCheckBox) findViewById(R.id.checkBox_DeviceDetails);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_Feedback);

        String release = Build.VERSION.RELEASE;
        String device = Build.DEVICE;
        String model = Build.MODEL;
        String brand = Build.BRAND;
        String api = String.valueOf(Build.VERSION.SDK_INT);

        String deviceDetails =

                "Brand: " + brand + "\n" +
                        "OS Version: " + release + "\n" +
                        "Api level: " + api + "\n" +
                        "Device: " + device + "\n" +
                        "Model: " + model;

        edt_FeedbackSubject.setText(
                getIntent().hasExtra("subject") ?
                        getIntent().getStringExtra("subject") : "Feedback");


        txt_DeviceDetails.setText(deviceDetails);


        checkBox_DeviceDetails.setOnCheckedChangeListener((buttonView, isChecked) -> {
            txt_DeviceDetails.setAlpha(0.5f);
            if (isChecked) {
                txt_DeviceDetails.setAlpha(1f);
            }

        });

        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_SubmitFeedback) {
                if (Objects.equals(Objects.requireNonNull(edt_FeedbackSubject.getText()).toString(), "")) {
                    edt_FeedbackSubject.setError("Empty Field");
                    return false;
                }

                if (Objects.equals(Objects.requireNonNull(edt_FeedbackBody.getText()).toString(), "")) {
                    edt_FeedbackBody.setError("Empty Field");
                    return false;
                }


                String to = "adityakhot6838@gmail.com";
                String subject =
                        Objects.requireNonNull(edt_FeedbackSubject.getText()).toString() +
                                " [com.ad.app.notify] " +
                                BuildConfig.VERSION_NAME;

                String body = Objects.requireNonNull(edt_FeedbackBody.getText()).toString();

                if (checkBox_DeviceDetails.isChecked()) {
                    body = Objects.requireNonNull(edt_FeedbackBody.getText()).toString() + "\n\n\n\n\n\n\n\n" +
                            "??? Following details will help in development." + "\n" +
                            deviceDetails;
                }


                String mailTo = "mailto:" + to +
                        "?&subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(body);

                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                emailIntent.setData(Uri.parse(mailTo));

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

            return true;
        });
        toolbar.setNavigationOnClickListener(v -> {

            new Utils(this).log("onBackPressed");

            finish();
            overridePendingTransition(R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        new Utils(this).log("onBackPressed");

        overridePendingTransition(R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
    }

}