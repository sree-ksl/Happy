package com.example.Happy;

import android.util.Log;

import java.util.Random;

/**
 * Created by hello on 29/08/14.
 */
public class RandomActivity {

    public String getAnActivity(){
        //update the activity label
        String happyAct = "";

        //Randomly select one of the happy activities
        //Instantiating the object means constructor (construct a new Random generator variable)
        Random randomGenerator = new Random();
        // use the no of elements in the array as the limit if random number generator
        int randomNumber = randomGenerator.nextInt(ActivityArray.mHappyActs.length);

        //prints the random number to debugger
        Log.d("Random Number", "Number is: " + randomNumber);

        //convert the random numbers to text
        happyAct = ActivityArray.mHappyActs[randomNumber];

        return happyAct;
    }

}
