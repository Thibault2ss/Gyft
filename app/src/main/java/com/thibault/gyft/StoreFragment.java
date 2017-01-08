package com.thibault.gyft;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Thibault on 12/29/2016.
 */

public class StoreFragment extends Fragment {

    private int _yDelta;
    private int lastY;
    private int i=0;
    int floatingHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView=inflater.inflate(R.layout.store_view, container, false);
        //Pass info from the square item to the store view
        //TO DO: get info from the transaction manager
        final ImageView picture= (ImageView) myInflatedView.findViewById(R.id.picture);
        final RelativeLayout sumupBar = (RelativeLayout) myInflatedView.findViewById(R.id.sumup_bar);
        TextView storename=(TextView) myInflatedView.findViewById(R.id.store_name);
        TextView distance=(TextView) myInflatedView.findViewById(R.id.distance);
        ImageView gift=(ImageView)myInflatedView.findViewById(R.id.gift);
        final View floating_button=myInflatedView.findViewById(R.id.floating_button);

        //Set position of some stuff
        setPositions(myInflatedView,floating_button,sumupBar);
        //Fill info in fragment
        picture.setImageResource(R.drawable.cupcakes);
        storename.setText("Kite-Hill");
        distance.setText("4 miles");
        gift.setImageResource(R.drawable.gift_100);
        //Set on touch listener of footer
        sumupBar.setOnTouchListener(sumupbarTouchListener());
        return myInflatedView;
    }

    //Handles the scroll of the sumupbar
    public View.OnTouchListener sumupbarTouchListener(){
        View.OnTouchListener sumupbarTouchListener=new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                i++;
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        _yDelta = Y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        Magnet(v,Y,lastY,200);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        layoutParams.topMargin = Y - _yDelta;
                        v.setLayoutParams(layoutParams);
                        if (i>6){
                            lastY=Y;
                            i=0;
                        }

                        break;
                }
                v.invalidate();
                return true; // <-- this line made the difference
            }
        };
        return sumupbarTouchListener;
    }

    //sumupbar magnet animation (either attracted to top either bottom)
    public void Magnet(final View v, int FingerPosition,int PreviousFingerPosition,int duration){
        final RelativeLayout.LayoutParams layoutparam = (RelativeLayout.LayoutParams) v.getLayoutParams();
        final int DeltaY;
        if(FingerPosition<=PreviousFingerPosition){
            DeltaY = -layoutparam.topMargin;
        }
        else{
            DeltaY = MainActivity.screen_width_px-layoutparam.topMargin;
        }
        TranslateAnimation anim = new TranslateAnimation( 0, 0 , 0, DeltaY );
        anim.setDuration(duration);
        anim.setFillAfter( true );
        v.startAnimation(anim);
        //Reset margin at the end of animation
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                v.clearAnimation();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                layoutParams.topMargin = layoutParams.topMargin+DeltaY;
                v.setLayoutParams(layoutParams);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    //Set positions of some elements in that view
    public void setPositions(View inflatedView,final View floatingButton,final View sumupBar){
        //Set Sumup bar below image at startup
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) sumupBar.getLayoutParams();
        layoutParams.topMargin=MainActivity.screen_width_px;
        sumupBar.setLayoutParams(layoutParams);
        //Set floating button position
        inflatedView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                floatingHeight = floatingButton.getHeight();
                int[] location=new int[2];
                sumupBar.getLocationInWindow(location);
                if(floatingHeight > 0 && location[1]>0)
                {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) floatingButton.getLayoutParams();
                    lp.topMargin=location[1]-(floatingHeight/2)-MainActivity.header_height-MainActivity.statusBarHeight;
                    floatingButton.setLayoutParams(lp);
                    getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }



}
