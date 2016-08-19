package com.village.wannajoin.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.village.wannajoin.R;
import com.village.wannajoin.model.ContactAndGroup;
import com.village.wannajoin.model.Member;
import com.village.wannajoin.util.ArrayFirebase;
import com.village.wannajoin.util.Constants;

import java.util.ArrayList;

public class GroupDetailActivity extends AppCompatActivity {

    ContactAndGroup mContactAndGroup;
    Query mGroupMemberRef;
    ArrayFirebase mSnapshotsGroupMembers;
    ArrayList<Member> mGroupMemberList;
    GroupDetailRecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContactAndGroup = getIntent().getParcelableExtra(Constants.GROUP);
        getSupportActionBar().setTitle(mContactAndGroup.getName());
        mGroupMemberList = new ArrayList<>();
        mGroupMemberRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_GROUP_MEMBERS).child(mContactAndGroup.getId()).orderByChild("name");
        mSnapshotsGroupMembers = new ArrayFirebase(mGroupMemberRef);
        mAdapter = new GroupDetailRecyclerViewAdapter(this, mGroupMemberList);
        mSnapshotsGroupMembers.setOnChangedListener(new ArrayFirebase.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                switch (type) {
                    case Added:
                        Member member = mSnapshotsGroupMembers.getItem(index).getValue(Member.class);
                        mGroupMemberList.add(index,member);
                        //mContactsRecyclerViewAdapter.notifyItemInserted(index);
                        break;
                    case Changed:
                        Member member1 = mSnapshotsGroupMembers.getItem(index).getValue(Member.class);
                        mGroupMemberList.set(index,member1);
                        //mContactsRecyclerViewAdapter.notifyItemChanged(posc);
                        break;
                    case Removed:
                        mGroupMemberList.remove(index);
                        //mContactsRecyclerViewAdapter.notifyItemRemoved(posr);
                        break;
                    case Moved:
                        //notifyItemMoved(oldIndex, index);
                        break;
                    default:
                        throw new IllegalStateException("Incomplete case statement");
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.group_member_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

}
