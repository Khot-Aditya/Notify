package com.ad.app.notify.adapter;

import static com.ad.app.notify.utils.Constants.NOTIFICATION_MODEL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
        dayOfTheWeek = object.getNotificationDate().substring(0, 3);
        date = object.getNotificationDate().substring(5, 7);

        //if not date starts with 0
        if (!date.startsWith("0")) {
            date = "0" + date;
        }


        holder.txt_DayOfTheWeek.setText(dayOfTheWeek);
        holder.txt_Date.setText(date);

        String subText = object.getNotificationSubText().length() > 300 ?
                object.getNotificationSubText().substring(0, 300) + "..." :
                object.getNotificationSubText();


        //------------------------------------------------------------------------------------------
        float factor1 = 0.95f;
        int color1 = object.getNotificationBgColor();
        int a = Color.alpha(color1);
        int r = Math.round(Color.red(color1) * factor1);
        int g = Math.round(Color.green(color1) * factor1);
        int b = Math.round(Color.blue(color1) * factor1);
        int darker = Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));


        //------------------------------------------------------------------------------------------
        float factor = 0.7f;
        int color = object.getNotificationBgColor();
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        int lighter = Color.argb(Color.alpha(color), red, green, blue);

        //------------------------------------------------------------------------------------------
        float factor2 = 0.55f;
        int color2 = object.getNotificationBgColor();
        int a2 = Color.alpha(color2);
        int r2 = Math.round(Color.red(color2) * factor2);
        int g2 = Math.round(Color.green(color2) * factor2);
        int b2 = Math.round(Color.blue(color2) * factor2);
        int darker2 = Color.argb(a2,
                Math.min(r2, 255),
                Math.min(g2, 255),
                Math.min(b2, 255));

        holder.txt_Tags.setTextColor(darker2);
        holder.txt_Time.setTextColor(darker2);

        holder.view_Decor_RecyclerView.setCardBackgroundColor(darker);
        holder.cardview_Container.setCardBackgroundColor(lighter);

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

