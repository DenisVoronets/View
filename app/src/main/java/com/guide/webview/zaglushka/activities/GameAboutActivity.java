package com.guide.webview.zaglushka.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.guide.webview.R;

public class GameAboutActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_about);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GameStartMenuActivity.class));
        finish();
    }
}
