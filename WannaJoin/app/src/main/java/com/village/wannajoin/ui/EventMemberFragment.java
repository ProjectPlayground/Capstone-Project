package com.village.wannajoin.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.village.wannajoin.R;
import com.village.wannajoin.model.Event;
import com.village.wannajoin.model.Member;
import com.village.wannajoin.util.ArrayFirebase;
import com.village.wannajoin.util.Constants;
import com.village.wannajoin.util.DividerItemDecoration;

import java.util.ArrayList;


public class EventMemberFragment extends Fragment {

    private String mEventId;
    private String mOwnerId;
    private Event mEvent;
    private ArrayList<Member> mEventMemberList;
    Query mRef;
    ArrayFirebase mSnapshotsEventMembers;
    EventMembersRecyclerViewAdapter mAdapter;
    //private OnFragmentInteractionListener mListener;

    public EventMemberFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EventMemberFragment newInstance(Event event) {
        EventMemberFragment fragment = new EventMemberFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EVENT,event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEvent = getArguments().getParcelable(Constants.EVENT);
            mEventId = mEvent.getEventId();
            mRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_EVENT_MEMBERS).child(mEventId).child(Constants.FIREBASE_LOCATION_USERS).orderByChild("name");
            mEventMemberList = new ArrayList<>();
            mAdapter = new EventMembersRecyclerViewAdapter(getActivity(),mEventMemberList);
            mSnapshotsEventMembers = new ArrayFirebase(mRef);
            mSnapshotsEventMembers.setOnChangedListener(new ArrayFirebase.OnChangedListener() {
                @Override
                public void onChanged(EventType type, int index, int oldIndex) {
                    switch (type) {
                        case Added:
                            Member member = mSnapshotsEventMembers.getItem(index).getValue(Member.class);
                            mEventMemberList.add(index,member);
                            mAdapter.notifyItemInserted(index);
                            break;
                        case Changed:
                            Member memberc = mSnapshotsEventMembers.getItem(index).getValue(Member.class);
                            mEventMemberList.set(index,memberc);
                            mAdapter.notifyItemChanged(index);
                            break;
                        case Removed:
                            mEventMemberList.remove(index);
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




        }

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_member, container, false);
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.event_member_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
 /*   public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
  /*  public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSnapshotsEventMembers.cleanup();
        // mAdapter.cleanup();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_event_member_fragment,menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem itemEdit = menu.findItem(R.id.action_add);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(mEvent.getOwnerId())){
            itemEdit.setVisible(true);
        }else
            itemEdit.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent i = new Intent(getActivity(), ShareEventActivity.class);
            i.putExtra("type","EDIT");
            i.putExtra(Constants.EVENT, mEvent);
            i.putParcelableArrayListExtra(Constants.EVENT_MEMBERS,mEventMemberList);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
