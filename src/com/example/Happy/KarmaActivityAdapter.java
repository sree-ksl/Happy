package com.example.Happy;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

/**
 * Created by hello on 31/08/14.
 */
public class KarmaActivityAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mActivities;

    public KarmaActivityAdapter(Context context, List<ParseObject> activities) {
        super(context, R.layout.message_item ,activities);
        mContext = context;
        mActivities = activities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder;

        if(convertView == null){
            //Inflate convertView from the layout file and using a context and return it to the list
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            //Initialise holder as a new view holder
            holder = new ViewHolder();
            //Initialise image view and text view inside it
            //holder.iconImageView = (ImageView) convertView.findViewById(R.id.messageIcon);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.senderLabel);
            holder.timeLabel = (TextView) convertView.findViewById(R.id.timeLabel);
            convertView.setTag(holder); //to make the list scroll correctly
        }else{
            holder = (ViewHolder)convertView.getTag(); //gets the viewHolder that was already created
        }

        ParseObject activity = mActivities.get(position);

        //get the time of the message
        Date createdAt = activity.getCreatedAt();
        long now = new Date().getTime();
        String convertedDate = DateUtils.getRelativeTimeSpanString(createdAt.getTime(), now, DateUtils.SECOND_IN_MILLIS).toString();
        holder.timeLabel.setText(convertedDate);

        //check if message file is image or video
        //if(activity.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)) {
            //holder.iconImageView.setImageResource(R.drawable.ic_picture);
        //}else{
        //    holder.iconImageView.setImageResource(R.drawable.ic_video);
        //}
        holder.nameLabel.setText(activity.getString(ParseConstants.KEY_ACTIVITY_NAME));

        return convertView;
    }

    public static class ViewHolder{
        //includes data that's to be displayed in the custom layout
        //ImageView iconImageView;
        TextView nameLabel;
        TextView timeLabel;
    }

    //refill the adapter
    public void refill(List<ParseObject> activities){
        //clear the current data
        mActivities.clear();
        //add all the new ones
        mActivities.addAll(activities);
        //call a method
        notifyDataSetChanged();
    }
}
