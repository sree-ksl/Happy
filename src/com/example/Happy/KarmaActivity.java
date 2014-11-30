package com.example.Happy;

import android.app.ActionBar;
import android.app.ListActivity;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.*;
import com.parse.*;

import java.util.List;

/**
 * Created by hello on 31/08/14.
 */
public class KarmaActivity extends ListActivity {

    protected List<ParseObject> mActivities;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    private int mHappyPoints = 0;

    public static final String TAG = KarmaActivity.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karma);

        //Setup the actionbar
        final ActionBar actionBar = getActionBar();
        actionBar.show();

        // Run a task to fetch the notifications count
        new FetchCountTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.star);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mHappyPoints);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.star) {
            // TODO: display unread notifications.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    Updates the count of notifications in the ActionBar.
     */
    private void updateNotificationsBadge(int count) {
        mHappyPoints = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }


    //Sample AsyncTask to fetch the notifications count

    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            // example count. This is where you'd query your data store for the actual count.
            //query the activities class to display the points based on done activities
            ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_ACTIVITIES);
            //check for current user with id
            query.whereEqualTo(ParseConstants.KEY_CURRENT_USER_ID, ParseUser.getCurrentUser().getObjectId());
            try {
                mHappyPoints = query.count();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Points : " + mHappyPoints);
            return mHappyPoints;
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }





    protected View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_karma,container,false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefreshLayout.setColorScheme(R.color.swipeRefresh1, R.color.swipeRefresh2, R.color.swipeRefresh3, R.color.swipeRefresh4);


        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();

        setProgressBarIndeterminateVisibility(true);

        retrieveActivityList();
    }

    private void retrieveActivityList(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_ACTIVITIES);
        //display list of activities for that user
        query.whereEqualTo(ParseConstants.KEY_CURRENT_USER_ID, ParseUser.getCurrentUser().getObjectId());

        //display most recent messages first
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> activities, ParseException e) {

                setProgressBarIndeterminateVisibility(false);

                //animation of refreshing stops after messages are refreshed
                //if(mSwipeRefreshLayout.isRefreshing()){
                //    mSwipeRefreshLayout.setRefreshing(false);
                //}

                if(e == null){
                    //we have activities to display
                    mActivities = activities;

                    String[] activitiesDone = new String[mActivities.size()];
                    int i=0;
                    for(ParseObject activity : mActivities){
                        activitiesDone[i] = activity.getString(ParseConstants.KEY_ACTIVITY_NAME);
                        i++;
                    }
                    if(getListView().getAdapter() == null) {
                        KarmaActivityAdapter adapter = new KarmaActivityAdapter(getListView().getContext(), mActivities);
                        setListAdapter(adapter);
                    }

                }
                else{
                    //refill the adapter
                    ((KarmaActivityAdapter)getListView().getAdapter()).refill(mActivities);
                }
            }
        });
        

    }

    protected SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            retrieveActivityList();
        }
    };

}
