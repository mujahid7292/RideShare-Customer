package com.sand_corporation.www.uthao.CustomerDummyHistoryRecyclerView;

/**
 * Created by HP on 11/4/2017.
 */

public class DummyHistory {

    private String rideTimeForAdapter, customerpickUpForAdapter, customerDestinationForAdapter,
            customerDummyRideId;
    private byte[] ridePolyLineMapForAdapter;

    public DummyHistory(String rideTimeForAdapter,
                        String customerpickUpForAdapter,
                        String customerDestinationForAdapter,
                        String customerDummyRideId,
                        byte[] ridePolyLineMapForAdapter) {
        this.rideTimeForAdapter = rideTimeForAdapter;
        this.customerpickUpForAdapter = customerpickUpForAdapter;
        this.customerDestinationForAdapter = customerDestinationForAdapter;
        this.customerDummyRideId = customerDummyRideId;
        this.ridePolyLineMapForAdapter = ridePolyLineMapForAdapter;
    }

    public String getCustomerDummyRideId() {
        return customerDummyRideId;
    }

    public String getRideTimeForAdapter() {
        return rideTimeForAdapter;
    }

    public String getCustomerpickUpForAdapter() {
        return customerpickUpForAdapter;
    }

    public String getCustomerDestinationForAdapter() {
        return customerDestinationForAdapter;
    }

    public byte[] getRidePolyLineMapForAdapter() {
        return ridePolyLineMapForAdapter;
    }
}
