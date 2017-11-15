package com.oamk.beaconeggs;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.oamk.beaconeggs.estimote.BeaconNotificationManager;
import com.oamk.beaconeggs.estimote.EstimoteCloudBeaconDetails;
import com.oamk.beaconeggs.estimote.EstimoteCloudBeaconDetailsFactory;
import com.oamk.beaconeggs.estimote.ProximityContentManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String fDate;
    private Context mContext;

    private ProximityContentManager proximityContentManager;
    private BeaconNotificationManager beaconNotificationManager;

    private LunchMenuFetcher lunchMenuFetcher = new LunchMenuFetcher();
    private ArrayList<LunchMenuItem> lunchMenuItems = new ArrayList<>();
    private LunchMenuItemAdapter lunchMenuItemAdapter;

    private ListView lunchMenuListView;
    private LinearLayout searchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mContext = this;
        Date cDate = new Date();
        fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        lunchMenuListView = (ListView) findViewById(R.id.menuListView);
        lunchMenuItemAdapter = new LunchMenuItemAdapter(this, R.layout.menu_item, lunchMenuItems);
        lunchMenuListView.setAdapter(lunchMenuItemAdapter);

        searchLayout = (LinearLayout) findViewById(R.id.searchLayout);

        beaconNotificationManager = new BeaconNotificationManager(this);
        beaconNotificationManager.addNotification(
            "359bdb94a0f2f3d0fdba03eff8002108",
            "There is a restaurant nearby!",
            "Please come back"
        );

        proximityContentManager = new ProximityContentManager(this,
                Arrays.asList(
                        "359bdb94a0f2f3d0fdba03eff8002108"),
                new EstimoteCloudBeaconDetailsFactory());
        proximityContentManager.setListener(new ProximityContentManager.Listener() {
            @Override
            public void onContentChanged(Object content) {
                Log.d(TAG, "onContentChanged");
                String text;
                Integer backgroundColor;
                if (content != null) {
                    EstimoteCloudBeaconDetails beaconDetails = (EstimoteCloudBeaconDetails) content;
                    text = beaconDetails.getBeaconName();

                    lunchMenuFetcher.fetchLunchMenu(lunchMenuItems, lunchMenuItemAdapter, mContext, fDate);

                    searchLayout.setVisibility(View.INVISIBLE);

                    lunchMenuItemAdapter.notifyDataSetChanged();
                } else {
                    text = "";
                    lunchMenuItems.clear();

                    searchLayout.setVisibility(View.VISIBLE);

                    lunchMenuItemAdapter.notifyDataSetChanged();
                }
                ((TextView) findViewById(R.id.restaurantTitle)).setText(text);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favourites) {
            Intent intent = new Intent(this, FavouritesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else {
            Log.d(TAG, "Starting ProximityContentManager content updates");
            proximityContentManager.startContentUpdates();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Stopping ProximityContentManager content updates");
        proximityContentManager.stopContentUpdates();
        beaconNotificationManager.startMonitoring();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proximityContentManager.destroy();
    }
}
