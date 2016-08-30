package com.village.wannajoin.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.village.wannajoin.FavoriteEventLocationData.EventLocationContract;
import com.village.wannajoin.FavoriteEventLocationData.EventLocationLoader;
import com.village.wannajoin.FavoriteEventLocationData.EventLocationQueryHandler;
import com.village.wannajoin.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by richa on 8/29/16.
 */
public class FavoriteLocationsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public FavoriteLocationsRecyclerViewAdapter (Cursor cursor, Context context){
        mCursor = cursor;
        mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.favorite_locations_item, parent, false);
        final ViewHolder vh = new ViewHolder(view, new ViewHolder.ViewHolderClicks() {
            @Override
            public void onClick(int pos) {
                mCursor.moveToPosition(pos);
                removeLocationFromDatabase(mCursor.getString(EventLocationLoader.Query.COLUMN_LOCATION_ID));
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ((mCursor != null) && (!mCursor.isClosed())) {
            if (mCursor.moveToPosition(position)) {
                FavoriteLocationsRecyclerViewAdapter.ViewHolder vh = (FavoriteLocationsRecyclerViewAdapter.ViewHolder) holder;
                vh.favLocationTV.setText(mCursor.getString(EventLocationLoader.Query.COLUMN_LOCATION_ADDRESS));
            }
        }
    }

    @Override
    public int getItemCount() {
        if ((mCursor != null) && (!mCursor.isClosed())){
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if ((mCursor != null) && (!mCursor.isClosed())){
            if (mCursor.moveToPosition(position)){
                return mCursor.getLong(EventLocationLoader.Query._ID);
            }

        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ViewHolderClicks mClickListener;
        public TextView favLocationTV;
        public ImageButton removeLocationButton;
        public ViewHolder(View view, ViewHolderClicks clickListener) {
            super(view);
            mClickListener = clickListener;
            favLocationTV= (TextView) view.findViewById(R.id.fav_location);
            removeLocationButton = (ImageButton) view.findViewById(R.id.remove_location);
            removeLocationButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onClick(getAdapterPosition());
        }
        public interface ViewHolderClicks {
            void onClick(int pos);
        }

    }

    public void removeLocationFromDatabase(String locationId){
        ContentResolver contentResolver = mContext.getContentResolver();
        EventLocationQueryHandler locationHandler = new EventLocationQueryHandler(contentResolver, mContext);

        String where = EventLocationContract.LocationEntry.COLUMN_LOCATION_ID + "="+ "\""+locationId+"\"";

        locationHandler.startDelete(0,null,EventLocationContract.LocationEntry.CONTENT_URI,where,null);

    }
}
