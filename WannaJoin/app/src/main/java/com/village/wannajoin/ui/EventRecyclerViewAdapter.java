package com.village.wannajoin.ui;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Event;
import com.village.wannajoin.util.Util;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by richa on 3/31/16.
 */
public class EventRecyclerViewAdapter extends FirebaseRecyclerAdapter<Event, EventRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    public EventRecyclerViewAdapter(Class<Event> modelClass, int modelLayout, Class<ViewHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Event event, int i) {

        viewHolder.mTitleView.setText(event.getTitle());
       // viewHolder.mTitleTemp.setText(mContext.getString(R.string.text_after_event_title));
        viewHolder.mLocationView.setText(event.getLocation());
        String owner = event.getOwnerName();
        //viewHolder.mOwner.setText(owner.substring(0,1).toUpperCase()+owner.substring(1));
        viewHolder.mOwner.setText(Util.capitalizeWords(owner));
        //viewHolder.mOwnerTemp.setText(mContext.getString(R.string.text_after_event_owner));
        if (event.getOwnerPhotoUrl() == null) {
            viewHolder.messengerImageView
                    .setImageDrawable(ContextCompat
                            .getDrawable(mContext,
                                    R.drawable.ic_account_circle_black_48dp));
        } else {
            Glide.with(mContext)
                    .load(event.getOwnerPhotoUrl())
                    .into(viewHolder.messengerImageView);
        }
        viewHolder.mDate.setText(Util.getDateFromTimeStamp(event.getFromTime()));
        viewHolder.mTime.setText(Util.getTimeFromTimeStamp(event.getFromTime()));
        viewHolder.mPeople.setText("+5 going");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mOwner;
        //public TextView mOwnerTemp;
        public TextView mTitleView;
        //public TextView mTitleTemp;
        public TextView mLocationView;
        public TextView mPeople;
        public CircleImageView messengerImageView;
        public TextView mDate;
        public TextView mTime;
        public ViewHolder(View view) {
            super(view);
            mOwner = (TextView) view.findViewById(R.id.owner);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mLocationView = (TextView) view.findViewById(R.id.location);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
            mDate = (TextView) view.findViewById(R.id.date);
            mTime = (TextView) view.findViewById(R.id.time);
           // mOwnerTemp = (TextView) view.findViewById(R.id.owner_temp);
           // mTitleTemp = (TextView) view.findViewById(R.id.title_temp);
            mPeople = (TextView) view.findViewById(R.id.people_going);
        }
    }
}
