package com.guide.webview.zaglushka.gameutils;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.guide.webview.R;
import com.guide.webview.zaglushka.activities.GameFieldActivity;
import com.guide.webview.zaglushka.activities.GameStartMenuActivity;

public class MyPopUp {
    private GameFieldActivity gameScreen;

    public MyPopUp(GameFieldActivity gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void showPopupWindow(View view) {

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.fragment_popup, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView textGameEnd = popupView.findViewById(R.id.textView_game_end);
        textGameEnd.setText("You win!!!");
        Button buttonAgain = popupView.findViewById(R.id.button_play_again);
        Button butoonExit = popupView.findViewById(R.id.button_exit_game);
        buttonAgain.setOnClickListener(v -> {
            gameScreen.startGame();
            popupWindow.dismiss();
        });
        butoonExit.setOnClickListener(view1 -> {
            gameScreen.startActivity(new Intent(gameScreen, GameStartMenuActivity.class));
            gameScreen.finish();
        });
    }
}
