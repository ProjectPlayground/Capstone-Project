package com.village.wannajoin.wannajoin.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.village.wannajoin.wannajoin.R;
import com.village.wannajoin.wannajoin.model.Event;

/**
 * Created by richa on 3/31/16.
 */
public class EventRecyclerViewAdapter extends FirebaseRecyclerAdapter<Event, EventRecyclerViewAdapter.ViewHolder> {


    public EventRecyclerViewAdapter(Class<Event> modelClass, int modelLayout, Class<ViewHolder> viewHolderClass, Firebase ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Event event, int i) {

        viewHolder.mTitleView.setText(event.getTitle());
        viewHolder.mLocationView.setText(event.getLocation());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView mTitleView;
        public final TextView mLocationView;
        public ViewHolder(View view) {
            super(view);

            mTitleView = (TextView) view.findViewById(R.id.title);
            mLocationView = (TextView) view.findViewById(R.id.location);
        }
    }
}
