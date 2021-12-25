package com.ad.app.notify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ad.app.notify.service.NotificationService;
import com.ad.app.notify.utils.Constants;
import com.ad.app.notify.utils.TextProcessor;
import com.ad.app.notify.utils.Utils;

public class TextReceiverActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CharSequence message = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);


        if (Intent.ACTION_SEND.equals(getIntent().getAction()) && "text/plain".equals(getIntent().getType())) {

            new TextProcessor(this).process(getIntent().getStringExtra(Intent.EXTRA_TEXT),Constants.ACTION_ADD);

//            Intent intent = new Intent(this, NotificationService.class);

//            intent.putExtra(Constants.NOTIFICATION_ID, new Utils().getNotificationId());
//            intent.putExtra(Constants.NOTIFICATION_SUB_TEXT, getIntent().getStringExtra(Intent.EXTRA_TEXT));
//            startService(intent);

        } else if (message != null) {

            new TextProcessor(this).process(message.toString(),Constants.ACTION_ADD);

//            Intent intent = new Intent(this, NotificationService.class);

//            intent.putExtra(Constants.NOTIFICATION_ID, new Utils().getNotificationId());
//            intent.putExtra(Constants.NOTIFICATION_SUB_TEXT, message.toString());
//            startService(intent);
        } else {
            Toast.makeText(this, "Exception Found", Toast.LENGTH_SHORT).show();
        }


        finish();
    }

}