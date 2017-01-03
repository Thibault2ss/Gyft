package com.thibault.gyft;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        ImageView picture= (ImageView) myInflatedView.findViewById(R.id.picture);
        TextView storename=(TextView) myInflatedView.findViewById(R.id.store_name);
        TextView distance=(TextView) myInflatedView.findViewById(R.id.distance);
        ImageView gift=(ImageView)myInflatedView.findViewById(R.id.gift);
        picture.setImageResource(R.drawable.cupcakes);
        storename.setText("Kite-Hill");
        distance.setText("4 miles");
        gift.setImageResource(R.drawable.gift_100);

        return myInflatedView;
    }

}
