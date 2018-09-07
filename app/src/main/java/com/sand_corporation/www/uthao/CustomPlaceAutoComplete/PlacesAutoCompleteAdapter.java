package com.sand_corporation.www.uthao.CustomPlaceAutoComplete;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.crash.FirebaseCrash;
import com.sand_corporation.www.uthao.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by HP on 11/9/2017.
 */

public class PlacesAutoCompleteAdapter extends RecyclerView.Adapter<PlacesAutoCompleteAdapter.PlacePredictionHolder>
                implements Filterable{
    private static final String TAG = "PlacesAutoCompleteAdapter";
    public ArrayList<PlaceAutoComplete> mResultList;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds mBounds;
    private AutocompleteFilter mPlaceFilter;
    private Context mContext;
    private int layout;

    public PlacesAutoCompleteAdapter(Context context, int resource, GoogleApiClient googleApiClient,
                                     LatLngBounds bounds, AutocompleteFilter filter) {
        mContext = context;
        layout = resource;
        mGoogleApiClient = googleApiClient;
        mBounds = bounds;
        mPlaceFilter = filter;
    }

    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                //Here we will filter the search result
                FilterResults filterResults = new FilterResults();
                //We will first check whether our user put some text in the EditText field or not
                if (charSequence != null){
                    //charSequence == String put on the EditText field
                    //As customer has put some string in it, we will send query to AutoComplete Api
                    mResultList = getAutocomplete(charSequence); //Here may be problem should use PlaceAutocomplete API
                    if (mResultList != null){
                        // The API successfully returned results.
                        //Now we will put all the returned place result to mResultList Array
                        filterResults.values = mResultList;
                        filterResults.count = mResultList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0){
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                }else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private ArrayList<PlaceAutoComplete> getAutocomplete(CharSequence charSequence){
        if (mGoogleApiClient.isConnected()){
            Log.i("", "Starting autocomplete query for: " + charSequence);

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results = Places.GeoDataApi
                    .getAutocompletePredictions(mGoogleApiClient,charSequence.toString(),
                    mBounds,mPlaceFilter);
            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            AutocompletePredictionBuffer autocompletePrediction = results.await(30, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            Status status = autocompletePrediction.getStatus();
            if (!status.isSuccess()){
                Toast.makeText(mContext, "Error contacting API: " + status.toString(),
                        Toast.LENGTH_SHORT).show();
                Log.e("", "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePrediction.release();
                return null;
            }

            Log.i("", "Query completed. Received " + autocompletePrediction.getCount()
                    + " predictions.");

            // Copy the results into our own data structure, because we can't hold onto the buffer.
            // AutocompletePrediction objects encapsulate the API response (place ID and description).
            Iterator<AutocompletePrediction>iterator = autocompletePrediction.iterator();
            ArrayList resultList = new ArrayList(autocompletePrediction.getCount());
            try {

                while (iterator.hasNext()){
                    AutocompletePrediction prediction = iterator.next();
                    //Now we will Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                    PlaceAutoComplete placeAutoComplete = new PlaceAutoComplete(prediction.getPlaceId()
                            ,prediction.getFullText(null));
                    resultList.add(placeAutoComplete);
                }
            } catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                Crashlytics.log("PlacesAutoCompleteAdapter:IndexOutOfBounds");
                FirebaseCrash.log("PlacesAutoCompleteAdapter:IndexOutOfBounds");
            }


            // Release the buffer now that all data has been copied.
            autocompletePrediction.release();
            return resultList;
        }
        Log.e("", "Google API client is not connected for autocomplete query.");
        return null;
    }

    @Override
    public PlacePredictionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layout,parent,false);
        PlacePredictionHolder holder = new PlacePredictionHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PlacePredictionHolder holder, final int position) {
        try {
            holder.mPrediction.setText(mResultList.get(position).description);
        } catch (IndexOutOfBoundsException e){
            FirebaseCrash.log("PlacesAutoCompleteAdapter.class, Index out of bound"
            + e.toString());
        }

        holder.mRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mGetLatLonCallback.getLocation(mResultList.get(position).toString());

            }
        });
    }

    @Override
    public int getItemCount() {
        if(mResultList != null)
            return mResultList.size();
        else
            return 0;
    }

    public PlaceAutoComplete getItem(int position){
        return mResultList.get(position);
    }


    public class PlacePredictionHolder extends RecyclerView.ViewHolder{
        private TextView mPrediction;
        private LinearLayout mRow;

        public PlacePredictionHolder(View itemView) {
            super(itemView);
            mPrediction = itemView.findViewById(R.id.address);
            mRow = itemView.findViewById(R.id.predictedRow);
        }
    }
}
