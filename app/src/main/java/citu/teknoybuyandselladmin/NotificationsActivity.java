package citu.teknoybuyandselladmin;

import io.realm.Realm;

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
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import citu.teknoybuyandselladmin.adapters.NotificationAdapter;
import citu.teknoybuyandselladmin.models.Notification;
import citu.teknoybuyandselladmin.services.NotificationService;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class NotificationsActivity extends BaseActivity {

    private static final String TAG = "NotificationsActivity";
    public static final String NOTIFICATION_ID = "notification_id";

    private Notification notification;
    private ProgressBar mProgressBar;
    private TextView mTxtMessage;

    private RecyclerView list;
    private Realm realm;

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
        realm = Realm.getInstance(new RealmConfiguration.Builder(this).build());

        mProgressBar = (ProgressBar) findViewById(R.id.progressGetNotifications);
        mTxtMessage = (TextView) findViewById(R.id.txtMessage);
        mReceiver = new NotificationRefreshBroadcastReceiver();

        RealmResults<Notification> notifications = realm.where(Notification.class).findAll();
        if(notifications.size() == 0){
            Log.e(TAG,"No notif cached"+notifications.size());
            getNotifications();
        }

        mAdapter = new NotificationAdapter(notifications);
        list = (RecyclerView) findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));
        list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
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

        getNotifications();

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

            mProgressBar.setVisibility(View.GONE);

            Log.e(TAG,intent.getStringExtra("response"));
        }

    }

}
