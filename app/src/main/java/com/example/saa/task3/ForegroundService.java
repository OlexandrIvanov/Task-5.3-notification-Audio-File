package com.example.saa.task3;


import android.app.PendingIntent;
import android.app.Service;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import android.util.Log;



public class ForegroundService extends Service {

    int musicInArray=0;


    PendingIntent pplayIntent;

    MediaPlayer mediaPlayer;
    Bitmap icon;


    int[] allAudio = {R.raw.qwerty, R.raw.audio1, R.raw.audio2, R.raw.audio3,
            R.raw.audio4};


    private static final String LOG_TAG = "Log";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationCompat.Builder notification;


        if (intent.getAction().equals("Start")) {
            Log.i(LOG_TAG, "Start Foreground Intent ");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction("ACTION.MAIN_ACTION");
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Intent playIntent = new Intent(this, ForegroundService.class);
            playIntent.setAction("ACTION.PLAY_ACTION");
            pplayIntent = PendingIntent.getService(this, 0,
                    playIntent, 0);


            Intent previousAudioIntent = new Intent(this, ForegroundService.class);
            previousAudioIntent.setAction("ACTION.PREVIOUS");
            PendingIntent pPreviouIntent = PendingIntent.getService(this, 0,
                    previousAudioIntent, 0);


            Intent nextAudioIntent = new Intent(this, ForegroundService.class);
            nextAudioIntent.setAction("ACTION.NEXT");
            PendingIntent pNextAudioIntent = PendingIntent.getService(this, 0,
                    nextAudioIntent, 0);

            icon = BitmapFactory.decodeResource(getResources(), R.drawable.music);


            notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Music Player")
                    .setTicker("Music Player")
                    .setContentText("My Music")
                    .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off)
                    .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    //.setOngoing(true)
                    .addAction(android.R.drawable.ic_media_previous, null, pPreviouIntent)
                    .addAction(android.R.drawable.ic_media_pause, null, pplayIntent)
                    .addAction(android.R.drawable.ic_media_next, null, pNextAudioIntent);

            startForeground(8, notification.build());
            mediaPlayer = MediaPlayer.create(this, allAudio[musicInArray]);
            mediaPlayer.start();

        }
        switch (intent.getAction()) {
            case "ACTION.PLAY_ACTION":
                Log.d("Log", "ACTION.PLAY_ACTION");
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    stopForeground(false);
                } else {
                    mediaPlayer.start();


                }
                break;

            case "ACTION.PREVIOUS":


                if (musicInArray ==0) {
                    musicInArray = 4;
                } else {
                    musicInArray = musicInArray-1;
                }
                mediaPlayer.release();
                mediaPlayer = null;
                mediaPlayer = MediaPlayer.create(this, allAudio[musicInArray]);
                mediaPlayer.start();
                Log.d("Log", "ACTION.PREVIOUS " + musicInArray);
                break;
            case "ACTION.NEXT":


                if (musicInArray < 4) {
                    musicInArray = musicInArray+1;
                } else {

                    musicInArray=0;
                }
                mediaPlayer.release();
                mediaPlayer = null;
                mediaPlayer = MediaPlayer.create(this, allAudio[musicInArray]);
                mediaPlayer.start();

                break;

        }
        if (intent.getAction().equals("Stop")) {
            mediaPlayer.release();
            mediaPlayer = null;
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }



}