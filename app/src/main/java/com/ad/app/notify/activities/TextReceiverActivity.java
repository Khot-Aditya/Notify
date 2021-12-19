package com.ad.app.notify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ad.app.notify.service.NotificationService;
import com.ad.app.notify.utils.Utils;

public class TextReceiverActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CharSequence message = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);


        if (Intent.ACTION_SEND.equals(getIntent().getAction()) && "text/plain".equals(getIntent().getType())) {

            Intent intent = new Intent(this, NotificationService.class);

            intent.putExtra("notification_id", new Utils().getNotificationId());
            intent.putExtra("notification_body", getIntent().getStringExtra(Intent.EXTRA_TEXT));
            startService(intent);

        } else if (message != null) {

            Intent intent = new Intent(this, NotificationService.class);

            intent.putExtra("notification_id", new Utils().getNotificationId());
            intent.putExtra("notification_body", message.toString());
            startService(intent);
        } else {
            Toast.makeText(this, "Exception Found", Toast.LENGTH_SHORT).show();
        }


        finish();
    }

}