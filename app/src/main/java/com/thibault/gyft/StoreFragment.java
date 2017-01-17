package com.thibault.gyft;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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
import android.widget.FrameLayout;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Thibault on 12/29/2016.
 */

public class StoreFragment extends Fragment {
    public int navButtonHeight;
    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myInflatedView=inflater.inflate(R.layout.store_view, container, false);
        //Pass info from the square item to the store view
        //TO DO: get info from the transaction manager
        dataTransaction(myInflatedView);

        //Set dimensions of a bunch of stuff: height top spacer, height Scrollview, nav button position
        setDimensions(myInflatedView);

        //Allow touch through transparent layer of Scrollview
        setTouchThrough(myInflatedView);

        //Set MapView
        setMapView(myInflatedView,savedInstanceState);

        //Set Magnet effect for scrollview
        final ScrollView sv=(ScrollView) myInflatedView.findViewById(R.id.sv);
        sv.setOnTouchListener(MagnetEffect(sv));

        //Add picture parallax, re-position nav button when scroll
        scrollEffects(myInflatedView,sv);

        return myInflatedView;
    }

    //Method to pass info from the square item (or server) to the store view
    //TO DO: get info from the transaction manager
    public void dataTransaction(View rootview){
    final ImageView picture= (ImageView) rootview.findViewById(R.id.picture);
    TextView storename=(TextView) rootview.findViewById(R.id.store_name);
    TextView distance=(TextView) rootview.findViewById(R.id.distance);
    ImageView gift=(ImageView)rootview.findViewById(R.id.gift);

    //Fill info in fragment
    picture.setImageResource(R.drawable.cupcakes);
    storename.setText("Kite-Hill");
    distance.setText("4 miles");
    gift.setImageResource(R.drawable.gift_100);
}

    //Set top spacer height, so map doesn't overlay header
    public void setDimensions(View rootview){

        //Set Top Spacer Height
        RelativeLayout spacer_top=(RelativeLayout) rootview.findViewById(R.id.spacer_top);
        int header_height=(int)getResources().getDimension(R.dimen.header_height);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)spacer_top.getLayoutParams();
        params.height=MainActivity.statusBarHeight+header_height;
        spacer_top.setLayoutParams(params);

        //Set Invisible layer Height
        View transparent_layer= rootview.findViewById(R.id.transparent);
        int footer_height=(int)getResources().getDimension(R.dimen.footer_height);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) transparent_layer.getLayoutParams();
        params1.height=MainActivity.screen_height_px+MainActivity.NavigationBarHeight-footer_height;
        transparent_layer.setLayoutParams(params1);

        //Set Footer container Height (to add the white background of footer scroller
        RelativeLayout footer_container=(RelativeLayout) rootview.findViewById(R.id.footer_container);
        RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) footer_container.getLayoutParams();
        params3.height=MainActivity.screen_height_px+MainActivity.NavigationBarHeight;
        footer_container.setLayoutParams(params3);

        //Set Scrollview Height
        RelativeLayout scrollview=(RelativeLayout) rootview.findViewById(R.id.sv_layout);
        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) scrollview.getLayoutParams();
        params2.height=2*(MainActivity.screen_height_px+MainActivity.NavigationBarHeight)-footer_height;
        scrollview.setLayoutParams(params2);

        //Set Navigation button position
        final View navButton=rootview.findViewById(R.id.floating_button);
        rootview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                navButtonHeight = navButton.getHeight();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) navButton.getLayoutParams();
                lp.topMargin=-navButtonHeight/2;
                navButton.setLayoutParams(lp);
                getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    //Method to allow touch through the transparent layer of the scrollview
    public void setTouchThrough(View rootview){
        View tr= rootview.findViewById(R.id.transparent);
        tr.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                int RawY=(int)event.getRawY();
                if (RawY<(MainActivity.header_height+MainActivity.statusBarHeight)){
                    MainActivity.header.dispatchTouchEvent(event);
                } else{
                    mMapView.dispatchTouchEvent(event);
                }
                return true;
            }
        });
    }

    //set map method
    public void setMapView(View rootview,Bundle savedInstanceState){
        mMapView = (MapView) rootview.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    googleMap.setMyLocationEnabled(true);
                }
                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    //Magnet effect of scrollview
    public View.OnTouchListener MagnetEffect(final ScrollView sv){
        View.OnTouchListener MagnetEffect  = new View.OnTouchListener(){
            int lastY=0;
            int i=0;
            int COUNTER=10;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                i++;
                if (i>COUNTER){
                    lastY=sv.getScrollY();
                    i=0;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    int currentY = sv.getScrollY();
                    if (currentY-lastY>0){
                        sv.post(new Runnable() {
                            @Override
                            public void run() {
                                sv.smoothScrollTo(0, sv.getBottom());
                            }
                        });
                    }
                    else{
                        sv.post(new Runnable() {
                            @Override
                            public void run() {
                                sv.smoothScrollTo(0, 0);
                            }
                        });
                    }
                }
                return false;
            }
        };
        return MagnetEffect;
    }

    //Method for some effects when scroll
    public void scrollEffects(View rootview,final ScrollView sv){
        final View navButton=rootview.findViewById(R.id.floating_button);
        final View picture=rootview.findViewById(R.id.picture);
        sv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                float scrollY = sv.getScrollY();
                float scrollYmax=sv.getBottom();
                RelativeLayout.LayoutParams params3=(RelativeLayout.LayoutParams) navButton.getLayoutParams();
                RelativeLayout.LayoutParams params4=(RelativeLayout.LayoutParams) picture.getLayoutParams();
                float newTopMargin=scrollY*(navButtonHeight/scrollYmax)-(navButtonHeight/2);
                float newTopMargin1=((scrollY+300)-scrollYmax)/5;
                params3.setMargins(params3.leftMargin,(int)newTopMargin,params3.rightMargin,params3.bottomMargin);
                params4.setMargins(params4.leftMargin,(int)newTopMargin1,params4.rightMargin,params4.bottomMargin);
                navButton.setLayoutParams(params3);
                picture.setLayoutParams(params4);
            }
        });
    }

}
