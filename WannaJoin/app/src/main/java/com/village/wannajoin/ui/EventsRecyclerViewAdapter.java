package com.village.wannajoin.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Event;
import com.village.wannajoin.util.Util;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by richa on 7/14/16.
 */
public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<Event> mEventList;
    EmptyViewClickedListener mListner;

    interface EmptyViewClickedListener {
        void onItemClicked(int type);
    }

    public EventsRecyclerViewAdapter(Context mContext, ArrayList<Event> mEventList, EmptyViewClickedListener mListner) {
        this.mContext = mContext;
        this.mEventList = mEventList;
        this.mListner = mListner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (viewType ==-1){
            view = layoutInflater.inflate(R.layout.empty_recycler_view, parent, false);
            EmptyViewHolder vh = new EmptyViewHolder(view);
            return vh;
        }else {
            view = layoutInflater.inflate(R.layout.fragment_event, parent, false);
            EventsRecyclerViewAdapter.ViewHolder vh = new EventsRecyclerViewAdapter.ViewHolder(view);
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==-1){
            EventsRecyclerViewAdapter.EmptyViewHolder evh = (EventsRecyclerViewAdapter.EmptyViewHolder)holder;
            evh.defaultText.setText(mEventList.get(position).getTitle());
            evh.defaultText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        mListner.onItemClicked(1);

                }
            });
        }else {

            EventsRecyclerViewAdapter.ViewHolder viewHolder = (EventsRecyclerViewAdapter.ViewHolder) holder;
            viewHolder.mTitleView.setText(mEventList.get(position).getTitle());
            // viewHolder.mTitleTemp.setText(mContext.getString(R.string.text_after_event_title));
            viewHolder.mLocationView.setText(mEventList.get(position).getLocation());
            String owner = mEventList.get(position).getOwnerName();
            //viewHolder.mOwner.setText(owner.substring(0,1).toUpperCase()+owner.substring(1));
            viewHolder.mOwner.setText(Util.capitalizeWords(owner));
            //viewHolder.mOwnerTemp.setText(mContext.getString(R.string.text_after_event_owner));
            if (mEventList.get(position).getOwnerPhotoUrl() == null) {
                viewHolder.messengerImageView
                        .setImageDrawable(ContextCompat
                                .getDrawable(mContext,
                                        R.drawable.ic_account_circle_black_48dp));
            } else {
                Glide.with(mContext)
                        .load(mEventList.get(position).getOwnerPhotoUrl())
                        .into(viewHolder.messengerImageView);
            }
            viewHolder.mDate.setText(Util.getDateFromTimeStamp(mEventList.get(position).getFromTime()));
            viewHolder.mTime.setText(Util.getTimeFromTimeStamp(mEventList.get(position).getFromTime()));
            viewHolder.mPeople.setText("+5 going");
        }
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mEventList.get(position).getType();
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

    public static class EmptyViewHolder extends RecyclerView.ViewHolder{
        public TextView defaultText;
        public EmptyViewHolder(View view) {
            super(view);
            defaultText = (TextView) view.findViewById(R.id.empty_view);
        }
    }

}
