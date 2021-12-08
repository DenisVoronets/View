package com.guide.webview.zaglushka.activities;


import static com.guide.webview.zaglushka.gameutils.Constants.NAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.guide.webview.R;
import com.guide.webview.zaglushka.gameutils.GridElement;
import com.guide.webview.zaglushka.gameutils.MyPopUp;

public class GameFieldActivity extends Activity implements View.OnClickListener {

    private TextView scoreText;
    private TextView userName;
    private String mUserNameText;
    private GridLayout gameCardsGridLayout;
    private Context context = GameFieldActivity.this;
    private int mScore = 0;
    private int mLastCard = 0;
    private int mFail = 0;
    private MyPopUp myPopUp;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        gameCardsGridLayout = findViewById(R.id.gameCardsGridLayout);
        constraintLayout = findViewById(R.id.constraintlayoout_game);
        userName = findViewById(R.id.userName);
        scoreText = findViewById(R.id.scoreText);
        mUserNameText = getIntent().getStringExtra(NAME);
        userName.setText("Player: " + mUserNameText);
        startGame();


    }

    public void startGame() {
        scoreText.setText("Score: " + mScore);
        GridElement[] allCards = new GridElement[16];
        gameCardsGridLayout.removeAllViews();
        for (int j = 1; j <= 16; j++) {
            allCards[j - 1] = new GridElement(context, j);
            allCards[j - 1].setOnClickListener(this);
        }
        for (int j = 0; j < 16; j++) {
            int random = (int) (Math.random() * 16);
            GridElement randomCard = allCards[random];
            allCards[random] = allCards[j];
            allCards[j] = randomCard;
        }
        for (int j = 0; j < 16; j++) {
            gameCardsGridLayout.addView(allCards[j]);
            gameCardsGridLayout.setColumnCount(4);
            gameCardsGridLayout.setRowCount(4);
        }
    }

    @Override
    public void onClick(View view) {
        final GridElement selectedCard = (GridElement) view;
        selectedCard.flip();
        if (mLastCard > 0) {
            final GridElement card2 = findViewById(mLastCard);
            if (card2.getmFrontResId() == selectedCard.getmFrontResId() && card2.getId() != selectedCard.getId()) {
                Button firsImageView = findViewById(card2.getId());
                firsImageView.setClickable(false);
                Button secondImageView = findViewById(selectedCard.getId());
                secondImageView.setClickable(false);
                card2.setFlippable(false);
                selectedCard.setFlippable(false);
                mScore++;
                scoreText.setText("Score: " + mScore);
                Toast.makeText(context, "Correct", Toast.LENGTH_SHORT).show();

                if (mScore % 8 == 0) {
                    myPopUp = new MyPopUp(this);
                    myPopUp.showPopupWindow(constraintLayout);
                }
            } else {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    card2.flip();
                    selectedCard.flip();
                }, 500);
                mFail++;
            }
            mLastCard = 0;
        } else {
            mLastCard = selectedCard.getId();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, GameUserNameActivity.class));
        finish();
    }
}
