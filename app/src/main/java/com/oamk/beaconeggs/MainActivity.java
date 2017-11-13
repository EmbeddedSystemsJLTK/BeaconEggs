package com.oamk.beaconeggs;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proximityContentManager.destroy();
    }
}
