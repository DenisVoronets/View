package com.guide.webview.zaglushka.gameutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;

import com.guide.webview.R;

@SuppressLint("ViewConstructor")
public class GridElement extends AppCompatButton {

    private boolean mIsFlipped = false;
    private boolean mIsFlippable = true;
    private int mFrontDrawable;
    private int mBackDrawable;
    private int mFrontResId = 0;


    public GridElement(Context context, int id){
        super(context);
        setId(id);
        mBackDrawable = R.drawable.cell;
        mFrontDrawable = mFrontResId;
        setBackgroundResource(mBackDrawable);

        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null){
            params.width = 150;
            params.height = 150;
        } else {
            params = new ViewGroup.LayoutParams(150,150);
        }
        setLayoutParams(params);
    }

    public void setId(int id){
        super.setId(id);
        switch (id % 8){
            case 0:
                mFrontResId = R.drawable.joker;
                break;
            case 1:
                mFrontResId = R.drawable.cherry;
                break;
            case 2:
                mFrontResId = R.drawable.ring;
                break;
            case 3:
                mFrontResId = R.drawable.diamond;
                break;
            case 4:
                mFrontResId = R.drawable.start;
                break;
            case 5:
                mFrontResId = R.drawable.bug;
                break;
            case 6:
                mFrontResId = R.drawable.wheel;
                break;
            case 7:
                mFrontResId = R.drawable.book;
                break;
        }

    }

    public int getmFrontResId(){
        return mFrontResId;
    }

    public boolean isFlipped(){
        return mIsFlipped;
    }

    public boolean isFlippable(){
        return mIsFlippable;
    }

    public void setFlippable(boolean flippable){
        mIsFlippable = flippable;
    }

    public void flip(){
        if (!mIsFlippable)
            return;
        if (!mIsFlipped){
            setBackgroundResource(mFrontDrawable);
            mIsFlipped = true;
        } else {
            setBackgroundResource(mBackDrawable);
            mIsFlipped = false;
        }
    }








}

