package com.thibault.gyft;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;


public class MainActivity extends Activity {
    public int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION=1;
    public static int screen_height_px;
    public static int screen_width_px;
    public static int statusBarHeight;
    public static int NavigationBarHeight;
    public static int header_height;
    public static View header=null;
    public ImageView menu_icon = null;
    public static RelativeLayout menu_container=null;
    public RelativeLayout dark_mask=null;
    public static int menu_left_margin=0;
    public Context mcontext=null;
    public static boolean menuOpen=false;
    public static boolean HEADER_TRANSPARENT=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext=this;
        //get status bar height
        statusBarHeight=getStatusBarHeight();
        //Get Screen Dimensions
        getScreenDim();
        //Status Bar tint color
        setStatusBarTint(this,"#000000FF");
        //Set Header to right size and transparence, and its click and touch listeners
        setHeader(this);
        header=findViewById(R.id.header);
        //Bring Menu from fragment and its listeners
        setMenu(this);
        // Apporter Fragment Gridview, and set its top spacer to be the header height
        setGridview(this);
        //Get Menu left margin
        menu_container=(RelativeLayout) findViewById(R.id.menu_container);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) menu_container.getLayoutParams();
        menu_left_margin=lp.leftMargin;


        // Localisation Permissions
        checkPermissions(this);

    }


    //Set Status Bar Tint color method
    public void setStatusBarTint(Activity activity,String color){
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
    // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor(color));
    }
    //Set header and adjust it with status bar
    public void setHeader(Activity activity){
        final View header = findViewById(R.id.header);
        // if api level sufficient, set status bar transparent, get header height and add status bar height to it plus padding
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();

            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            header.setPadding(0,getStatusBarHeight(),0,0);
            header.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    header_height=header.getHeight();
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,header_height+statusBarHeight);
                    header.setLayoutParams(params);
                    header.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        //Set Onclick listener for menu icon and dark mask
        menu_icon = (ImageView) findViewById(R.id.menu_icon);
        menu_icon.setOnClickListener(menuListener(this));
        dark_mask = (RelativeLayout) findViewById(R.id.dark_mask);
        dark_mask.setOnClickListener(menuListener(this));

        //Set Toggle Listeners for view change icons
        final Button gridview_button = (Button) header.findViewById(R.id.gridview_button);
        final Button storeview_button = (Button) header.findViewById(R.id.storeview_button);
        final ImageView gridview_icon = (ImageView) header.findViewById(R.id.gridview_icon);
        header_height=header.getHeight();
        gridview_button.setOnClickListener(toggleListener(this));
        storeview_button.setOnClickListener(toggleListener(this));
        gridview_button.setSelected(true);
        gridview_icon.getDrawable().setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.colorPrimary, null), PorterDuff.Mode.MULTIPLY);
    }
    //Method to get status bar height
    public int getStatusBarHeight() {
        int status_bar_height = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            status_bar_height = getResources().getDimensionPixelSize(resourceId);
        }
        return status_bar_height;
    }
    //Method to get screen dimension
    public void getScreenDim(){
        //        get height and width of screen
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screen_height_px = displaymetrics.heightPixels;
        screen_width_px = displaymetrics.widthPixels;
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            NavigationBarHeight=resources.getDimensionPixelSize(resourceId);
        }
    }
    //bring menu view fragment
    public static void setMenu(Activity activity){
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MenuFragment menuFragment = new MenuFragment();
        fragmentTransaction.add(R.id.menu_container, menuFragment);
        fragmentTransaction.commit();
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
    //bring map view fragment
    public static void setMapview(Activity activity){
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MapFragment mapFragment = new MapFragment();
        fragmentTransaction.replace(R.id.map_container, mapFragment);
        fragmentTransaction.commit();
    }
    //remove map view fragment
    public static void removeMapview(Activity activity){
        Fragment mapFragment=activity.getFragmentManager().findFragmentById(R.id.map_container);
        if (mapFragment!=null){
        activity.getFragmentManager().beginTransaction().remove(activity.getFragmentManager().findFragmentById(R.id.map_container)).commit();
        }
    }
    //Change header style from red to transparent
    public static void changeHeaderStyle(Activity activity){
        final ImageView gridview_icon = (ImageView) header.findViewById(R.id.gridview_icon);
        final ImageView storeview_icon = (ImageView) header.findViewById(R.id.storeview_icon);
        if (HEADER_TRANSPARENT){
            header.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorPrimary));

            HEADER_TRANSPARENT=false;
        } else{
            header.setBackgroundColor(ContextCompat.getColor(activity,android.R.color.transparent));
            HEADER_TRANSPARENT=true;
        }
    }
    //Set a listener for toggle buttons
    public static View.OnClickListener toggleListener(final Activity activity){
        final View header = activity.findViewById(R.id.header);
        final Button gridview_button = (Button) header.findViewById(R.id.gridview_button);
        final Button storeview_button = (Button) header.findViewById(R.id.storeview_button);
        final ImageView gridview_icon = (ImageView) header.findViewById(R.id.gridview_icon);
        final ImageView storeview_icon = (ImageView) header.findViewById(R.id.storeview_icon);
        final ImageView menu_icon = (ImageView) header.findViewById(R.id.menu_icon);
        final ImageView qr_code_icon = (ImageView) header.findViewById(R.id.qr_code_icon);
        View.OnClickListener toggleListener  = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.gridview_button){
                    gridview_button.setSelected(true);
                    gridview_icon.getDrawable().setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.colorPrimary, null), PorterDuff.Mode.MULTIPLY);
                    storeview_button.setSelected(false);
                    storeview_icon.getDrawable().setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.buttons_default, null), PorterDuff.Mode.MULTIPLY);
                    menu_icon.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.buttons_default, null), PorterDuff.Mode.MULTIPLY);
                    qr_code_icon.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.buttons_default, null), PorterDuff.Mode.MULTIPLY);
                    activity.getFragmentManager().popBackStack("GridView", 0);
                    changeHeaderStyle(activity);
                    removeMapview(activity);
                }
                else {
                    gridview_button.setSelected(false);
                    storeview_button.setSelected(true);
                    menu_icon.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.buttons_when_header_transparent, null), PorterDuff.Mode.MULTIPLY);
                    qr_code_icon.setColorFilter(ResourcesCompat.getColor(activity.getResources(), R.color.buttons_when_header_transparent, null), PorterDuff.Mode.MULTIPLY);
                    FragmentManager manager = activity.getFragmentManager();
                    setMapview(activity);
                    changeHeaderStyle(activity);
                    boolean fragmentPopped = manager.popBackStackImmediate ("StoreView", 0);
                    if (fragmentPopped){
                        activity.getFragmentManager().popBackStack("StoreView", 0);
                    } else{
                        setStoreview(activity);
                    }
                }
            }
        };
        return toggleListener;
    }
    //Handle Slider menu animation
    public static View.OnClickListener menuListener(final Activity activity){
        final View dark_mask=activity.findViewById(R.id.dark_mask)  ;
        View.OnClickListener menuListener  = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (menuOpen) {
                    menu_container.startAnimation(menuDisappear(menu_left_margin,activity));
                    menuOpen=Boolean.FALSE;
                    dark_mask.setAnimation(FadeOut(250,activity));
                }
                else {
                    menu_container.startAnimation(menuAppear(menu_left_margin,activity));
                    menuOpen=!menuOpen;
                    dark_mask.setAnimation(FadeIn(250,activity));
                }
            }
        };
        return menuListener;
    }
    //    Animation menu appear
    private static TranslateAnimation menuAppear(final int fromMargin, final Activity activity) {
        final View menu_container = activity.findViewById(R.id.menu_container);
        final ProgressBar levelBar=(ProgressBar)activity.findViewById(R.id.level_bar);
        final int progressLevel=levelBar.getProgress();
        levelBar.setProgress(0);
        TranslateAnimation animation = new TranslateAnimation(0, -fromMargin, 0, 0);
        animation.setDuration(250);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                // Cancel the animation to stop the menu from popping back.
                menu_container.clearAnimation();
                // Set the new left margin.
                setLeftMargin(menu_container, 0);
                level_bar_anim(activity,levelBar,progressLevel,500);
            }
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
        });
        return animation;
    }
    //    Animation for menu disappear
    private static TranslateAnimation menuDisappear(final int toMargin,Activity activity) {
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
    //FadeIn animation for dark mask
    private static Animation FadeIn(final int duration,Activity activity) {
        final View dark_mask = activity.findViewById(R.id.dark_mask);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(duration);
        fadeIn.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation) {
                // Cancel the animation to stop the menu from popping back.
                menu_container.clearAnimation();
                // Set the new left margin.
                dark_mask.setVisibility(View.VISIBLE);
            }
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
        });
        return fadeIn;
    }
    //FadeOut animation for dark mask
    private static Animation FadeOut(final int duration,Activity activity) {
        final View dark_mask = activity.findViewById(R.id.dark_mask);
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setDuration(duration);
        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation) {
                // Cancel the animation to stop the menu from popping back.
                menu_container.clearAnimation();
                // Set the new left margin.
                dark_mask.setVisibility(View.INVISIBLE);
            }
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
        });
        return fadeOut;
    }
    //Method to set left margin
    private static void setLeftMargin(View view, int leftMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        layoutParams.leftMargin = leftMargin;
        view.requestLayout();
    }
    //Level Bar animation
    private static void level_bar_anim(final Activity activity,final ProgressBar levelBar,final int progressLevel,int duration){
        MenuFragment.LevelBarAnimation anim = new MenuFragment.LevelBarAnimation(levelBar, 0, progressLevel);
        anim.setDuration(duration);
        levelBar.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                levelBar.setProgress(progressLevel);
            }
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
        });

    }
    //Method to check and ask Permissions
    public void checkPermissions(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }
    //Method to convert dip to pixel-not used for now
    private int dipsToPixels(int dips) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(dips * scale + 0.5f);
    }


}
