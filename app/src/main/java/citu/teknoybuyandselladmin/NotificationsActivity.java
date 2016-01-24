package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import citu.teknoybuyandselladmin.adapters.NotificationAdapter;
import citu.teknoybuyandselladmin.models.Notification;
import citu.teknoybuyandselladmin.services.ExpirationCheckerService;
import citu.teknoybuyandselladmin.services.NotificationService;


public class NotificationsActivity extends BaseActivity {

    private static final String TAG = "NotificationsActivity";
    public static final String NOTIFICATION_ID = "notification_id";

    private Notification notification;
    private ProgressBar mProgressBar;

    private ArrayList<Notification> mNotifications = new ArrayList<Notification>();
    private RecyclerView list;
    private NotificationAdapter mAdapter;

    private SwipeRefreshLayout refreshLayout;

    private NotificationRefreshBroadcastReceiver mReceiver;

    private Gson gson = new Gson();

    private String response = "";
    private int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setupUI();


        mProgressBar = (ProgressBar) findViewById(R.id.progressGetNotifications);
        mReceiver = new NotificationRefreshBroadcastReceiver();

        getNotifications();

        Log.e(TAG,"Size: "+ mNotifications.size() + "");
        mAdapter = new NotificationAdapter(mNotifications);
        list = (RecyclerView) findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));
        list.setAdapter(mAdapter);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(NotificationsActivity.this, "Refreshing ...", Toast.LENGTH_SHORT).show();
                // call this after refreshing is done
                getNotifications();
                refreshLayout.setRefreshing(false);
            }
        });


    }

    private void getNotifications() {
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }

    /*public void getNotifications() {
        String username = "admin";
        Server.getNotifications(username, mProgressBar, new Ajax.Callbacks() {
            TextView txtMessage = (TextView) findViewById(R.id.txtMessage);

            @Override
            public void success(String responseBody) {
                ArrayList<Notification> notifications = gson.fromJson(responseBody, new TypeToken<ArrayList<Notification>>() {
                }.getType());

                if (notifications.size() == 0) {
                    txtMessage.setText("No new notifications");
                    txtMessage.setVisibility(View.VISIBLE);
                } else {
                    txtMessage.setVisibility(View.GONE);

                    RecyclerView list = (RecyclerView) findViewById(R.id.list);
                    list.setHasFixedSize(true);
                    list.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));
                    list.setAdapter(new NotificationAdapter(notifications));
                }

            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                txtMessage.setText("Connection Error: Cannot connect to server. Please check your internet connection");
                txtMessage.setVisibility(View.VISIBLE);
            }
        });
    }*/

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_notifications;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " + response);
        registerReceiver(mReceiver, new IntentFilter(NotificationService.class.getCanonicalName()));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private class NotificationRefreshBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            refreshLayout.setRefreshing(false);
            response = intent.getStringExtra("response");
            result = intent.getIntExtra("service result", 0);

            mNotifications = gson.fromJson(response, new TypeToken<ArrayList<Notification>>() {
            }.getType());
            mAdapter = new NotificationAdapter(mNotifications);
            list.setAdapter(mAdapter);

            Log.e(TAG, "Received: " + response);
            Log.e(TAG, "Received: " + mNotifications.size());
            mProgressBar.setVisibility(View.GONE);
        }

    }

}
