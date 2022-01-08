package com.ad.app.notify.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.app.notify.R;

import java.util.List;

public class ItemSelectorDataAdapter extends RecyclerView.Adapter<ItemSelectorDataAdapter.ItemSelectorViewHolder> {

    private List<String> mDataset;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;
    private String tag;

    public ItemSelectorDataAdapter(List<String> myDataset, RecyclerViewItemClickListener listener, String tag) {
        mDataset = myDataset;
        this.recyclerViewItemClickListener = listener;
        this.tag = tag;
    }

    @NonNull
    @Override
    public ItemSelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_item_selector, parent, false);
        return new ItemSelectorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSelectorViewHolder itemSelectorViewHolder, int i) {
        itemSelectorViewHolder.mTextView.setText(mDataset.get(i));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ItemSelectorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;

        public ItemSelectorViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.textView);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnItem(mDataset.get(this.getAdapterPosition()), tag);

        }
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(String data, String tag);
    }
}