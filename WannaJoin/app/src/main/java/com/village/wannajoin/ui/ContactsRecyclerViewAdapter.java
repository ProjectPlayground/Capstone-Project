package com.village.wannajoin.ui;

import android.content.Context;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.village.wannajoin.R;
import com.village.wannajoin.model.ContactAndGroup;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by richa on 6/30/16.
 */
public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context mContext;
    String mActivityName;
    ArrayList<ContactAndGroup> contactAndGroupArrayList;
    ViewClickedListener mListner;

    interface ViewClickedListener {
        void onItemClicked(int type, ContactAndGroup contactAndGroup);
    }


    public ContactsRecyclerViewAdapter(Context context, String activityName, ArrayList<ContactAndGroup> cgList, ViewClickedListener listener) {
        mContext = context;
        mActivityName = activityName;
        contactAndGroupArrayList = cgList;
        mListner = listener;
    }

    @Override
    public int getItemCount() {
        return contactAndGroupArrayList.size();
    }

    public ContactAndGroup getItem(int position) {
        return contactAndGroupArrayList.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (viewType ==0){
            view = layoutInflater.inflate(R.layout.list_label_item, parent, false);
            LabelViewHolder vh = new LabelViewHolder(view);
            return vh;
        }else if(viewType ==-1) {
            view = layoutInflater.inflate(R.layout.empty_recycler_view, parent, false);
            EmptyViewHolder vh = new EmptyViewHolder(view, new EmptyViewHolder.EmptyViewHolderClicks() {
                @Override
                public void onViewClicked(int pos) {
                    if (getItem(pos).isGroup())
                        mListner.onItemClicked(1, null);
                    else
                        mListner.onItemClicked(2, null);
                }
            });
            return vh;
        }else{
            view = layoutInflater.inflate(R.layout.contact_list_item, parent, false);
            ContactsRecyclerViewAdapter.ViewHolder vh = new ContactsRecyclerViewAdapter.ViewHolder(view, new ViewHolder.ViewHolderClicks() {
                @Override
                public void onDetail(int pos) {
                        int type = getItem(pos).getType();
                        mListner.onItemClicked(type, getItem(pos));
                }
            });
            return vh;
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType()==0){
            ContactsRecyclerViewAdapter.LabelViewHolder lvh = (ContactsRecyclerViewAdapter.LabelViewHolder)holder;
            lvh.label.setText(getItem(position).getName());
            lvh.itemView.setContentDescription(getItem(position).getName());
        }else if(holder.getItemViewType()==-1){
            ContactsRecyclerViewAdapter.EmptyViewHolder evh = (ContactsRecyclerViewAdapter.EmptyViewHolder)holder;
            evh.defaultText.setText(getItem(position).getName());
            evh.itemView.setContentDescription(getItem(position).getName());
           /* evh.defaultText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getItem(position).isGroup())
                        mListner.onItemClicked(1);
                    else
                        mListner.onItemClicked(2);
                }
            });*/
        }
        else {
            ContactsRecyclerViewAdapter.ViewHolder vh = (ContactsRecyclerViewAdapter.ViewHolder)holder;
            if (mActivityName.equals("ShareEventActivity"))
                vh.isSelected.setVisibility(View.VISIBLE);
            if (mActivityName.equals("MainActivity"))
                vh.isSelected.setVisibility(View.GONE);
            vh.contactName.setText(getItem(position).getName());
            int imageId=R.drawable.ic_account_circle_black_48dp;
            if(getItem(position).getType()==1){
                imageId = R.drawable.ic_group_black_36dp;
            }else if (getItem(position).getType()==2){
                imageId = R.drawable.ic_account_circle_black_48dp;
            }
            if (getItem(position).getPhotoUrl() == null) {
                    vh.contactImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(mContext,
                                            imageId));

            } else {
                Glide.with(mContext)
                        .load(getItem(position).getPhotoUrl())
                        .into(vh.contactImageView);
            }
            vh.itemView.setContentDescription(getItem(position).getName());
            vh.isSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        contactAndGroupArrayList.get(position).setSelected(isChecked);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

   @Override
    public int getItemViewType(int position) {
        return contactAndGroupArrayList.get(position).getType();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ViewHolderClicks mClickListener;
        public CircleImageView contactImageView;
        public TextView contactName;
        public CheckBox isSelected;
        public ViewHolder(View view, ViewHolderClicks clickListener) {
            super(view);
            contactImageView = (CircleImageView) view.findViewById(R.id.contact_image_view);
            contactName = (TextView) view.findViewById(R.id.contact_name);
            isSelected = (CheckBox) view.findViewById(R.id.select_box);
            view.setOnClickListener(this);
            mClickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            mClickListener.onDetail(getAdapterPosition());
        }
        public interface ViewHolderClicks {
            void onDetail(int pos);
        }
    }

    public static class LabelViewHolder extends RecyclerView.ViewHolder{
        public TextView label;
        public LabelViewHolder(View view) {
            super(view);
            label = (TextView) view.findViewById(R.id.name);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView defaultText;
        public EmptyViewHolderClicks mEVClickListener;
        public EmptyViewHolder(View view, EmptyViewHolderClicks mEVClickListener) {
            super(view);
            defaultText = (TextView) view.findViewById(R.id.empty_view);
            this.mEVClickListener = mEVClickListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mEVClickListener.onViewClicked(getAdapterPosition());
        }

        public interface EmptyViewHolderClicks {
            void onViewClicked(int pos);
        }
    }
}

