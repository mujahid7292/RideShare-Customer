package com.sand_corporation.www.uthao.FirebaseMessaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.sand_corporation.www.uthao.R;

public class NotificationWebViewActivity extends AppCompatActivity {

    //Please make change in manifest for on_click attribute

    private WebView fcmWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_web_view);

        //Configure Firebase Messaging WebView
        fcmWebView = findViewById(R.id.fcmWebView);
        fcmWebView.getSettings().setJavaScriptEnabled(true);
        fcmWebView.setWebChromeClient(new WebChromeClient());

        onNewIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {

        if (intent.getStringExtra("webUrl") != null){
            fcmWebView.loadUrl(intent.getStringExtra("webUrl"));
        } else {
            Log.i("Token","FCM_WEB_URL == null");
        }

    }

}
