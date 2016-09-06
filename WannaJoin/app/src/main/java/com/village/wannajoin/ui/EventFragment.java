package com.village.wannajoin.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.village.wannajoin.R;
import com.village.wannajoin.model.ContactAndGroup;
import com.village.wannajoin.model.Event;
import com.village.wannajoin.model.Group;
import com.village.wannajoin.util.ArrayFirebase;
import com.village.wannajoin.util.Constants;
import com.village.wannajoin.util.DividerItemDecoration;
import com.village.wannajoin.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EventFragment extends Fragment implements EventsRecyclerViewAdapter.OnClickedListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
   // private OnListFragmentInteractionListener mListener;
    Query mRef;
    EventsRecyclerViewAdapter mAdapter;
    ArrayList<Event> mEventList;
    ArrayFirebase mSnapshotsEvents;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventFragment newInstance(int columnCount) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
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

        mRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_EVENTS).orderByChild("eventMembers/"+currentUserId).startAt(Util.getMidNightTimeStamp());
       // mAdapter = new EventRecyclerViewAdapter(Event.class, R.layout.fragment_event,EventRecyclerViewAdapter.ViewHolder.class,mRef, getContext());
        mEventList = new ArrayList<>();
        Event emptyEvent = new Event(null,"No available events. Start by creating events and sharing with friends.",null,null,null,1,1,null,null);
        emptyEvent.setType(-1); // set -1 for empty event
        mEventList.add(emptyEvent);
        mAdapter = new EventsRecyclerViewAdapter(getActivity(),mEventList, this);
        mSnapshotsEvents = new ArrayFirebase(mRef);
        mSnapshotsEvents.setOnChangedListener(new ArrayFirebase.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                switch (type) {
                    case Added:
                        Event event = mSnapshotsEvents.getItem(index).getValue(Event.class);
                        if ((mEventList.size()==1)&&(mEventList.get(0).getType() ==-1)){
                            mEventList.remove(0);
                            mAdapter.notifyItemRemoved(0);
                        }
                        mEventList.add(index,event);
                        mAdapter.notifyItemInserted(index);
                        break;
                    case Changed:
                        Event eventc = mSnapshotsEvents.getItem(index).getValue(Event.class);
                        mEventList.set(index,eventc);
                        mAdapter.notifyItemChanged(index);
                        break;
                    case Removed:
                        mEventList.remove(index);
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
        Util.updateWidgets(getContext());
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);



        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.event_list);
        TextView emptyView = (TextView)view.findViewById(R.id.empty_view);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));

        recyclerView.setAdapter(mAdapter);

      /*  if (mAdapter.getItemCount()==0){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), NewEventActivity.class);
                    startActivity(i);
                }
            });
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }*/
        return view;
    }


  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
   /* public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Event event);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_event_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.new_event){
            Intent i = new Intent(getActivity(),NewEventActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSnapshotsEvents.cleanup();
       // mAdapter.cleanup();
    }

    @Override
    public void onItemClicked(Event event) {
        if (event!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
                Intent i = new Intent(getActivity(), EventDetailActivity.class);
                i.putExtra(Constants.EVENT, event);
                startActivity(i, bundle);
            }else {
                Intent i = new Intent(getActivity(), EventDetailActivity.class);
                i.putExtra(Constants.EVENT, event);
                startActivity(i);
            }
        }else{
            Intent i = new Intent(getActivity(),NewEventActivity.class);
            startActivity(i);
        }
    }

}
