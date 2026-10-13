package com.example.serviceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AboutActivity extends AppCompatActivity {

    private TextView tvAboutServiceStatus;
    private Button btnAboutStopService, btnAboutBackMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvAboutServiceStatus = findViewById(R.id.tvAboutServiceStatus);
        btnAboutStopService = findViewById(R.id.btnAboutStopService);
        btnAboutBackMain = findViewById(R.id.btnAboutBackMain);

        btnAboutStopService.setOnClickListener(v -> {
            stopService(new Intent(AboutActivity.this, MusicService.class));
            updateUI();
        });

        btnAboutBackMain.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (MusicService.isRunning) {
            tvAboutServiceStatus.setText("● Service Running");
            tvAboutServiceStatus.setTextColor(ContextCompat.getColor(this, R.color.accent_green));
            btnAboutStopService.setEnabled(true);
        } else {
            tvAboutServiceStatus.setText("● Service Stopped");
            tvAboutServiceStatus.setTextColor(ContextCompat.getColor(this, R.color.accent_red));
            btnAboutStopService.setEnabled(false);
        }
    }
}
