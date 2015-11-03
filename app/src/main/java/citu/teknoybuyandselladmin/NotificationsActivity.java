package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselladmin.adapters.NotificationListAdapter;
import citu.teknoybuyandselladmin.models.Notification;


public class NotificationsActivity extends BaseActivity {

    private static final String TAG = "NotificationsActivity";

    private Notification notif;
    private ProgressDialog readProgress;
    private ProgressBar mProgressBar;

    public static final String NOTIFICATION_ID = "notification_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setupUI();

        readProgress = new ProgressDialog(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetNotifications);

        getNotifications();
    }

    public void getNotifications() {
        String username = "admin";
        Server.getNotifications(username,mProgressBar, new Ajax.Callbacks() {
            TextView txtMessage = (TextView) findViewById(R.id.txtMessage);

            @Override
            public void success(String responseBody) {
                ArrayList<Notification> notifications = new ArrayList<Notification>();
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        txtMessage.setText("No new notifications");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        notifications = Notification.asList(jsonArray);
                        txtMessage.setVisibility(View.GONE);
                        ListView lv = (ListView) findViewById(R.id.listViewNotif);
                        final NotificationListAdapter listAdapter = new NotificationListAdapter(NotificationsActivity.this, R.layout.item_notification, notifications);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                notif = (Notification) parent.getItemAtPosition(position);
                                view.setBackgroundResource(R.color.forNotifs);

                                Map<String, String> data = new HashMap<>();
                                data.put(NOTIFICATION_ID, notif.getId() + "");

                                readProgress.setIndeterminate(true);
                                readProgress.setMessage("Loading. . .");
                                Server.readNotification(data, readProgress, new Ajax.Callbacks() {
                                    @Override
                                    public void success(String responseBody) {
                                        String notificationType = NotificationsActivity.this.notif.getNotificationType();
                                        String itemPurpose = NotificationsActivity.this.notif.getItemPurpose();
                                        if (notificationType.equals("sell")) {
                                            Log.v(TAG, "sell");
                                            Intent intent;
                                            intent = new Intent(NotificationsActivity.this, ItemsOnQueueActivity.class);
                                            startActivity(intent);
                                        } else if (notificationType.equals("buy")) {
                                            Log.v(TAG, "buy");
                                            Intent intent;
                                            intent = new Intent(NotificationsActivity.this, ReservedItemsActivity.class);
                                            startActivity(intent);
                                        } else if (notificationType.equals("get")) {
                                            Log.v(TAG, "get");
                                            Intent intent;
                                            intent = new Intent(NotificationsActivity.this, ReservedItemsActivity.class);
                                            startActivity(intent);
                                        } else if (notificationType.equals("donate")) {
                                            Log.v(TAG, "donate");
                                            Intent intent;
                                            intent = new Intent(NotificationsActivity.this, DonationsActivity.class);
                                            startActivity(intent);
                                        } else if (notificationType.equals("edit") && itemPurpose.equals("Sell")) {
                                            Log.v(TAG, "edit sell item");
                                            Intent intent;
                                            intent = new Intent(NotificationsActivity.this, ItemsOnQueueActivity.class);
                                            startActivity(intent);
                                        } else if (notificationType.equals("edit") && itemPurpose.equals("Donate")) {
                                            Log.v(TAG, "edit donated item");
                                            Intent intent;
                                            intent = new Intent(NotificationsActivity.this, DonationsActivity.class);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void error(int statusCode, String responseBody, String statusText) {
                                        Log.v(TAG, "Cannot connect to server");
                                    }
                                });
                            }
                        });
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                txtMessage.setText("Connection Error: Cannot connect to server. Please check your internet connection");
                txtMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_notifications;
    }
}
