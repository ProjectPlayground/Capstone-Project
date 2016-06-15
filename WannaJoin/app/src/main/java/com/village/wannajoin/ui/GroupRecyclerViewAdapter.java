package com.village.wannajoin.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.village.wannajoin.model.Group;

/**
 * Created by richa on 6/15/16.
 */
public class GroupRecyclerViewAdapter extends FirebaseRecyclerAdapter<Group, GroupRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    public GroupRecyclerViewAdapter(Class<Group> modelClass, int modelLayout, Class<ViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Group model, int position) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View view) {
            super(view);
        }
    }
}
