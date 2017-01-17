package com.thibault.gyft;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by Thibault on 12/29/2016.
 */

public class GridFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView=inflater.inflate(R.layout.grid_view, container, false);
        //Pass info to this view
        //TO DO: get info from the stores around
        GridView gridView = (GridView) myInflatedView.findViewById(R.id.gridview);
        gridView.setAdapter(new GridViewAdapter(getActivity(),getFragmentManager()));

        //Set right height for top spacer
        RelativeLayout spacer_top=(RelativeLayout) myInflatedView.findViewById(R.id.spacer_top);
        int header_height=(int)getResources().getDimension(R.dimen.header_height);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,MainActivity.statusBarHeight+header_height);
        spacer_top.setLayoutParams(params);

        return myInflatedView;
    }



}
