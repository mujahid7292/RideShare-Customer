package com.sand_corporation.www.uthao.CustomerRideHistory;

import android.widget.ImageView;

/**
 * Created by HP on 2/13/2018.
 */

public class CustomerRideHistory {
    private String strCustomerRideDate, strVehicleModel,strTotalCostOfTheRide,
            strPaymentMode,strCustomerPickUpLocation,strCustomerDestinationLocation,
            strCustomerRideDistance,strDriverName, polyLineTotalRideLength;
    private ImageView imgDriverProfilePic;
    private int intDriverRating;

    public CustomerRideHistory() {

    }

    public CustomerRideHistory(String strCustomerRideDate, String strVehicleModel,
                               String strTotalCostOfTheRide, String strPaymentMode,
                               String strCustomerPickUpLocation,
                               String strCustomerDestinationLocation,
                               String strCustomerRideDistance, String strDriverName,
                               String polyLineTotalRideLength, ImageView imgDriverProfilePic,
                               int intDriverRating) {
        this.strCustomerRideDate = strCustomerRideDate;
        this.strVehicleModel = strVehicleModel;
        this.strTotalCostOfTheRide = strTotalCostOfTheRide;
        this.strPaymentMode = strPaymentMode;
        this.strCustomerPickUpLocation = strCustomerPickUpLocation;
        this.strCustomerDestinationLocation = strCustomerDestinationLocation;
        this.strCustomerRideDistance = strCustomerRideDistance;
        this.strDriverName = strDriverName;
        this.polyLineTotalRideLength = polyLineTotalRideLength;
        this.imgDriverProfilePic = imgDriverProfilePic;
        this.intDriverRating = intDriverRating;
    }

    public String getStrCustomerRideDate() {
        return strCustomerRideDate;
    }

    public void setStrCustomerRideDate(String strCustomerRideDate) {
        this.strCustomerRideDate = strCustomerRideDate;
    }

    public String getStrVehicleModel() {
        return strVehicleModel;
    }

    public void setStrVehicleModel(String strVehicleModel) {
        this.strVehicleModel = strVehicleModel;
    }

    public String getStrTotalCostOfTheRide() {
        return strTotalCostOfTheRide;
    }

    public void setStrTotalCostOfTheRide(String strTotalCostOfTheRide) {
        this.strTotalCostOfTheRide = strTotalCostOfTheRide;
    }

    public String getStrPaymentMode() {
        return strPaymentMode;
    }

    public void setStrPaymentMode(String strPaymentMode) {
        this.strPaymentMode = strPaymentMode;
    }

    public String getStrCustomerPickUpLocation() {
        return strCustomerPickUpLocation;
    }

    public void setStrCustomerPickUpLocation(String strCustomerPickUpLocation) {
        this.strCustomerPickUpLocation = strCustomerPickUpLocation;
    }

    public String getStrCustomerDestinationLocation() {
        return strCustomerDestinationLocation;
    }

    public void setStrCustomerDestinationLocation(String strCustomerDestinationLocation) {
        this.strCustomerDestinationLocation = strCustomerDestinationLocation;
    }

    public String getStrCustomerRideDistance() {
        return strCustomerRideDistance;
    }

    public void setStrCustomerRideDistance(String strCustomerRideDistance) {
        this.strCustomerRideDistance = strCustomerRideDistance;
    }

    public String getStrDriverName() {
        return strDriverName;
    }

    public void setStrDriverName(String strDriverName) {
        this.strDriverName = strDriverName;
    }

    public String getPolyLineTotalRideLength() {
        return polyLineTotalRideLength;
    }

    public void setPolyLineTotalRideLength(String polyLineTotalRideLength) {
        this.polyLineTotalRideLength = polyLineTotalRideLength;
    }

    public ImageView getImgDriverProfilePic() {
        return imgDriverProfilePic;
    }

    public void setImgDriverProfilePic(ImageView imgDriverProfilePic) {
        this.imgDriverProfilePic = imgDriverProfilePic;
    }

    public int getIntDriverRating() {
        return intDriverRating;
    }

    public void setIntDriverRating(int intDriverRating) {
        this.intDriverRating = intDriverRating;
    }
}
