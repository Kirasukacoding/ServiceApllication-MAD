package com.example.serviceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class TrackSelectorActivity extends AppCompatActivity {

    private RadioGroup radioGroupTracks;
    private RadioButton rbTrack1, rbTrack2, rbTrack3;
    private Button btnPlaySelected, btnStopTrack, btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_selector);

        radioGroupTracks = findViewById(R.id.radioGroupTracks);
        rbTrack1 = findViewById(R.id.rbTrack1);
        rbTrack2 = findViewById(R.id.rbTrack2);
        rbTrack3 = findViewById(R.id.rbTrack3);
        btnPlaySelected = findViewById(R.id.btnPlaySelected);
        btnStopTrack = findViewById(R.id.btnStopTrack);
        btnBackHome = findViewById(R.id.btnBackHome);

        btnPlaySelected.setOnClickListener(v -> playSelectedTrack());

        btnStopTrack.setOnClickListener(v -> {
            stopService(new Intent(TrackSelectorActivity.this, MusicService.class));
        });

        btnBackHome.setOnClickListener(v -> finish());
    }

    private void playSelectedTrack() {
        int selectedResId = R.raw.music_track1;
        String selectedName = "Track 1";

        if (rbTrack2.isChecked()) {
            selectedResId = R.raw.music_track2;
            selectedName = "Track 2";
        } else if (rbTrack3.isChecked()) {
            selectedResId = R.raw.music_track3;
            selectedName = "Track 3";
        }

        Intent intent = new Intent(TrackSelectorActivity.this, MusicService.class);
        intent.putExtra(MusicService.EXTRA_TRACK_RES_ID, selectedResId);
        intent.putExtra(MusicService.EXTRA_TRACK_NAME, selectedName);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MusicService.isRunning) {
            if (MusicService.currentTrackResId == R.raw.music_track2) {
                rbTrack2.setChecked(true);
            } else if (MusicService.currentTrackResId == R.raw.music_track3) {
                rbTrack3.setChecked(true);
            } else {
                rbTrack1.setChecked(true);
            }
        }
    }
}
