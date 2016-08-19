package com.village.wannajoin.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Member;
import com.village.wannajoin.util.Util;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by richa on 8/18/16.
 */
public class GroupDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<Member> mGroupMemberList;

    public GroupDetailRecyclerViewAdapter (Context mContext, ArrayList<Member> memberList){
        this.mContext = mContext;
        mGroupMemberList = memberList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.group_member_list, parent, false);
        GroupDetailRecyclerViewAdapter.ViewHolder vh = new GroupDetailRecyclerViewAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GroupDetailRecyclerViewAdapter.ViewHolder vh = (GroupDetailRecyclerViewAdapter.ViewHolder)holder;
        vh.contactName.setText(Util.capitalizeWords(mGroupMemberList.get(position).getName()));
        vh.contactName.setTextSize(mContext.getResources().getDimension(R.dimen.primary_text_size));
        if (mGroupMemberList.get(position).getPhotoUrl() == null) {
            vh.contactImageView
                    .setImageDrawable(ContextCompat
                            .getDrawable(mContext,
                                    R.drawable.ic_account_circle_black_48dp));
        } else {
            Glide.with(mContext)
                    .load(mGroupMemberList.get(position).getPhotoUrl())
                    .into(vh.contactImageView);
        }

        vh.removeMember.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mGroupMemberList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView contactImageView;
        public TextView contactName;
        public ImageButton removeMember;
        public ViewHolder(View view) {
            super(view);
            contactImageView = (CircleImageView) view.findViewById(R.id.user_image);
            contactName = (TextView) view.findViewById(R.id.user_name);
            removeMember = (ImageButton) view.findViewById(R.id.remove_user);
        }
    }
}
