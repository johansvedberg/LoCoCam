package com.hyco.lococam;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Constants {


    private Constants() {

    }

    public static final HashMap<String, LatLng> LUND_LANDMARKS = new HashMap<String, LatLng>();

    static {
        //Design Centrum
        LUND_LANDMARKS.put("IKDC", new LatLng(55.714493, 13.212647));
        //Botaniska Trädgården
        LUND_LANDMARKS.put("Botan", new LatLng(55.70383, 13.203335));
        //Stadsparken
        LUND_LANDMARKS.put("Stadsparken", new LatLng(55.698437, 13.186255));
        //Malmö Swedbank Stadion
        LUND_LANDMARKS.put("Swedbank", new LatLng(55.58339, 12.987642));
        // Syrenvägen 12A
        LUND_LANDMARKS.put("@Home", new LatLng(55.69, 13.20202));
    }
}
