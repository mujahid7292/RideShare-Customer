package com.sand_corporation.www.uthao.CustomerDummyRideHistory;

import android.widget.ImageView;

/**
 * Created by HP on 9/16/2017.
 */

public class CustomerDummyRideHistory {
    private String strCustomerRideDate,strDriverCarModel,strTotalCostOfTheRide,
                strPaymentMode,strCustomerPickUpLocation,strCustomerDestinationLocation,
            strCustomerRideDistance,strDriverName;
    private ImageView imgDriverProfilePic,imgDummyRideHistoryPolyLIne;
    private int intDriverRating;


    public CustomerDummyRideHistory(String strCustomerRideDate, String strDriverCarModel,
                                    String strTotalCostOfTheRide, String strPaymentMode,
                                    String strCustomerPickUpLocation, String strCustomerDestinationLocation,
                                    String strCustomerRideDistance, String strDriverName,
                                    ImageView imgDriverProfilePic, ImageView imgDummyRideHistoryPolyLIne,
                                    int intDriverRating) {
        this.strCustomerRideDate = strCustomerRideDate;
        this.strDriverCarModel = strDriverCarModel;
        this.strTotalCostOfTheRide = strTotalCostOfTheRide;
        this.strPaymentMode = strPaymentMode;
        this.strCustomerPickUpLocation = strCustomerPickUpLocation;
        this.strCustomerDestinationLocation = strCustomerDestinationLocation;
        this.strCustomerRideDistance = strCustomerRideDistance;
        this.strDriverName = strDriverName;
        this.imgDriverProfilePic = imgDriverProfilePic;
        this.imgDummyRideHistoryPolyLIne = imgDummyRideHistoryPolyLIne;
        this.intDriverRating = intDriverRating;
    }

    public String getStrCustomerRideDate() {
        return strCustomerRideDate;
    }

    public void setStrCustomerRideDate(String strCustomerRideDate) {
        this.strCustomerRideDate = strCustomerRideDate;
    }

    public String getStrDriverCarModel() {
        return strDriverCarModel;
    }

    public void setStrDriverCarModel(String strDriverCarModel) {
        this.strDriverCarModel = strDriverCarModel;
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

    public ImageView getImgDriverProfilePic() {
        return imgDriverProfilePic;
    }

    public void setImgDriverProfilePic(ImageView imgDriverProfilePic) {
        this.imgDriverProfilePic = imgDriverProfilePic;
    }

    public ImageView getImgDummyRideHistoryPolyLIne() {
        return imgDummyRideHistoryPolyLIne;
    }

    public void setImgDummyRideHistoryPolyLIne(ImageView imgDummyRideHistoryPolyLIne) {
        this.imgDummyRideHistoryPolyLIne = imgDummyRideHistoryPolyLIne;
    }

    public int getIntDriverRating() {
        return intDriverRating;
    }

    public void setIntDriverRating(int intDriverRating) {
        this.intDriverRating = intDriverRating;
    }
}
