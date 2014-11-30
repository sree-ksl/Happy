package com.example.Happy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.parse.ParseAnalytics;

public class HappyActivity extends Activity {

    protected TextView mTodayActivity;
    protected TextView mKarmaActivity;

    public static final String TAG = HappyActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ParseAnalytics.trackAppOpened(getIntent());

        //hide the action bar
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        mTodayActivity = (TextView)findViewById(R.id.todayAct);

        mTodayActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToday = new Intent(HappyActivity.this,TodayActivity.class);
                startActivity(intentToday);
            }
        });

        mKarmaActivity = (TextView)findViewById(R.id.karma);
        mKarmaActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKarma = new Intent(HappyActivity.this,KarmaActivity.class);
                startActivity(intentKarma);
            }
        });



    }
}
