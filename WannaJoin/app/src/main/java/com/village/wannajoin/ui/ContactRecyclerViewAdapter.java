package com.village.wannajoin.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Member;
import com.village.wannajoin.util.Util;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by richa on 6/16/16.
 */
public class ContactRecyclerViewAdapter extends FirebaseRecyclerAdapter<Member, ContactRecyclerViewAdapter.ViewHolder> {

    private Context mContext;

    public ContactRecyclerViewAdapter(Class<Member> modelClass, int modelLayout, Class<ViewHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
    }
    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Member model, int position) {
        viewHolder.contactName.setText(Util.capitalizeWords(model.getName()));
        if (model.getPhotoUrl() == null) {
            viewHolder.contactImageView
                    .setImageDrawable(ContextCompat
                            .getDrawable(mContext,
                                    R.drawable.ic_account_circle_black_48dp));
        } else {
            Glide.with(mContext)
                    .load(model.getPhotoUrl())
                    .into(viewHolder.contactImageView);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView contactImageView;
        public TextView contactName;
        public ViewHolder(View view) {
            super(view);
            contactImageView = (CircleImageView) view.findViewById(R.id.contact_image_view);
            contactName = (TextView) view.findViewById(R.id.contact_name);
        }
    }
}
