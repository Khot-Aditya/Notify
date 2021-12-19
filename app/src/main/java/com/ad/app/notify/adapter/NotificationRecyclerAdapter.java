package com.ad.app.notify.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.app.notify.R;
import com.ad.app.notify.model.NotificationModel;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.ListBasicViewHolder> {

    private final List<NotificationModel> objectList;
    private final Context context;

    public NotificationRecyclerAdapter(List<NotificationModel> objectList, Context context) {
        this.objectList = objectList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationRecyclerAdapter.ListBasicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemveiw_notification, parent, false);
        return new NotificationRecyclerAdapter.ListBasicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerAdapter.ListBasicViewHolder holder, int position) {
        final NotificationModel object = objectList.get(position);

        holder.cardview_Container.setCardBackgroundColor(Color.parseColor("#FFF2DE"));
        holder.txt_DayOfTheWeek.setText("SUN");
        holder.txt_Date.setText("19");
        holder.txt_SubText.setText(object.getNotificationSubText());
        holder.txt_Time.setText(object.getNotificationTime());
        holder.ic_Pin.setImageResource(R.drawable.ic_thumbtack);
        holder.txt_Tags.setText(object.getNotificationTags());

//        holder.txt_FileName.setText(object.getFileName());
//        holder.txt_Filetype.setText(object.getFileType());
//        holder.txt_FileSize.setText(Formatter.formatShortFileSize(context, Integer.parseInt(object.getFileSize())));
//
//        holder.img_RemoveFile.setOnClickListener(v -> {
//            objectList.remove(holder.getAdapterPosition());
//            notifyItemRemoved(holder.getAdapterPosition());
//        });

    }


    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public static class ListBasicViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView cardview_Container;
        private final TextView txt_DayOfTheWeek;
        private final TextView txt_Date;
        private final TextView txt_SubText;
        private final TextView txt_Time;
        private final ImageView ic_Pin;
        private final TextView txt_Tags;

        public ListBasicViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_Container = itemView.findViewById(R.id.cardview_Container);
            txt_DayOfTheWeek = itemView.findViewById(R.id.txt_DayOfTheWeek);
            txt_Date = itemView.findViewById(R.id.txt_Date);
            txt_SubText = itemView.findViewById(R.id.txt_SubText);
            txt_Time = itemView.findViewById(R.id.txt_Time);
            ic_Pin = itemView.findViewById(R.id.ic_Pin);
            txt_Tags = itemView.findViewById(R.id.txt_Tags);

        }
    }
}

