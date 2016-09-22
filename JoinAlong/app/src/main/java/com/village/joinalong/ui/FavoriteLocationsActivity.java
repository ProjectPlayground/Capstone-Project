package com.village.joinalong.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.village.joinalong.FavoriteEventLocationData.EventLocationLoader;
import com.village.joinalong.R;
import com.village.joinalong.util.DividerItemDecoration;

public class FavoriteLocationsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private FavoriteLocationsRecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_locations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.fav_location_recycler_view);
        mEmptyView = (TextView) findViewById(R.id.fav_location_empty_view);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return EventLocationLoader.newAllLocationInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data !=null) {
            if (data.getCount()>0) {
                mEmptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter = new FavoriteLocationsRecyclerViewAdapter(data, this);
                mAdapter.setHasStableIds(true);
                mRecyclerView.setAdapter(mAdapter);
                int columnCount = getResources().getInteger(R.integer.list_column_count);
                StaggeredGridLayoutManager sglm =
                        new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(sglm);
                mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

            }else{
                mEmptyView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }else{
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mRecyclerView.setAdapter(null);
    }
}
