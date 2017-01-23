package com.thibault.gyft;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Thibault on 1/2/2017.
 */

public class MenuFragment extends Fragment {
    int BONUS_LEVEL;
    int LEVEL_MAX;
    ProgressBar level_bar;
    ImageView bonus_icon;
    View fragmentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView=inflater.inflate(R.layout.menu, container, false);
        View around_me=fragmentView.findViewById(R.id.around_me);
        View map=fragmentView.findViewById(R.id.map);
        View gifts=fragmentView.findViewById(R.id.gifts);
        View trades=fragmentView.findViewById(R.id.trades);

        // set State Listener for menu items
        around_me.setOnTouchListener(changeColor());
        map.setOnTouchListener(changeColor());
        gifts.setOnTouchListener(changeColor());
        trades.setOnTouchListener(changeColor());


        //set Progress Bar and bonus icon
        setLevelBar();


        return fragmentView;
    }

    //OnTouch listener for menu items
    public View.OnTouchListener changeColor(){
        View.OnTouchListener changeColor=new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    change_to_active(v);
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    change_to_passive(v);
                }
                return true;
            }
        };
        return changeColor;
    }
    //Method to set Menu Item color to Active
    public void change_to_active(View v){
        LinearLayout menu_item=(LinearLayout) v;
        final int childcount = menu_item.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View vv = menu_item.getChildAt(i);
            if(vv instanceof ImageView){
                ImageView imageView=(ImageView) vv;
                imageView.setColorFilter(ContextCompat.getColor(getActivity(),R.color.text_higlight));
            }
            else if (vv instanceof TextView){
                TextView textView=(TextView)vv;
                textView.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.text_higlight, null));
            }
        }
    }
    //Method to set Menu Item color to Passive
    public void change_to_passive(View v){
        LinearLayout menu_item=(LinearLayout) v;
        final int childcount = menu_item.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View vv = menu_item.getChildAt(i);
            if(vv instanceof ImageView){
                ImageView imageView=(ImageView) vv;
                imageView.setColorFilter(ContextCompat.getColor(getActivity(),R.color.menu_item_color));
            }
            else if (vv instanceof TextView){
                TextView textView=(TextView)vv;
                textView.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.menu_item_color, null));
            }
        }
    }
    //level Bar animation
    public static class LevelBarAnimation extends Animation{
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public LevelBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }
    //Set level Bar
    public void setLevelBar(){
        //TODO: get BONUSLEVEL and LEVELMAX from user
        level_bar=(ProgressBar) fragmentView.findViewById(R.id.level_bar);
        bonus_icon=(ImageView)fragmentView.findViewById(R.id.bonus_icon);
        BONUS_LEVEL=1;
        LEVEL_MAX=50;
        int colorLevelId = getResources().getIdentifier("color_level_"+String.valueOf(BONUS_LEVEL), "color", getActivity().getPackageName());

        level_bar.setMax(LEVEL_MAX);
        level_bar.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.level_bar));
        level_bar.setProgress(25);
        //Color of progress Bar
        LayerDrawable layerDrawable = (LayerDrawable) level_bar.getProgressDrawable();
        ClipDrawable progress_bar=(ClipDrawable) layerDrawable.findDrawableByLayerId(R.id.progress);
        progress_bar.setColorFilter(ContextCompat.getColor(getActivity(),colorLevelId),android.graphics.PorterDuff.Mode.MULTIPLY);
        //Color of button
        bonus_icon.setColorFilter(ContextCompat.getColor(getActivity(),colorLevelId));
    }


}

