package com.guide.webview.zaglushka.menuutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.guide.webview.R;
import com.guide.webview.zaglushka.activities.GameAboutActivity;
import com.guide.webview.zaglushka.activities.GamePrivacyPolicyActivity;
import com.guide.webview.zaglushka.activities.GameUserNameActivity;
import com.bumptech.glide.Glide;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context mContext;
    private Activity activity;
    private int[] mColors = {R.drawable.button_play,R.drawable.button_about,R.drawable.button_privacy,R.drawable.button_exit};

    private onItemClick clickCb;
    private boolean is3D;

    public Adapter(Context c, boolean is3D) {
        mContext = c;
        this.is3D = is3D;
    }

    public Adapter(Context c, onItemClick cb, boolean is3D, Activity activity) {
        mContext = c;
        this.activity = activity;
        clickCb = cb;
        this.is3D = is3D;
    }

    public void setOnClickLstn(onItemClick cb) {
        this.clickCb = cb;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.layout_item;
        if (is3D) layout = R.layout.layout_item_mirror;
        View v = LayoutInflater.from(mContext).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(mContext).load(mColors[position]).into(holder.img);
        holder.itemView.setOnClickListener(v -> {
            switch (mColors[position]){
                case R.drawable.button_play:
                    activity.startActivity(new Intent(activity, GameUserNameActivity.class));
                    activity.finish();
                    break;
                case R.drawable.button_privacy:
                    activity.startActivity(new Intent(activity, GamePrivacyPolicyActivity.class));
                    activity.finish();
                    break;
                case R.drawable.button_about:
                    activity.startActivity(new Intent(activity, GameAboutActivity.class));
                    activity.finish();
                    break;
                case R.drawable.button_exit:
                    activity.finish();
                    break;
            }
            if (clickCb != null) {
                clickCb.clickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mColors.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

    public interface onItemClick {
        void clickItem(int pos);
    }
}
