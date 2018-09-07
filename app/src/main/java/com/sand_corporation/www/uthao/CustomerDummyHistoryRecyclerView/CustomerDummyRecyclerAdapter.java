package com.sand_corporation.www.uthao.CustomerDummyHistoryRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sand_corporation.www.uthao.CustomerDummyHistorySinglePageActivity;
import com.sand_corporation.www.uthao.R;

import java.util.ArrayList;

/**
 * Created by HP on 11/4/2017.
 */

public class CustomerDummyRecyclerAdapter
        extends RecyclerView.Adapter<CustomerDummyRecyclerAdapter.MyViewHolder> {

    //The first thing we need is customer life time ride history. So as we created object of DummyHistory.
    //Than we should put all the history into ArrayList.
    private ArrayList<DummyHistory> arrayList = new ArrayList<>();
    //By above code we are putting customer's life time ride history into ArrayList.
    private Context context;

    //Below we are creating a constructor of CustomerDummyRecyclerAdapter.class. Every time, we create
    //an object of this class, this below constructor will automatically called. So every time
    //customers life time ride history will be saved in the ArrayList.
    public CustomerDummyRecyclerAdapter(ArrayList<DummyHistory> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Below we will inflate "item_history" layout.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_row_dummy_history,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DummyHistory dummyHistory = arrayList.get(position);
        holder.customerDummyRideId.setText(dummyHistory.getCustomerDummyRideId());
        holder.rideTimeForAdapter.setText(dummyHistory.getRideTimeForAdapter());
        holder.customerpickUpForAdapter.setText(dummyHistory.getCustomerpickUpForAdapter());
        holder.customerDestinationForAdapter.setText(dummyHistory.getCustomerDestinationForAdapter());
        if (dummyHistory.getRidePolyLineMapForAdapter() != null){
            getMaximumAMountOfVM();
            holder.ridePolyLineMapForAdapter.setImageBitmap(getImage(dummyHistory.getRidePolyLineMapForAdapter()));
        }
        //We could also write like below
        //holder.customerRideId.setText(arrayList.get(position).getRideId());
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private void getMaximumAMountOfVM(){
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int freeMemory = (int) (Runtime.getRuntime().freeMemory() / 1024);
        Log.i("Check","maxMemory: " + maxMemory + "\n" +
        "freeMemory: " + freeMemory);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView customerDummyRideId, rideTimeForAdapter,
                customerpickUpForAdapter, customerDestinationForAdapter;
        private ImageView ridePolyLineMapForAdapter;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //We have initialized each history item's on click listener.

            customerDummyRideId = itemView.findViewById(R.id.customerDummyRideId);
            rideTimeForAdapter = itemView.findViewById(R.id.rideTimeForAdapter);
            customerpickUpForAdapter = itemView.findViewById(R.id.customerpickUpForAdapter);
            customerDestinationForAdapter = itemView.findViewById(R.id.customerDestinationForAdapter);

            ridePolyLineMapForAdapter = itemView.findViewById(R.id.ridePolyLineMapForAdapter);
            //In "item_history" there will be only one text view, where we will show only the ride id.
        }

        @Override
        public void onClick(View view) {
            //After click we will move our user to new activity and there we will
            //show detail history of the ride.
            Intent intent = new Intent(view.getContext(), CustomerDummyHistorySinglePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("customerDummyRideId",customerDummyRideId.getText().toString());
            intent.putExtras(bundle);  //be careful here will be putExtra's
            view.getContext().startActivity(intent);  //Without context we will not be able to startActivity.


        }
    }
}
