package com.village.wannajoin.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.village.wannajoin.R;
import com.village.wannajoin.model.ContactAndGroup;
import com.village.wannajoin.model.Member;
import com.village.wannajoin.util.ArrayFirebase;
import com.village.wannajoin.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupDetailActivity extends AppCompatActivity implements GroupDetailRecyclerViewAdapter.ViewClickedListener {

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
        mAdapter = new GroupDetailRecyclerViewAdapter(this, mGroupMemberList, this);
        mSnapshotsGroupMembers.setOnChangedListener(new ArrayFirebase.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                switch (type) {
                    case Added:
                        Member member = mSnapshotsGroupMembers.getItem(index).getValue(Member.class);
                        mGroupMemberList.add(index,member);
                        mAdapter.notifyItemInserted(index);
                        break;
                    case Changed:
                        Member member1 = mSnapshotsGroupMembers.getItem(index).getValue(Member.class);
                        mGroupMemberList.set(index,member1);
                        mAdapter.notifyItemChanged(index);
                        break;
                    case Removed:
                        mGroupMemberList.remove(index);
                        mAdapter.notifyItemRemoved(index);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id ==android.R.id.home){
            supportFinishAfterTransition();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/" + Constants.FIREBASE_LOCATION_GROUPS + "/" + mContactAndGroup.getId(),null);
            childUpdates.put("/"+Constants.FIREBASE_LOCATION_GROUP_MEMBERS+"/"+mContactAndGroup.getId(),null);
            dbref.updateChildren(childUpdates);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            }else{
                finish();
            }
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSnapshotsGroupMembers.cleanup();
    }

    @Override
    public void onItemClicked(Member member) {
        //show contact detail dialog fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("contactdetail");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = ContactDetailDialogFragment.newInstance(member.getUserId(),member.getName(),member.getPhotoUrl());
        newFragment.show(ft, "contactdetail");
    }
}
