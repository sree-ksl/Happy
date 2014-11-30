package com.example.Happy;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import com.parse.*;
import com.parse.PushService;

/**
 * Created by hello on 25/08/14.
 */
public class HappyApplication extends Application {

   public static final String TAG = HappyApplication.class.getSimpleName();

    @Override
    public void onCreate() {

        Parse.initialize(this, "TkTLzcVpIrx2LLoZYbSVPabE9ROK3GCxruT5JM4a", "SIGh0l1kfvXGlGchpQnXkOp2wIEOEdnLAQJHIco2");
        PushService.setDefaultPushCallback(this, TodayActivity.class);
        //PushService.setDefaultPushCallback(this,HappyActivity.class, R.drawable.icon);  //this is used when you set custom icon for push notification.
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //First test  object
        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("foo", "bar");
        //testObject.saveInBackground();

        //create users without log in credentials-anonymous user
        ParseUser.enableAutomaticUser();
        ParseUser.getCurrentUser().increment("RunCount");
        ParseUser.getCurrentUser().saveInBackground();

        //to set current user
        String sessionToken = ParseUser.getCurrentUser().getSessionToken();

        Log.d(TAG,"Session Token is: " + ParseUser.getCurrentUser().getSessionToken());

        ParseUser.becomeInBackground(sessionToken, new LogInCallback(){
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // The current user is now set to user.
                    user = ParseUser.getCurrentUser();
                } else {
                    // The token could not be validated.
                    Toast.makeText(HappyApplication.this,"Error!",Toast.LENGTH_LONG).show();
                }
            }
        });


        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access while disabling public write access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

    public static void updateParseInstallation(ParseUser user){

       ParseInstallation installation = ParseInstallation.getCurrentInstallation();
       installation.put(ParseConstants.KEY_USER_ID, user.getObjectId());
       //save this installation to parse
       installation.saveInBackground();

    }


}
