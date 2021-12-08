package com.guide.webview.zaglushka.activities;


import static com.guide.webview.zaglushka.gameutils.Constants.NAME;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.guide.webview.R;

public class GameUserNameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText mainUserNameText = findViewById(R.id.nameEditText);
        final Button playButton = findViewById(R.id.playButton);


        playButton.setOnClickListener(view -> {
            if (mainUserNameText.getText().length() > 0){
                Intent intent = new Intent(GameUserNameActivity.this, GameFieldActivity.class);
                intent.putExtra(NAME, mainUserNameText.getText().toString());
                startActivity(intent);
            } else {
                Toast.makeText(this,"Field NickName is empty",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GameStartMenuActivity.class));
        finish();
    }
}
