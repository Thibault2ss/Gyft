package com.thibault.gyft;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Thibault on 12/24/2016.
 */

public final class GridViewAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;
    private final Context mContext;
    private FragmentManager frag_manager=null;

    //    Define constructor
//    TO DO: GET DATA FROM DATABASE
    public GridViewAdapter(Context context, FragmentManager fragmentManager) {
        mInflater = LayoutInflater.from(context);
        this.frag_manager=fragmentManager;

        mItems.add(new Item("Kite-Hill",       R.drawable.cupcakes,"4 miles",R.drawable.gift_100));
        mItems.add(new Item("Kite-Hill",   R.drawable.cupcakes,"4 miles",R.drawable.gift_50));
        mItems.add(new Item("Kite-Hill", R.drawable.cupcakes,"4 miles",R.drawable.gift_100));
        mItems.add(new Item("Kite-Hill",      R.drawable.cupcakes,"4 miles",R.drawable.gift_100));
        mItems.add(new Item("Kite-Hill",       R.drawable.cupcakes,"4 miles",R.drawable.gift_1000));
        mItems.add(new Item("Kite-Hill",       R.drawable.cupcakes,"4 miles",R.drawable.gift_50));
        mItems.add(new Item("Kite-Hill",   R.drawable.cupcakes,"4 miles",R.drawable.gift_100));
        mItems.add(new Item("Kite-Hill", R.drawable.cupcakes,"4 miles",R.drawable.gift_1000));
        mItems.add(new Item("Kite-Hill",      R.drawable.cupcakes,"4 miles",R.drawable.gift_100));
        mItems.add(new Item("Kite-Hill",       R.drawable.cupcakes,"4 miles",R.drawable.gift_50));

        this.mContext=context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).drawableId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        ImageView gift;
        TextView store_name;
        TextView distance;

        //If ever no view is selected, inflate a grid item
        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.store_name, v.findViewById(R.id.store_name));
            v.setTag(R.id.distance, v.findViewById(R.id.distance));
            v.setTag(R.id.gift, v.findViewById(R.id.gift));
        }
        //pass info from items in list to view
        picture = (ImageView) v.getTag(R.id.picture);
        store_name = (TextView) v.getTag(R.id.store_name);
        distance=(TextView) v.getTag(R.id.distance);
        gift=(ImageView) v.getTag(R.id.gift);

        Item item = getItem(i);
        picture.setImageResource(item.drawableId);
        store_name.setText(item.store_name);
        distance.setText(item.distance);
        gift.setImageResource(item.giftId);



        //Set click listener for each view, to send to store view
        //TO DO: send info in the Transaction
        v.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = frag_manager.beginTransaction();
                StoreFragment fragment = new StoreFragment();
                fragmentTransaction.replace(R.id.store_container, fragment);
                fragmentTransaction.commit();

            }
        });
        return v;
    }

    private static class Item {
        public final String store_name;
        public final int drawableId;
        public final String distance;
        public final int giftId;


        Item(String name, int drawableId,String distance,int giftId) {
            this.store_name = name;
            this.drawableId = drawableId;
            this.distance=distance;
            this.giftId=giftId;
        }
    }
}