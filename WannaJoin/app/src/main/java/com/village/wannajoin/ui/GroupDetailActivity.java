package com.village.wannajoin.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.village.wannajoin.R;
import com.village.wannajoin.model.ContactAndGroup;
import com.village.wannajoin.util.Constants;


public class GroupDetailActivity extends AppCompatActivity {

    ContactAndGroup mContactAndGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        mContactAndGroup = getIntent().getParcelableExtra(Constants.GROUP);
        GroupDetailFragment groupDetailFragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.GROUP,mContactAndGroup);
        groupDetailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.group_detail_activity_container, groupDetailFragment).commit();

    }




}
