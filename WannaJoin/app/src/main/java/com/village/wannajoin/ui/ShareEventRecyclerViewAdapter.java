package com.village.wannajoin.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by richa on 6/20/16.
 */
public class ShareEventRecyclerViewAdapter extends RecyclerView.Adapter<ShareEventRecyclerViewAdapter.ViewHolder> {

    public ShareEventRecyclerViewAdapter(){

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
