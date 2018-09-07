package com.sand_corporation.www.uthao.CustomPlaceAutoComplete;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by HP on 11/10/2017.
 */

public class RecyclerItemClickListener implements OnItemTouchListener {

    //First we create an interface with the name OnItemClickListener and than we create an object of this interface
    //******************************interface*********************************************
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
    //******************************interface*********************************************

    GestureDetector mGestureDetector;
    //Create constructor from RecyclerItemClickListener class
    public RecyclerItemClickListener(Context context, OnItemClickListener listener){
        mListener = listener;
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
                //We must return true from here, other wise inside onInterceptTouchEvent()
                //method's if statement will not work.
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(),e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)){
            try {
                mListener.onItemClick(childView,rv.getChildLayoutPosition(childView));
            } catch (IndexOutOfBoundsException ex){
                Log.i("Check", "RecyclerItemClickListener:IndexOutOFBoundOccur");
                FirebaseCrash.log("RecyclerItemClickListener:IndexOutOFBoundOccur");
                Crashlytics.log("RecyclerItemClickListener:IndexOutOFBoundOccur");
            }

            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
