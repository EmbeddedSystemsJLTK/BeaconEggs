package com.oamk.beaconeggs;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 8.11.2017.
 */

public class LunchMenuFetcher {
    private final static String TAG = "LunchMenuFetcher";

    public void fetchLunchMenu(ArrayList lunchMenuItems){
        Log.d(TAG, "fetchLunchMenu");

        lunchMenuItems.add(new LunchMenuItem("VEGETABLE LUNCH", "Cauliflower and manchego patties", "Kakkaaaaa and honey sauce"));
    }
}
