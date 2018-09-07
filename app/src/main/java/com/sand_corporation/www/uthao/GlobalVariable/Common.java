package com.sand_corporation.www.uthao.GlobalVariable;

import com.sand_corporation.www.uthao.DeviceToDeviceNotification.IFCMService;
import com.sand_corporation.www.uthao.DeviceToDeviceNotification.RetrofitFCMClient;
import com.sand_corporation.www.uthao.Remote.IGoogleAPI;
import com.sand_corporation.www.uthao.Remote.RetrofitClient;

/**
 * Created by HP on 11/16/2017.
 */

public class Common {
    public static final String baseUrl = "https://maps.googleapis.com";
    public static String fcmURL = "https://fcm.googleapis.com/";
    public static String driverOrBiker;


    public static IGoogleAPI getGoogleApi(){

        return RetrofitClient.getClient(baseUrl).create(IGoogleAPI.class);
    }

    public static IFCMService getFCMService(){

        return RetrofitFCMClient.getClient(fcmURL).create(IFCMService.class);
    }
}
