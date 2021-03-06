package com.oamk.beaconeggs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

/**
 * Created by Administrator on 8.11.2017.
 */

public class LunchMenuFetcher {
    private final static String TAG = "LunchMenuFetcher";
    private static String SAVED_FAVOURITES_KEY = "saved_favourites";
    private static String SHARED_PREFERENCES = "shared_preferences";

    private SharedPreferences sharedPreferences;
    private boolean isFavourite;

    public void fetchLunchMenu(final ArrayList lunchMenuItems, final LunchMenuItemAdapter lunchMenuItemAdapter, Context context, String date){
        Log.d(TAG, "fetchLunchMenu");

        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        String URL = "http://www.amica.fi/api/restaurant/menu/day?date=" + date + "&language=en&restaurantPageId=66287";
        Log.d(TAG, URL);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        lunchMenuItems.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray sets = jsonObject.getJSONObject("LunchMenu").getJSONArray("SetMenus");

                            for (int i = 0; i < sets.length(); i++) {
                                String title = sets.getJSONObject(i).getString("Name");
                                Log.d(TAG, "title: " + title);
                                JSONArray meals = sets.getJSONObject(i).getJSONArray("Meals");
                                String set = "";
                                for (int j = 0; j < meals.length(); j++) {
                                    set += (j == meals.length() - 1)
                                            ? meals.getJSONObject(j).get("Name").toString()
                                            : meals.getJSONObject(j).get("Name").toString() + "\n";
                                }
                                if (set.length() != 0) {
                                    Log.d("MEAL", set);
                                    for(String s: sharedPreferences.getStringSet(SAVED_FAVOURITES_KEY, new HashSet<String>())){
                                        if(set.toLowerCase().contains(s.toLowerCase())){
                                            isFavourite = true;
                                            break;
                                        }else isFavourite = false;
                                    }
                                    lunchMenuItems.add(new LunchMenuItem(title, set, "", isFavourite));
                                }
                                lunchMenuItemAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }
}
