package com.sand_corporation.www.uthao.CustomPlaceAutoComplete;

/**
 * Created by HP on 11/9/2017.
 */

public class PlaceAutoComplete {

    public CharSequence placeID;
    public CharSequence description;

    public PlaceAutoComplete(CharSequence placeID,CharSequence description){
        this.placeID = placeID;
        this.description = description;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
