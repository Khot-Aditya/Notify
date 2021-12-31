package com.ad.app.notify.adapter;

import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;
import static com.ad.app.notify.utils.Constants.TAG_EMAIL;
import static com.ad.app.notify.utils.Constants.TAG_NOTE;
import static com.ad.app.notify.utils.Constants.TAG_PHONE_NUMBER;
import static com.ad.app.notify.utils.Constants.TAG_URL;
import static com.ad.app.notify.utils.Constants.TAG_WATCH_LATER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.app.notify.R;
import com.ad.app.notify.activities.EditorActivity;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.utils.Constants;
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


        //under developed function
//        switch (object.getNotificationCategory()) {
//
//            case TAG_WATCH_LATER:
//                holder.view_Decor_RecyclerView.setBackground(ContextCompat.getDrawable(context, R.drawable.itemdecor_recyclerview_watch_later));
//                holder.cardview_Container.setCardBackgroundColor(Color.parseColor("#FFDFDD"));
//                break;
//
//            case TAG_EMAIL:
//                holder.view_Decor_RecyclerView.setBackground(ContextCompat.getDrawable(context, R.drawable.itemdecor_recyclerview_email));
//                holder.cardview_Container.setCardBackgroundColor(ContextCompat.getColor(context,R.color.color_StickyNote6));
//                break;
//
//            case TAG_NOTE:
//                holder.view_Decor_RecyclerView.setBackground(ContextCompat.getDrawable(context, R.drawable.itemdecor_recyclerview_note));
//                holder.cardview_Container.setCardBackgroundColor(Color.parseColor("#FFDFDD"));
//                break;
//
//            case TAG_PHONE_NUMBER:
//                holder.view_Decor_RecyclerView.setBackground(ContextCompat.getDrawable(context, R.drawable.itemdecor_recyclerview_phone));
//                holder.cardview_Container.setCardBackgroundColor(Color.parseColor("#FFDFDD"));
//                break;
//
//            case TAG_URL:
//                holder.view_Decor_RecyclerView.setBackground(ContextCompat.getDrawable(context, R.drawable.itemdecor_recyclerview_url));
//                holder.cardview_Container.setCardBackgroundColor(Color.parseColor("#FFDFDD"));
//                break;
//        }



        String dayOfTheWeek = "null";
        String date = "null";
        try {
            dayOfTheWeek = object.getNotificationDate().substring(0, 3);
            date = object.getNotificationDate().substring(5, 7);

//            //if date starts with 0
//            if(date.substring(5,6).equals("0")){
//                date = date.substring(6,7);
//            }

        } catch (Exception e) {

        }

        holder.txt_DayOfTheWeek.setText(dayOfTheWeek);
        holder.txt_Date.setText(date);

        String subText = object.getNotificationSubText().length() > 300 ?
                object.getNotificationSubText().substring(0, 300) + "..." :
                object.getNotificationSubText();


        holder.txt_SubText.setText(subText);
        holder.txt_Time.setText(object.getNotificationTime());
        holder.ic_Pin.setVisibility(View.GONE);
        holder.txt_Tags.setText(" • " + object.getNotificationTags());

        holder.cardview_Container.setOnClickListener(v -> {

            context.startActivity(new Intent(context, EditorActivity.class).putExtra(NOTIFICATION_MODEL, object));

            ((Activity) context).overridePendingTransition(R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left);
        });

    }


    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public static class ListBasicViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView cardview_Container;
        private final View view_Decor_RecyclerView;
        private final TextView txt_DayOfTheWeek;
        private final TextView txt_Date;
        private final TextView txt_SubText;
        private final TextView txt_Time;
        private final ImageView ic_Pin;
        private final TextView txt_Tags;

        public ListBasicViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_Container = itemView.findViewById(R.id.cardview_Container);
            view_Decor_RecyclerView = itemView.findViewById(R.id.view_Decor_RecyclerView);
            txt_DayOfTheWeek = itemView.findViewById(R.id.txt_DayOfTheWeek);
            txt_Date = itemView.findViewById(R.id.txt_Date);
            txt_SubText = itemView.findViewById(R.id.txt_SubText);
            txt_Time = itemView.findViewById(R.id.txt_Time);
            ic_Pin = itemView.findViewById(R.id.ic_Pin);
            txt_Tags = itemView.findViewById(R.id.txt_Tags);

        }
    }
}

