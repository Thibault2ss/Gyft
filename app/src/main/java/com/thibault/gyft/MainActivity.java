package com.thibault.gyft;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.PorterDuff;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


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
    public View header=null;
    public ImageView menu_icon = null;
    public RelativeLayout menu_container=null;
    public int menu_left_margin=0;
    public Context mcontext=null;
    public Boolean menuOpen=Boolean.FALSE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set context
        mcontext=this;

        //Set Header to right size and transparence
        setHeader();
        header=findViewById(R.id.header);

        // Apporter Fragment Gridview
        //TO DO: FIX BACKSTACK NOT WORKING
        setGridview(this);

        //Set Onclick listener for menu
        menu_icon = (ImageView) header.findViewById(R.id.menu_icon);
        menu_icon.setOnClickListener(menuListener(this));

        //Get original left margin of menu
        menu_container=(RelativeLayout) findViewById(R.id.menu_container);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) menu_container.getLayoutParams();
        menu_left_margin=lp.leftMargin;

    }



    //Set header and adjust it with status bar
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


        gridview_button.setOnClickListener(toggleListener(this));
        storeview_button.setOnClickListener(toggleListener(this));
        gridview_button.performClick();
    }
    //Method to get screen dimension
    public void getWindowDim(){
        //        get height qnd width of screen
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        window_height_px = displaymetrics.heightPixels;
        window_width_px = displaymetrics.widthPixels;
    }
    //bring grid view fragment
    public static void setGridview(Activity activity){
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GridFragment gridFragment = new GridFragment();
        fragmentTransaction.add(R.id.store_container, gridFragment);
        fragmentTransaction.addToBackStack("GridView");
        fragmentTransaction.commit();
    }
    //bring store view fragment
    public static void setStoreview(Activity activity){
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StoreFragment storeFragment = new StoreFragment();
        fragmentTransaction.replace(R.id.store_container, storeFragment);
        fragmentTransaction.addToBackStack("StoreView");
        fragmentTransaction.commit();
    }
    //Set a listener for toggle buttons
    public static View.OnClickListener toggleListener(final Activity activity){
        final View header = activity.findViewById(R.id.header);
        final Button gridview_button = (Button) header.findViewById(R.id.gridview_button);
        final Button storeview_button = (Button) header.findViewById(R.id.storeview_button);
        final ImageView gridview_icon = (ImageView) header.findViewById(R.id.gridview_icon);
        final ImageView storeview_icon = (ImageView) header.findViewById(R.id.storeview_icon);
        View.OnClickListener toggleListener  = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.gridview_button){
                    gridview_button.setSelected(true);
                    gridview_icon.getDrawable().setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.colorPrimary, null), PorterDuff.Mode.MULTIPLY);
                    storeview_button.setSelected(false);
                    storeview_icon.getDrawable().setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.white, null), PorterDuff.Mode.MULTIPLY);
                    activity.getFragmentManager().popBackStack("GridView", 0);
                }
                else {
                    gridview_button.setSelected(false);
                    gridview_icon.getDrawable().setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.white, null), PorterDuff.Mode.MULTIPLY);
                    storeview_button.setSelected(true);
                    storeview_icon.getDrawable().setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.colorPrimary, null), PorterDuff.Mode.MULTIPLY);
                    setStoreview(activity);
                }
            }
        };
        return toggleListener;
    }
    //Handle Slider menu animation
    public  View.OnClickListener menuListener(final Activity activity){
        View.OnClickListener toggleListener  = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (menuOpen) {
                    menu_container.startAnimation(menuDisappear(menu_left_margin,activity));
                    menuOpen=Boolean.FALSE;
                }
                else {
                    menu_container.startAnimation(menuAppear(menu_left_margin,activity));
                    menuOpen=!menuOpen;
                }
            }
        };
        return toggleListener;
    }
    //    Animation menu appear
    private TranslateAnimation menuAppear(final int fromMargin,Activity activity) {
        final View menu_container = activity.findViewById(R.id.menu_container);
        TranslateAnimation animation = new TranslateAnimation(0, -fromMargin, 0, 0);
        animation.setDuration(250);
        animation.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation) {
                // Cancel the animation to stop the menu from popping back.
                menu_container.clearAnimation();
                // Set the new left margin.
                setLeftMargin(menu_container, 0);
            }
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
        });
        return animation;
    }
    //    Animation for menu disappear
    private TranslateAnimation menuDisappear(final int toMargin,Activity activity) {
        final View menu_container = activity.findViewById(R.id.menu_container);
        TranslateAnimation animation = new TranslateAnimation(0, toMargin, 0, 0);
        animation.setDuration(250);
        animation.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation) {
                // Cancel the animation to stop the menu from popping back.
                menu_container.clearAnimation();
                // Set the new left margin.
                setLeftMargin(menu_container, toMargin);
            }
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
        });
        return animation;
    }
    //Method to set bottom margin
    private void setLeftMargin(View view, int bottomMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        layoutParams.leftMargin = bottomMargin;
        view.requestLayout();
    }
    //Method to convert dip to pixel-not used for now
    private int dipsToPixels(int dips) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(dips * scale + 0.5f);
    }


}
