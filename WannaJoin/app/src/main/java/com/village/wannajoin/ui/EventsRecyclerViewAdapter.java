package com.village.wannajoin.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Event;
import com.village.wannajoin.util.Constants;
import com.village.wannajoin.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by richa on 7/14/16.
 */
public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<Event> mEventList;
    OnClickedListener mListener;
    FirebaseUser firebaseUser ;

    interface OnClickedListener {
        void onItemClicked(Event event);
    }

    public EventsRecyclerViewAdapter(Context mContext, ArrayList<Event> mEventList, OnClickedListener mListener) {
        this.mContext = mContext;
        this.mEventList = mEventList;
        this.mListener = mListener;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (viewType ==-1){
            view = layoutInflater.inflate(R.layout.empty_recycler_view, parent, false);
            EmptyViewHolder vh = new EmptyViewHolder(view, new EmptyViewHolder.EmptyViewHolderClicks() {
                @Override
                public void onViewClicked() {
                    mListener.onItemClicked(null);

                }
            });
            return vh;
        }else {
            view = layoutInflater.inflate(R.layout.fragment_event, parent, false);
            EventsRecyclerViewAdapter.ViewHolder vh = new EventsRecyclerViewAdapter.ViewHolder(view, new ViewHolder.ViewHolderClicks() {
                @Override
                public void onJoin(int pos) {
                    String eventId = mEventList.get(pos).getEventId();

                    DatabaseReference dbrefJoin = FirebaseDatabase.getInstance().getReference();
                    String status = mEventList.get(pos).getEventMembers().get(firebaseUser.getUid());
                    String newStatus=null;
                    String newStatus1 = null;
                    String[] statusArray = status.split("-");
                    if (statusArray[1].equals("0")){
                        newStatus = mEventList.get(pos).getFromTime()+"-1";
                        newStatus1 = "1";
                    }else{
                        newStatus = mEventList.get(pos).getFromTime()+"-0";
                        newStatus1 = "0";
                    }
                    Map<String, Object> childUpdates = new HashMap<>();
                    //dbrefJoin.setValue(newStatus);
                    childUpdates.put("/"+Constants.FIREBASE_LOCATION_EVENTS+"/"+eventId+"/"+Constants.FIREBASE_LOCATION_EVENT_MEMBERS+"/"+firebaseUser.getUid(),newStatus);
                    childUpdates.put("/"+Constants.FIREBASE_LOCATION_EVENT_MEMBERS+"/"+eventId+"/"+Constants.FIREBASE_LOCATION_USERS+"/"+firebaseUser.getUid()+"/"+Constants.FIREBASE_LOCATION_MEMBER_STATUS,newStatus1);
                    dbrefJoin.updateChildren(childUpdates);
                }

                @Override
                public void onDetail(int pos) {
                    mListener.onItemClicked(mEventList.get(pos));
                }
            });
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==-1){
            EventsRecyclerViewAdapter.EmptyViewHolder evh = (EventsRecyclerViewAdapter.EmptyViewHolder)holder;
            evh.defaultText.setText(mEventList.get(position).getTitle());
            evh.itemView.setContentDescription(mEventList.get(position).getTitle());
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
            String status = mEventList.get(position).getEventMembers().get(firebaseUser.getUid());
            String[] statusarray = status.split("-");
            if (statusarray[1].equals("1"))
                viewHolder.mJoinButton.setText(R.string.cancel_button_text);
            else
                viewHolder.mJoinButton.setText(R.string.join_button_text);
            String dateString = Util.getDateFromTimeStamp(mEventList.get(position).getFromTime());
            String timeString = Util.getTimeFromTimeStamp(mEventList.get(position).getFromTime());
            viewHolder.mDate.setText(dateString);
            viewHolder.mTime.setText(timeString);
            int count=0;
            for(String s:mEventList.get(position).getEventMembers().values()){
                if (s!=null) {
                    String[] sa = s.split("-");
                    if (sa[1].equals("1"))
                        count++;
                }
            }
            viewHolder.mPeople.setText("+"+count+ " "+mContext.getString(R.string.event_member_going));
            if (mEventList.get(position).getLocation().equals("")){
                if (count>1)
                    viewHolder.itemView.setContentDescription(mContext.getString(R.string.event_list_item_content_description2,owner,mEventList.get(position).getTitle(),dateString,timeString,count));
                else
                    viewHolder.itemView.setContentDescription(mContext.getString(R.string.event_list_item_content_description1,owner,mEventList.get(position).getTitle(),dateString,timeString,count));
            }else {
                if (count > 1)
                    viewHolder.itemView.setContentDescription(mContext.getString(R.string.event_list_item_content_description2l, owner, mEventList.get(position).getTitle(), dateString, timeString, mEventList.get(position).getLocation(), count));
                else
                    viewHolder.itemView.setContentDescription(mContext.getString(R.string.event_list_item_content_description1l, owner, mEventList.get(position).getTitle(), dateString, timeString, mEventList.get(position).getLocation(), count));
            }
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ViewHolderClicks mClickListener;
        public TextView mOwner;
        public TextView mTitleView;
        public TextView mLocationView;
        public TextView mPeople;
        public CircleImageView messengerImageView;
        public TextView mDate;
        public TextView mTime;
        public Button mJoinButton;
        public ViewHolder(View view, ViewHolderClicks clickListener) {
            super(view);

            mOwner = (TextView) view.findViewById(R.id.owner);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mLocationView = (TextView) view.findViewById(R.id.location);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
            mDate = (TextView) view.findViewById(R.id.date);
            mTime = (TextView) view.findViewById(R.id.time);
            mPeople = (TextView) view.findViewById(R.id.people_going);
            mJoinButton = (Button) view.findViewById(R.id.join_button);
            mJoinButton.setOnClickListener(this);
            view.setOnClickListener(this);
            mClickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            if (v instanceof Button){
                mClickListener.onJoin(getAdapterPosition());
            } else {
                mClickListener.onDetail(getAdapterPosition());
            }
        }
        public interface ViewHolderClicks {
            void onJoin(int pos);
            void onDetail(int pos);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView defaultText;
        public EmptyViewHolderClicks mEVClickListener;
        public EmptyViewHolder(View view, EmptyViewHolderClicks mEVClickListener) {
            super(view);
            defaultText = (TextView) view.findViewById(R.id.empty_view);
            this.mEVClickListener = mEVClickListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mEVClickListener.onViewClicked();
        }

        public interface EmptyViewHolderClicks {
            void onViewClicked();
        }
    }

}
