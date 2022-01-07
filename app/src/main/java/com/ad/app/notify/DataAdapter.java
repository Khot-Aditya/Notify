package com.ad.app.notify;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.FruitViewHolder> {

    private List<String> mDataset;
    RecyclerViewItemClickListener recyclerViewItemClickListener;
    String tag;

    public DataAdapter(List<String> myDataset, RecyclerViewItemClickListener listener, String tag) {
        mDataset = myDataset;
        this.recyclerViewItemClickListener = listener;
        this.tag = tag;
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_fruit, parent, false);
        return new FruitViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder fruitViewHolder, int i) {
        fruitViewHolder.mTextView.setText(mDataset.get(i));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;

        public FruitViewHolder(View v) {
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