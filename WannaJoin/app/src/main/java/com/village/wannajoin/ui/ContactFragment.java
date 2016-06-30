package com.village.wannajoin.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Group;
import com.village.wannajoin.model.Member;
import com.village.wannajoin.util.Constants;


public class ContactFragment extends Fragment {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    Query mGroupRef;
    DatabaseReference mContactRef;
    GroupRecyclerViewAdapter mGroupsAdapter;
    ContactRecyclerViewAdapter mContactsAdapter;

    //private OnFragmentInteractionListener mListener;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(int param1) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);

        }
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mGroupRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_GROUPS).orderByChild("groupMembers/"+currentUserId).equalTo(true);
        mContactRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_CONTACTS).child(currentUserId);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        Context context = view.getContext();
        RecyclerView groupRecyclerView = (RecyclerView) view.findViewById(R.id.group_list);
        if (mColumnCount <= 1) {
            groupRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            groupRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        groupRecyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));

        mGroupsAdapter = new GroupRecyclerViewAdapter(Group.class, R.layout.group_list_item,GroupRecyclerViewAdapter.ViewHolder.class,mGroupRef, getContext());
        groupRecyclerView.setAdapter(mGroupsAdapter);

        RecyclerView contactRecyclerView = (RecyclerView) view.findViewById(R.id.contact_list);
        if (mColumnCount <= 1) {
            contactRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            contactRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        contactRecyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));

        mContactsAdapter= new ContactRecyclerViewAdapter(Member.class, R.layout.contact_list_item,ContactRecyclerViewAdapter.ViewHolder.class,mContactRef, getContext());
        contactRecyclerView.setAdapter(mContactsAdapter);

        return view;
    }


/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_contact_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.new_contact){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            DialogFragment newFragment = NewContactDialogFragment.newInstance();
            newFragment.show(ft, "dialog");
            return true;
        }

        if(id == R.id.new_group){
            Intent i = new Intent(getActivity(),NewGroupActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGroupsAdapter.cleanup();
        mContactsAdapter.cleanup();
    }
}
