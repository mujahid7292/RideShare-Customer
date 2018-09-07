package com.sand_corporation.www.uthao.CustomerHistoryRecyclerView;

/**
 * Created by HP on 11/4/2017.
 */

public class History {

    private String rideId;
    private String time;




    public History(String rideId, String time){
        this.rideId = rideId;
        this.time = time;

    }

    public String getRideId() {
        return rideId;
    }
    public String getTime() {
        return time;
    }

}
