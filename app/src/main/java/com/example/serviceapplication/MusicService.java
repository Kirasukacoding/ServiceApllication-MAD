package com.example.serviceapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

public class MusicService extends Service {

    public static final String ACTION_PLAY = "com.example.serviceapplication.ACTION_PLAY";
    public static final String ACTION_STOP = "com.example.serviceapplication.ACTION_STOP";
    public static final String EXTRA_TRACK_RES_ID = "EXTRA_TRACK_RES_ID";
    public static final String EXTRA_TRACK_NAME = "EXTRA_TRACK_NAME";

    public static boolean isRunning = false;
    public static String currentTrackName = "None";
    public static int currentTrackResId = R.raw.music_track1;

    private MediaPlayer player;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();

            if (ACTION_STOP.equals(action)) {
                stopSelf();
                return START_NOT_STICKY;
            }

            int trackResId = intent.getIntExtra(EXTRA_TRACK_RES_ID, R.raw.music_track1);
            String trackName = intent.getStringExtra(EXTRA_TRACK_NAME);
            if (trackName == null || trackName.isEmpty()) {
                trackName = "Track 1";
            }

            ensureDeviceVolume();
            releasePlayer();

            try {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build();

                if (trackResId == -1) {
                    Uri alarmUri = Settings.System.DEFAULT_RINGTONE_URI;
                    player = MediaPlayer.create(this, alarmUri);
                } else {
                    player = MediaPlayer.create(this, trackResId);
                }

                if (player != null) {
                    player.setVolume(1.0f, 1.0f);
                    player.setLooping(true);
                    player.setOnErrorListener((mp, what, extra) -> {
                        Toast.makeText(MusicService.this, "Audio error (" + what + ", " + extra + ")", Toast.LENGTH_SHORT).show();
                        return false;
                    });
                    player.start();

                    isRunning = true;
                    currentTrackName = trackName;
                    currentTrackResId = trackResId;

                    Toast.makeText(this, "Playing: " + trackName, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to load audio", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return START_STICKY;
    }

    private void ensureDeviceVolume() {
        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currentVol < (maxVol * 0.3)) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (maxVol * 0.7), 0);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void releasePlayer() {
        if (player != null) {
            try {
                if (player.isPlaying()) {
                    player.stop();
                }
            } catch (Exception ignored) {
            }
            try {
                player.release();
            } catch (Exception ignored) {
            }
            player = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();

        isRunning = false;
        currentTrackName = "None";

        releasePlayer();
    }
}
