package com.sand_corporation.www.uthao;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by HP on 11/20/2017.
 */

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private TextView txtCustomerRideDate,txtDriverCarModel,txtTotalCostOfTheRide,
            txtPaymentMode,txtCustomerPickUpLocation,txtCustomerDestinationLocation,
            txtCustomerRideDistance,txtDriverName;
    private ImageView imgDriverProfilePic;
    private RatingBar rtnDriverRating;
    private LinearLayout bottomSheetLinearLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        FirebaseCrash.log("BottomSheetFragment:onCreate.called");
        Crashlytics.log("BottomSheetFragment:onCreate.called");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_fragment,container,false);

        bottomSheetLinearLayout = view.findViewById(R.id.bottomSheetLinearLayout);
        bottomSheetLinearLayout.setVisibility(View.VISIBLE);
        txtCustomerRideDate = view.findViewById(R.id.txtCustomerRideDate);
        txtDriverCarModel = view.findViewById(R.id.txtDriverCarModel);
        txtTotalCostOfTheRide = view.findViewById(R.id.txtTotalCostOfTheRide);
        txtPaymentMode = view.findViewById(R.id.txtPaymentMode);
        txtCustomerPickUpLocation = view.findViewById(R.id.txtCustomerPickUpLocation);
        txtCustomerDestinationLocation = view.findViewById(R.id.txtCustomerDestinationLocation);
        txtCustomerRideDistance = view.findViewById(R.id.txtCustomerRideDistance);
        txtDriverName = view.findViewById(R.id.txtDriverName);
        imgDriverProfilePic = view.findViewById(R.id.imgDriverProfilePic);
        rtnDriverRating = view.findViewById(R.id.rtnDriverRating);

        txtDriverName.setText("Tamim");

        return view;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new
            BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {

                        case BottomSheetBehavior.STATE_COLLAPSED:{

                            Log.d("BSB","collapsed") ;
                        }
                        case BottomSheetBehavior.STATE_SETTLING:{

                            Log.d("BSB","settling") ;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED:{

                            Log.d("BSB","expanded") ;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {

                            Log.d("BSB" , "hidden") ;
                            dismiss();
                        }
                        case BottomSheetBehavior.STATE_DRAGGING: {

                            Log.d("BSB","dragging") ;
                        }
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    Log.d("BSB","sliding " + slideOffset ) ;
                }
            };


//    @SuppressLint("RestrictedApi")
//    @Override
//    public void setupDialog(Dialog dialog, int style) {
//        super.setupDialog(dialog, style);
//        View view = View.inflate(getContext(), R.layout.bottom_sheet_fragment, null);
//        bottomSheetLinearLayout = view.findViewById(R.id.bottomSheetLinearLayout);
//        bottomSheetLinearLayout.setVisibility(View.VISIBLE);
//        txtCustomerRideDate = view.findViewById(R.id.txtCustomerRideDate);
//        txtDriverCarModel = view.findViewById(R.id.txtDriverCarModel);
//        txtTotalCostOfTheRide = view.findViewById(R.id.txtTotalCostOfTheRide);
//        txtPaymentMode = view.findViewById(R.id.txtPaymentMode);
//        txtCustomerPickUpLocation = view.findViewById(R.id.txtCustomerPickUpLocation);
//        txtCustomerDestinationLocation = view.findViewById(R.id.txtCustomerDestinationLocation);
//        txtCustomerRideDistance = view.findViewById(R.id.txtCustomerRideDistance);
//        txtDriverName = view.findViewById(R.id.txtDriverName);
//        imgDriverProfilePic = view.findViewById(R.id.imgDriverProfilePic);
//        rtnDriverRating = view.findViewById(R.id.rtnDriverRating);
//
//        txtDriverName.setText("Tamim");
//        dialog.setContentView(view);
//
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
//        CoordinatorLayout.Behavior behavior = params.getBehavior();
//
//        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
//            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
//        }
//    }
}
