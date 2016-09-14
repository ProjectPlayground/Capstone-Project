package com.village.wannajoin.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Group;
import com.village.wannajoin.model.Member;
import com.village.wannajoin.model.User;
import com.village.wannajoin.util.Util;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by richa on 6/13/16.
 */
public class GroupMemberAdapter extends BaseAdapter{
    ArrayList<Member> members;
    Context mContext;
    Group mGroup;

    public GroupMemberAdapter(ArrayList<Member> members, Context mContext, Group group) {
        this.members = members;
        this.mContext = mContext;
        this.mGroup = group;
    }

    public void updateAdapter(ArrayList<Member> members){
        this.members = members;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_member_list,null);
            holder.memberImageView = (CircleImageView)convertView.findViewById(R.id.user_image);
            holder.memberNameTV = (TextView) convertView.findViewById(R.id.user_name);
            holder.removeMember = (ImageButton) convertView.findViewById(R.id.remove_user);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.memberNameTV.setText(Util.capitalizeWords(members.get(position).getName()));
        if (members.get(position).getPhotoUrl() == null) {
            holder.memberImageView
                    .setImageDrawable(ContextCompat
                            .getDrawable(mContext,
                                    R.drawable.ic_account_circle_black_48dp));
        } else {
            Glide.with(mContext)
                    .load(members.get(position).getPhotoUrl())
                    .into(holder.memberImageView);
        }

        if (mGroup != null) {
            if (mGroup.getGroupOwner().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                holder.removeMember.setVisibility(View.VISIBLE);
                holder.removeMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        members.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
            else{
                holder.removeMember.setVisibility(View.GONE);
            }
        }else {
            holder.removeMember.setVisibility(View.VISIBLE);
            holder.removeMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    members.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

    public class ViewHolder{
        public CircleImageView memberImageView;
        public TextView memberNameTV;
        public ImageButton removeMember;
    }
}
