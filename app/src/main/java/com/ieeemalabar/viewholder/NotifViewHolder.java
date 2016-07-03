package com.ieeemalabar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ieeemalabar.R;

/**
 * Created by AKHIL on 03-Jul-16.
 */
public class NotifViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public NotifViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setMessage(String title, String message, String date) {
        TextView Ttitle = (TextView) mView.findViewById(R.id.notif_title);
        TextView Tmessage = (TextView) mView.findViewById(R.id.notif_text);
        TextView Tdate = (TextView) mView.findViewById(R.id.notif_date);

        Ttitle.setText(title);
        Tmessage.setText(message);
        Tdate.setText(date);
    }

}