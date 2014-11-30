package com.example.Happy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.parse.*;

/**
 * Created by hello on 25/08/14.
 */
public class TodayActivity extends Activity {

    TextView mActivityTodo;
    Button mPicButton;
    Button mNoButton;
    EditText mComments;

    private int mHappyPoints = 0;


    //public static final int TAKE_PHOTO_REQUEST = 0;
    //public static final int MEDIA_TYPE_IMAGE = 2;

    //protected Uri mMediaUri;

    public static final String TAG = TodayActivity.class.getSimpleName();

    //Constructs a new random activity object
    private RandomActivity mRandomActivity = new RandomActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        // Run a task to fetch the notifications count
        new FetchCountTask().execute();

        ParseAnalytics.trackAppOpened(getIntent());

        //Setup the actionbar
        final ActionBar actionBar = getActionBar();
        actionBar.show();

        mActivityTodo = (TextView) findViewById(R.id.activityTodo);
        mPicButton = (Button) findViewById(R.id.uploadButton);
        mNoButton = (Button) findViewById(R.id.notdoneButton);
        mComments = (EditText) findViewById(R.id.editNotes);

        //save random activity which we get from random generator to a string variable
        final String todoAct = mRandomActivity.getAnActivity();

        //Update the label with dynamic text
        mActivityTodo.setText(todoAct);


        //save happy activity of the day as done if yes is clicked
        mPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start camera
                //Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                //if(mMediaUri == null){
                //display an error
                //    Toast.makeText(TodayActivity.this,"There was a problem accessing your device's external storage.",Toast.LENGTH_LONG).show();
                //}
                //else {
                //    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                //    startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                //}

                //save happy activity of the day as done in background and increase happy point and save picture if clicked
                ParseObject doneActivity = new ParseObject(ParseConstants.CLASS_ACTIVITIES);
                doneActivity.put(ParseConstants.KEY_CURRENT_USER_ID, ParseUser.getCurrentUser().getObjectId());
                doneActivity.put(ParseConstants.KEY_ACTIVITY_NAME, todoAct);
                doneActivity.put(ParseConstants.KEY_NOTES, mComments.getText().toString());
                //doneActivity.put(ParseConstants.KEY_FILE_TYPE,mFileType);
                doneActivity.saveInBackground();

                //display current user id
                Log.d(TAG, "User's object id is: " + ParseUser.getCurrentUser().getObjectId());

                Intent listKarmaIntent = new Intent(TodayActivity.this, KarmaActivity.class);
                startActivity(listKarmaIntent);

                //Increase happy point

                //display toast message as saved

            }

            //private Uri getOutputMediaFileUri(int mediaType) {
            //check if sd card is available
            //    if(isExternalStorageAvailable()){
            //get uri

            //Get External storage directory
            //        String appName = TodayActivity.this.getString(R.string.app_name);
            //        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),appName);

            //create our own sub directory
            //        if(!mediaStorageDir.exists()){
            //            if(!mediaStorageDir.mkdirs()){
            //                Log.e(TAG,"Failed to create directory.");
            //                return null;
            //            }
            //        }

            //Create a file name
            //Create the actual file
            //        File mediaFile;
            //        Date now = new Date();
            //        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

            //        String path = mediaStorageDir.getPath() + File.separator;
            //        if(mediaType == MEDIA_TYPE_IMAGE){
            //            mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
            //        }
            //        else{
            //            return null;
            //        }

            //        Log.d(TAG,"File: " + Uri.fromFile(mediaFile));

            //return the file's uri
            //        return Uri.fromFile(mediaFile);
            //    }
            //    else{
            //        return null;
            //    }
            //}

            //private boolean isExternalStorageAvailable(){
            //    String state = Environment.getExternalStorageState();

            //    if(state.equals(Environment.MEDIA_MOUNTED)){
            //        return true;
            //    }
            //    else{
            //        return false;
            //   }
            //}
        });

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get back to main activity
                Intent intent = new Intent(TodayActivity.this, HappyActivity.class);
                startActivity(intent);
            }
        });

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
            Log.d(TAG,"Points : " +mHappyPoints);
            return mHappyPoints;
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }

        //@Override
        //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);

        //    if(resultCode == RESULT_OK){
        //Success!Add it to Gallery
        //        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        //        mediaScanIntent.setData(mMediaUri);
        //        sendBroadcast(mediaScanIntent);

        //    }

        //   else if(resultCode != RESULT_CANCELED){
        //        Toast.makeText(TodayActivity.this,"Sorry,there was an error!",Toast.LENGTH_LONG).show();
        //    }
        //}

}