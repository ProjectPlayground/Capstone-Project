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
import com.village.wannajoin.model.Member;
import com.village.wannajoin.util.Util;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by richa on 8/11/16.
 */
public class EventMembersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<Member> mEventMemberList;

    public EventMembersRecyclerViewAdapter (Context mContext, ArrayList<Member> memberList){
        this.mContext = mContext;
        mEventMemberList = memberList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.event_member_list, parent, false);
        EventMembersRecyclerViewAdapter.ViewHolder vh = new EventMembersRecyclerViewAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventMembersRecyclerViewAdapter.ViewHolder vh = (EventMembersRecyclerViewAdapter.ViewHolder) holder;
        vh.contactName.setText(Util.capitalizeWords(mEventMemberList.get(position).getName()));
        if (mEventMemberList.get(position).getPhotoUrl() == null) {
            vh.contactImageView
                    .setImageDrawable(ContextCompat
                            .getDrawable(mContext,
                                    R.drawable.ic_account_circle_black_48dp));
        } else {
            Glide.with(mContext)
                    .load(mEventMemberList.get(position).getPhotoUrl())
                    .into(vh.contactImageView);
        }

        if(mEventMemberList.get(position).getStatus().equals("1")){
            vh.memberStatus.setText(R.string.event_member_going);
        }else{
            vh.memberStatus.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mEventMemberList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView contactImageView;
        public TextView contactName;
        public TextView memberStatus;
        public ViewHolder(View view) {
            super(view);
            contactImageView = (CircleImageView) view.findViewById(R.id.contact_image_view);
            contactName = (TextView) view.findViewById(R.id.contact_name);
            memberStatus = (TextView) view.findViewById(R.id.member_status);
        }
    }
}
