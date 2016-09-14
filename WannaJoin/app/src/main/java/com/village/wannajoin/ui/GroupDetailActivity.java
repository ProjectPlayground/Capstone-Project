package com.village.wannajoin.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.village.wannajoin.R;
import com.village.wannajoin.model.ContactAndGroup;
import com.village.wannajoin.util.Constants;


public class GroupDetailActivity extends AppCompatActivity {

    ContactAndGroup mContactAndGroup;
    private static final String TAG_DETAIL_VIEW_FRAGMENT = "detail_view_fragment";
    private GroupDetailFragment mGroupDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        mContactAndGroup = getIntent().getParcelableExtra(Constants.GROUP);
        FragmentManager fm = getSupportFragmentManager();
        mGroupDetailFragment  = (GroupDetailFragment)fm.findFragmentByTag(TAG_DETAIL_VIEW_FRAGMENT);
        if (mGroupDetailFragment ==null) {
            mGroupDetailFragment = new GroupDetailFragment();
            Bundle args = new Bundle();
            args.putParcelable(Constants.GROUP, mContactAndGroup);
            mGroupDetailFragment.setArguments(args);

            fm.beginTransaction().add(R.id.group_detail_activity_container, mGroupDetailFragment, TAG_DETAIL_VIEW_FRAGMENT).commit();
        }
    }




}
