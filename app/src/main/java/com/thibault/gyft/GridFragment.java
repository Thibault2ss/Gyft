package com.thibault.gyft;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


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
        //TO DO: get info from the stores aroung
        GridView gridView = (GridView) myInflatedView.findViewById(R.id.gridview);
        gridView.setAdapter(new GridViewAdapter(getActivity(),getFragmentManager()));
        return myInflatedView;
    }
}
