package com.thibault.gyft;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;


public class MainActivity extends Activity {

    public int getStatusBarHeight() {
        int status_bar_height = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            status_bar_height = getResources().getDimensionPixelSize(resourceId);
        }
        return status_bar_height;
    }
    public static int window_height_px=0;
    public static int window_width_px=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Header to right size and transparence
        setHeader();

        // Apporter Fragment Gridview
        //TO DO: FIX BACKSTACK NOT WORKING
        setGridview();

    }

    public void setHeader(){
        final View header = findViewById(R.id.header);
        // if api level sufficient, set status bar transparent, get header height and add status bar height to it plus padding
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            header.setPadding(0,getStatusBarHeight(),0,0);
            Log.v("testAAAA",String.valueOf(getStatusBarHeight()));
            header.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    header.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    final int header_height=header.getHeight();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,header_height+getStatusBarHeight());
                    header.setLayoutParams(params);
                }
            });
        }
        final Button gridview_button = (Button) header.findViewById(R.id.gridview_button);
        final Button storeview_button = (Button) header.findViewById(R.id.storeview_button);
        View.OnClickListener toggleListener  = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.gridview_button){
                    gridview_button.setSelected(true);
                    storeview_button.setSelected(false);
                }
                else{
                    gridview_button.setSelected(false);
                    storeview_button.setSelected(true);
                }
            }
        };
        gridview_button.setOnClickListener(toggleListener);
        storeview_button.setOnClickListener(toggleListener);
        gridview_button.performClick();
    }

    public void getWindowDim(){
        //        get height qnd width of screen
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        window_height_px = displaymetrics.heightPixels;
        window_width_px = displaymetrics.widthPixels;
    }

    public void setGridview(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GridFragment gridFragment = new GridFragment();
        fragmentTransaction.add(R.id.store_container, gridFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
