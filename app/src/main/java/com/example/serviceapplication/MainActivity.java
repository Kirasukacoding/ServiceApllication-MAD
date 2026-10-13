package com.example.serviceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnStart, btnStop;
    private Button btnNavTrackSelector, btnNavAbout;
    private TextView tvServiceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnNavTrackSelector = findViewById(R.id.btnNavTrackSelector);
        btnNavAbout = findViewById(R.id.btnNavAbout);
        tvServiceStatus = findViewById(R.id.tvServiceStatus);

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MusicService.class);
            intent.putExtra(MusicService.EXTRA_TRACK_RES_ID, MusicService.currentTrackResId);
            intent.putExtra(MusicService.EXTRA_TRACK_NAME, MusicService.currentTrackName.equals("None") ? "Track 1" : MusicService.currentTrackName);
            startService(intent);
            updateStatus();
        });

        btnStop.setOnClickListener(v -> {
            stopService(new Intent(MainActivity.this, MusicService.class));
            updateStatus();
        });

        btnNavTrackSelector.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TrackSelectorActivity.class);
            startActivity(intent);
        });

        btnNavAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    private void updateStatus() {
        if (MusicService.isRunning) {
            tvServiceStatus.setText("● Service Running");
            tvServiceStatus.setTextColor(ContextCompat.getColor(this, R.color.accent_green));
        } else {
            tvServiceStatus.setText("● Service Stopped");
            tvServiceStatus.setTextColor(ContextCompat.getColor(this, R.color.accent_red));
        }
    }
}
