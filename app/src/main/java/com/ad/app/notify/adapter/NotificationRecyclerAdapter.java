package com.ad.app.notify.adapter;

import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.app.notify.R;
import com.ad.app.notify.activities.EditorActivity;
import com.ad.app.notify.model.NotificationModel;
import com.ad.app.notify.utils.Utils;
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

        String dayOfTheWeek = object.getNotificationDate().substring(0, 3);
        String date = object.getNotificationDate().substring(5, 7);

//        if(date.length() == 1){
//            date = "0" + date;
//        }

        holder.txt_DayOfTheWeek.setText(dayOfTheWeek);
        holder.txt_Date.setText(date);

        String subText = object.getNotificationSubText().length() > 300 ?
                object.getNotificationSubText().substring(0, 300) + "..." :
                object.getNotificationSubText();

        holder.txt_Tags.setTextColor(new Utils(context).colorBrightness(
                object.getNotificationBgColor(), "dark", 0.55f));
        holder.txt_Time.setTextColor(new Utils(context).colorBrightness(
                object.getNotificationBgColor(), "dark", 0.55f));
        holder.view_Decor_RecyclerView.setCardBackgroundColor(
                new Utils(context).colorBrightness(
                        object.getNotificationBgColor(), "dark", 0.95f));
        holder.cardview_Container.setCardBackgroundColor(new Utils(context).colorBrightness(
                object.getNotificationBgColor(), "light", 0.7f));


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
        private final MaterialCardView view_Decor_RecyclerView;
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

