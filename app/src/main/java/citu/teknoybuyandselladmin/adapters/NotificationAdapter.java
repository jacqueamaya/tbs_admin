package citu.teknoybuyandselladmin.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import citu.teknoybuyandselladmin.DonationsActivity;
import citu.teknoybuyandselladmin.ItemsOnQueueActivity;
import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.ReservedItemsActivity;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.Notification;
import io.realm.RealmResults;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private static final String TAG = "NotificationAdapter";

    private RealmResults<Notification> mNotifications;

    public NotificationAdapter(RealmResults<Notification> notifications) {
        mNotifications = notifications;
    }

    public void updateData (RealmResults<Notification> notifications) {
        mNotifications = notifications;
        notifyDataSetChanged();
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification notification = mNotifications.get(position);
        holder.userAvatar.setImageURI(Uri.parse(notification.getItem().getPicture()));
        holder.notificationText.setText(Utils.capitalize(notification.getMessage()));
        holder.notificationDate.setText(Utils.parseDate(notification.getNotification_date()));
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleDraweeView userAvatar;
        TextView notificationText;
        TextView notificationDate;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            userAvatar = (SimpleDraweeView) itemView.findViewById(R.id.image);
            notificationText = (TextView) itemView.findViewById(R.id.textView);
            notificationDate = (TextView) itemView.findViewById(R.id.txtDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            Log.e(TAG,"onclick");
            final Context context = view.getContext();
            int position = getAdapterPosition();
            final Notification notification = mNotifications.get(position);
            view.setBackgroundResource(R.color.forNotifs);

           /* Map<String, String> data = new HashMap<>();
            data.put(NotificationsActivity.NOTIFICATION_ID, notification.getId() + "");

            ProgressDialog progressDialog = new ProgressDialog(view.getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading. . .");
            Server.readNotification(data, progressDialog, new Ajax.Callbacks() {
                @Override
                public void success(String responseBody) {*/
                    String notificationType = notification.getNotification_type();
                    String itemPurpose = notification.getItem().getPurpose();

                    if (notificationType.equals("sell")) {
                        Log.v(TAG, "sell");
                        context.startActivity(new Intent(context, ItemsOnQueueActivity.class));
                    } else if (notificationType.equals("for rent")) {
                        Log.v(TAG, "for rent");
                        context.startActivity(new Intent(context, ItemsOnQueueActivity.class));
                    } else if (notificationType.equals("rent")) {
                        Log.v(TAG, "rent");
                        context.startActivity(new Intent(context, ReservedItemsActivity.class));
                    } else if (notificationType.equals("buy")) {
                        Log.v(TAG, "buy");
                        context.startActivity(new Intent(context, ReservedItemsActivity.class));
                    } else if (notificationType.equals("get")) {
                        Log.v(TAG, "get");
                        context.startActivity(new Intent(context, ReservedItemsActivity.class));
                    } else if (notificationType.equals("donate")) {
                        Log.v(TAG, "donate");
                        context.startActivity(new Intent(context, DonationsActivity.class));
                    } else if (notificationType.equals("edit") && itemPurpose.equals("Sell")) {
                        Log.v(TAG, "edit sell item");
                        context.startActivity(new Intent(context, ItemsOnQueueActivity.class));
                    } else if (notificationType.equals("edit") && itemPurpose.equals("Donate")) {
                        Log.v(TAG, "edit donated item");
                        context.startActivity(new Intent(context, DonationsActivity.class));
                    }
                /*}

                @Override
                public void error(int statusCode, String responseBody, String statusText) {

                }
            });*/
        }

    }
}