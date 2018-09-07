package com.sand_corporation.www.uthao.CustomerHistoryRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sand_corporation.www.uthao.CustomerHistorySinglePageActivity;
import com.sand_corporation.www.uthao.R;

import java.util.ArrayList;

/**
 * Created by HP on 11/4/2017.
 */

public class CustomerRecyclerAdapter extends RecyclerView.Adapter<CustomerRecyclerAdapter.MyViewHolder> {

    //The first thing we need is customer life time ride history. So as we created object of DummyHistory.
    //Than we should put all the history into ArrayList.
    private ArrayList<History> arrayList = new ArrayList<>();
    //By above code we are putting customer's life time ride history into ArrayList.
    private Context context;

    //Below we are creating a constructor of CustomerDummyRecyclerAdapter.class. Every time, we create
    //an object of this class, this below constructor will automatically called. So every time
    //customers life time ride history will be saved in the ArrayList.
    public CustomerRecyclerAdapter(ArrayList<History> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Below we will inflate "item_history" layout.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_history,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        History history = arrayList.get(position);
        holder.customerRideId.setText(history.getRideId());
        holder.rideTime.setText(history.getTime());
        //We could also write like below
        //holder.customerRideId.setText(arrayList.get(position).getRideId());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView customerRideId, rideTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //We have initialized each history item's on click listener.

            customerRideId = itemView.findViewById(R.id.customerRideId);
            rideTime = itemView.findViewById(R.id.rideTime);
            //In "item_history" there will be only one text view, where we will show only the ride id.
        }

        @Override
        public void onClick(View view) {
            //After click we will move our user to new activity and there we will
            //show detail history of the ride.
            Intent intent = new Intent(view.getContext(),CustomerHistorySinglePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("customerRideId",customerRideId.getText().toString());
            intent.putExtras(bundle);  //be careful here will be putExtra's
            view.getContext().startActivity(intent);  //Without context we will not be able to startActivity.


        }
    }
}
