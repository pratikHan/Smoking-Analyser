package com.example.pratikhanchate.heartrate;
import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;


public class NotifActivity extends WearableActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notif);
    }
}
