package com.village.joinalong.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.village.joinalong.R;
import com.village.joinalong.model.Group;
import com.village.joinalong.util.Constants;
import com.village.joinalong.model.ContactAndGroup;
import com.village.joinalong.model.Member;
import com.village.joinalong.util.ArrayFirebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GroupDetailFragment extends Fragment implements GroupDetailRecyclerViewAdapter.ViewClickedListener {

    ContactAndGroup mContactAndGroup;
    Query mGroupMemberRef;
    ArrayFirebase mSnapshotsGroupMembers;
    ArrayList<Member> mGroupMemberList;
    GroupDetailRecyclerViewAdapter mAdapter;
    Group mGroup;

    public GroupDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContactAndGroup = getArguments().getParcelable(Constants.GROUP);
        }


        mGroupMemberList = new ArrayList<>();
        mGroupMemberRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_GROUP_MEMBERS).child(mContactAndGroup.getId()).orderByChild("name");
        mSnapshotsGroupMembers = new ArrayFirebase(mGroupMemberRef);
        mAdapter = new GroupDetailRecyclerViewAdapter(getContext(), mGroupMemberList, this);
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
                        throw new IllegalStateException(getString(R.string.snapshots_incomplete_case_error));
                }
            }
        });

        if (getActivity().getClass().getSimpleName().equals("GroupDetailActivity")) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_detail, container, false);
        Log.d("RB",getActivity().getClass().getSimpleName());
        if (getActivity().getClass().getSimpleName().equals("GroupDetailActivity")) {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mContactAndGroup.getName());
        }else{
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            toolbar.setTitle(mContactAndGroup.getName());
            toolbar.inflateMenu(R.menu.menu_group_detail);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.action_delete) {
                        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/" + Constants.FIREBASE_LOCATION_GROUPS + "/" + mContactAndGroup.getId(),null);
                        childUpdates.put("/"+Constants.FIREBASE_LOCATION_GROUP_MEMBERS+"/"+mContactAndGroup.getId(),null);
                        dbref.updateChildren(childUpdates);
                        getParentFragment().getChildFragmentManager().beginTransaction().remove(GroupDetailFragment.this).commit();
                    }
                    return true;
                }
            });
        }

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_GROUPS).child(mContactAndGroup.getId());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGroup = dataSnapshot.getValue(Group.class);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ImageView backdropImage = (ImageView)view.findViewById(R.id.backdrop);
        backdropImage.setContentDescription(mContactAndGroup.getName());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.group_member_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onItemClicked(Member member) {
        //show contact detail dialog fragment
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getChildFragmentManager().findFragmentByTag("contactdetail");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = ContactDetailDialogFragment.newInstance(member.getUserId(),member.getName(),member.getPhotoUrl());
        newFragment.show(ft, "contactdetail");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSnapshotsGroupMembers.cleanup();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_group_detail, menu);
    }

   @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem itemDelete = menu.findItem(R.id.action_delete);
        MenuItem itemEdit = menu.findItem(R.id.action_edit);
        if (mGroup !=null) {
           if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(mGroup.getGroupOwner())) {
               itemDelete.setVisible(true);
           } else {
               itemDelete.setVisible(false);
           }
            itemEdit.setVisible(true);
        }else{
           itemDelete.setVisible(false);
           itemEdit.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id ==android.R.id.home){
            getActivity().supportFinishAfterTransition();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/" + Constants.FIREBASE_LOCATION_GROUPS + "/" + mContactAndGroup.getId(),null);
            childUpdates.put("/"+Constants.FIREBASE_LOCATION_GROUP_MEMBERS+"/"+mContactAndGroup.getId(),null);
            dbref.updateChildren(childUpdates);
            if (getActivity().getClass().getSimpleName().equals("GroupDetailActivity")) {
                getActivity().supportFinishAfterTransition();
            }else{
                getParentFragment().getChildFragmentManager().beginTransaction().remove(this).commit();
            }

            return true;
        }

        if (id == R.id.action_edit){
            Intent i = new Intent(getActivity(),EditGroupActivity.class);
            i.putExtra(Constants.GROUP,mGroup);
            i.putParcelableArrayListExtra(Constants.GROUP_MEMBERS,mGroupMemberList);
            startActivity(i);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

}
