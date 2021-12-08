package com.guide.webview.zaglushka.activities;

import android.app.Activity;
import android.os.Bundle;

import com.guide.webview.R;
import com.guide.webview.zaglushka.menuutils.Adapter;
import com.guide.webview.zaglushka.menuutils.RecyclerCoverFlow;


public class GameStartMenuActivity extends Activity implements Adapter.onItemClick {

    private RecyclerCoverFlow mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start_menu);
        initList();
    }

    private void initList() {
        mList = findViewById(R.id.list);
        mList.set3DItem(true);
        mList.setLoop();
        mList.setAdapter(new Adapter(this, this, true,this));
    }

    @Override
    public void clickItem(int pos) {
        mList.smoothScrollToPosition(pos);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}