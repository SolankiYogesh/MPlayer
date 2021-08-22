package com.syinfotech.mplayer.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.syinfotech.mplayer.R;

public class SphlashSchreen extends AppCompatActivity {
    public static final int REQUEST_CODE_FOR_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sphlash_schreen);
        checkStoragePermission();
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //asking permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_FOR_PERMISSION);
        } else {
            nextActivity();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_FOR_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                nextActivity();
            } else {
                //asking permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_FOR_PERMISSION);
            }
        }
    }

    private void nextActivity() {
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SphlashSchreen.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }, 3000);
    }
}
