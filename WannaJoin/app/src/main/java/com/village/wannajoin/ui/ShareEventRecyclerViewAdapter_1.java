package com.village.wannajoin.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.village.wannajoin.R;
import com.village.wannajoin.model.ContactAndGroup;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by richa on 6/20/16.
 */
public class ShareEventRecyclerViewAdapter_1 extends RecyclerView.Adapter<ShareEventRecyclerViewAdapter_1.ViewHolder> {
    ArrayList<ContactAndGroup> contactList;
    Context mContext;
    public ShareEventRecyclerViewAdapter_1(Context context, ArrayList<ContactAndGroup> list){
        contactList = list;
        mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.share_contact_list_item, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactList.get(vh.getAdapterPosition()).setSelected(true);
                Collections.sort(contactList);
                notifyDataSetChanged();
            }
        });

        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (contactList.get(position).isSelected())
            holder.itemView.setBackgroundColor(Color.GREEN);
        holder.cgName.setText(contactList.get(position).getName());
        if (contactList.get(position).getPhotoUrl() == null) {
            holder.cgImageView
                    .setImageDrawable(ContextCompat
                            .getDrawable(mContext,
                                    R.drawable.ic_account_circle_black_48dp));
        } else {
            Glide.with(mContext)
                    .load(contactList.get(position).getPhotoUrl())
                    .into(holder.cgImageView);
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView cgImageView;
        public TextView cgName;
        public ViewHolder(View itemView) {
            super(itemView);
            cgImageView = (CircleImageView) itemView.findViewById(R.id.cg_image_view);
            cgName = (TextView) itemView.findViewById(R.id.cg_name);
        }
    }
}
