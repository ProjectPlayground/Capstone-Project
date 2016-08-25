package com.village.wannajoin.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.village.wannajoin.R;
import com.village.wannajoin.model.ContactAndGroup;
import com.village.wannajoin.model.User;
import com.village.wannajoin.util.Constants;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by richa on 8/24/16.
 */
public class ContactDetailDialogFragment extends DialogFragment {

    private String mContactId;
    private String mContactName;
    private Uri mContactPhotoUrl;

    public static ContactDetailDialogFragment newInstance(String contactId, String name, Uri photoUrl) {
        ContactDetailDialogFragment contactDetailDialogFragment= new ContactDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.CONTACT_ID,contactId);
        args.putString(Constants.CONTACT_NAME,name);
        args.putParcelable(Constants.CONTACT_PHOTO_URL,photoUrl);

        contactDetailDialogFragment.setArguments(args);
        return contactDetailDialogFragment;
    }
    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mContactId = args.getString(Constants.CONTACT_ID);
        mContactName = args.getString(Constants.CONTACT_NAME);
        mContactPhotoUrl = args.getParcelable(Constants.CONTACT_PHOTO_URL);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(R.string.contact_detail_dialog_title);
        View v = inflater.inflate(R.layout.dialog_fragment_contact_detail, container, false);
        CircleImageView circleImageView = (CircleImageView) v.findViewById(R.id.contact_image_view);
        TextView contactNameTV = (TextView) v.findViewById(R.id.contact_name);
        final TextView contactEmailTV = (TextView) v.findViewById(R.id.contact_email);
        Button buttonOk = (Button) v.findViewById(R.id.button_ok);
        Button buttonDelete = (Button) v.findViewById(R.id.button_delete);
        contactNameTV.setText(mContactName);
        if (mContactPhotoUrl == null) {
            circleImageView
                    .setImageDrawable(ContextCompat
                            .getDrawable(getContext(),
                                    R.drawable.ic_account_circle_black_48dp));

        } else {
            Glide.with(getContext())
                    .load(mContactPhotoUrl)
                    .into(circleImageView);
        }
        DatabaseReference dbrefuser = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERS).child(mContactId);
        dbrefuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                contactEmailTV.setText(user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dismiss();
            }
        });

       if (getActivity().getClass().getSimpleName().equals("GroupDetailActivity")){
           buttonDelete.setVisibility(View.GONE);
       }else {
           buttonDelete.setVisibility(View.VISIBLE);
           buttonDelete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   DatabaseReference dbrefcontact = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_CONTACTS).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mContactId);
                   dbrefcontact.removeValue();
                   dismiss();
               }
           });
       }
        return v;
    }

}
