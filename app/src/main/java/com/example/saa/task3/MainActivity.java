package com.example.saa.task3;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private final String serviceName = "com.example.saa.task3.ForegroundService";

    Button buttonStart, buttonStop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Log"," On Create");


        buttonStart = (Button) findViewById(R.id.btnStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
                startIntent.setAction("Start");
                startService(startIntent);
                buttonStop.setEnabled(true);
                buttonStart.setEnabled(false);

            }
        });


        buttonStop = (Button) findViewById(R.id.btnStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent stopIntent = new Intent(MainActivity.this, ForegroundService.class);
                    stopIntent.setAction("Stop");
                    startService(stopIntent);
                    buttonStop.setEnabled(false);
                    buttonStart.setEnabled(true);

            }
        });

        if (ifServiceOn()){
            buttonStart.setEnabled(false);

        }else buttonStop.setEnabled(false);

    }

    public boolean  ifServiceOn(){
        ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo: activityManager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceName.equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }


}
