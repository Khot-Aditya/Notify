package com.ad.app.notify.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.app.notify.R;
import com.ad.app.notify.utils.Utils;

public class ItemSelectorListViewDialog extends Dialog implements View.OnClickListener {

    public Context context;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter adapter;

    public ItemSelectorListViewDialog(Context context, RecyclerView.Adapter adapter) {
        super(context);
        this.context = context;
        this.adapter = adapter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_item_selector);

        getWindow().setGravity(Gravity.CENTER);

        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        recyclerView = findViewById(R.id.recycler_view_item_selector);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addItemDecoration(new Utils.SimpleDividerItemDecoration(context));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
//        dismiss();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}