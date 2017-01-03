package com.thibault.gyft;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Thibault on 1/2/2017.
 */

public class MenuFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView=inflater.inflate(R.layout.menu, container, false);
        View around_me=myInflatedView.findViewById(R.id.around_me);
        View map=myInflatedView.findViewById(R.id.map);
        View gifts=myInflatedView.findViewById(R.id.gifts);
        View trades=myInflatedView.findViewById(R.id.trades);

        // set State Listener for menu items
        around_me.setOnTouchListener(changeColor());
        map.setOnTouchListener(changeColor());
        gifts.setOnTouchListener(changeColor());
        trades.setOnTouchListener(changeColor());
        return myInflatedView;
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
                imageView.setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
            }
            else if (vv instanceof TextView){
                TextView textView=(TextView)vv;
                textView.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.colorPrimary, null));
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
}

