package com.village.wannajoin.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.village.wannajoin.R;

public class ShareEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.share_event_contacts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
