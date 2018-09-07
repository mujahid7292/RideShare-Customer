package com.sand_corporation.www.uthao.FirebaseMessaging;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sand_corporation.www.uthao.R;

public class NotificationMsgActivity extends AppCompatActivity {

    //Please make change in manifest for on_click attribute

    private TextView txtNotificationTitle, txtNotificationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_msg);

        String title = getIntent().getStringExtra("title")==null ? "N/A":getIntent().getStringExtra("title");
        String message = getIntent().getStringExtra("message")==null ? "N/A":getIntent().getStringExtra("message");

        txtNotificationTitle = findViewById(R.id.txtNotificationTitle);
        txtNotificationMessage = findViewById(R.id.txtNotificationMessage);

        txtNotificationTitle.setText(title);
        txtNotificationMessage.setText(message);
    }
}
