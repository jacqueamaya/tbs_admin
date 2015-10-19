package citu.teknoybuyandselladmin.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.Notification;


public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private static final String TAG = "NotificationListAdapter";

    private final int LAYOUT_RESOURCE_ID;

    private Context mContext;
    private ArrayList<Notification> items;

    public NotificationListAdapter(Context context, int textViewResourceId, ArrayList<Notification> list) {
        super(context, textViewResourceId, list);

        mContext = context;
        items = list;
        LAYOUT_RESOURCE_ID = textViewResourceId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Notification notification = items.get(position);

        String message, notificationDate;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(LAYOUT_RESOURCE_ID, parent, false);
        }

        TextView text = (TextView) view.findViewById(R.id.textView);
        ImageView image = (ImageView) view.findViewById(R.id.image);

        notificationDate = Utils.parseDate(notification.getNotificationDate());
        Log.v(TAG,notification.getStatus());
        if ("unread".equals(notification.getStatus())) {
            Log.v(TAG,"This notification is unread");
            view.setBackgroundResource(R.color.White);
        } else {
            Log.v(TAG,"This notification is read");
            view.setBackgroundResource(R.color.forNotifs);
        }

        Picasso.with(mContext)
                .load(notification.getItemLink())
                .placeholder(R.drawable.notif_user)
                .resize(50, 50)
                .centerCrop()
                .into(image);

        switch (notification.getNotificationType()) {
            case "sell":
                message = "<b>" + Utils.capitalize(notification.getOwnerUsername()) + " </b> wants to <b>sell</b> his/her <b>" + notification.getItemName() + "</b>.<br><small>" + notificationDate + "</small>";
                break;
            case "donate":
                message = "<b>" + Utils.capitalize(notification.getOwnerUsername()) + " </b> wants to <b>donate</b> his/her <b>" + notification.getItemName() + "</b>.<br><small>" + notificationDate + "</small>";
                break;
            case "buy":
                message = "<b>" + Utils.capitalize(notification.getMakerUsername()) + " </b> wants to <b>buy</b> the <b>" + notification.getItemName() + "</b> owned by <b>" + Utils.capitalize(items.get(position).getOwnerUsername()) + "</b>.<br><small>" + notificationDate + "</small>";
                break;
            case "cancel":
                message = "<b>" + Utils.capitalize(notification.getMakerUsername()) + " cancels</b> his/her reservation for <b>" + notification.getItemName() + "</b> owned by <b>" + Utils.capitalize(items.get(position).getOwnerUsername()) + "</b>.<br><small>" + notificationDate + "</small>";
                break;
            case "get":
                message = "<b>" + Utils.capitalize(notification.getMakerUsername()) + " " + "</b> wants to <b>reserve</b> the donated item, <b>" + notification.getItemName() + "</b> owned by <b>" + Utils.capitalize(items.get(position).getOwnerUsername()) + "</b>.<br><small>" + notificationDate + "</small>";
                break;
            case "edit":
                message = "<b>" + Utils.capitalize(notification.getOwnerUsername()) + " edited</b> his/her pending item, <b>" + notification.getItemName() + "</b>" + "</b>.<br><small>" + notificationDate + "</small>";
                break;
            case "delete":
                message = "<b>" + Utils.capitalize(notification.getOwnerUsername()) + " deleted</b> his/her pending item, <b>" + notification.getItemName() + "</b>" + "</b>.<br><small>" + notificationDate + "</small>";
                break;
            default:
                message = "<i>This is a default notification message</i>";
        }

        text.setText(Html.fromHtml(message));

        return view;
    }

}
