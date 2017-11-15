package com.oamk.beaconeggs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 14.11.2017.
 */

public class FavouritesActivity extends AppCompatActivity{

    private static String TAG = "FavouritesActivity";

    private static String SAVED_FAVOURITES_KEY = "saved_favourites";
    private static String SHARED_PREFERENCES = "shared_preferences";

    private ArrayList<FavouritesItem> favourites = new ArrayList<>();
    private FavouritesAdapter favouritesAdapter;

    private ListView favouriteListView;
    private EditText favouriteInput;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        for(String s: sharedPreferences.getStringSet(SAVED_FAVOURITES_KEY, new HashSet<String>())){
            favourites.add(new FavouritesItem(s));
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        favouriteListView = (ListView) findViewById(R.id.favouriteListView);
        favouritesAdapter = new FavouritesAdapter(this, R.layout.favourite_item, favourites);
        favouriteListView.setAdapter(favouritesAdapter);

        favouriteInput = (EditText) findViewById(R.id.favouriteEditText);
        addButton = (Button) findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favouriteInput.length() > 0){
                    favourites.add(new FavouritesItem(favouriteInput.getText().toString()));
                    favouritesAdapter.notifyDataSetChanged();
                    favouriteInput.setText("");

                    save();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        save();
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private void save(){
        Set<String> savedFavourites = new HashSet<String>();
        for(FavouritesItem favouritesItem: favourites){
            savedFavourites.add(favouritesItem.getName());
        }
        editor = sharedPreferences.edit();
        editor.putStringSet(SAVED_FAVOURITES_KEY, savedFavourites);
        editor.commit();
    }
}
