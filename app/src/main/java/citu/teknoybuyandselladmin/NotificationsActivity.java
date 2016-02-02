package citu.teknoybuyandselladmin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import citu.teknoybuyandselladmin.adapters.NotificationAdapter;
import citu.teknoybuyandselladmin.models.Notification;
import citu.teknoybuyandselladmin.services.ExpirationCheckerService;
import citu.teknoybuyandselladmin.services.NotificationService;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class NotificationsActivity extends BaseActivity {

    private static final String TAG = "NotificationsActivity";
    public static final String NOTIFICATION_ID = "notification_id";

    private TextView mTxtMessage;
    private ProgressBar mProgressBar;

    private RecyclerView list;
    private Realm realm;
    private NotificationAdapter mAdapter;

    private SwipeRefreshLayout refreshLayout;

    private NotificationRefreshBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setupUI();
        realm = Realm.getDefaultInstance();

        mTxtMessage = (TextView) findViewById(R.id.txtMessage);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetNotifications);
        mReceiver = new NotificationRefreshBroadcastReceiver();

        mProgressBar.setVisibility(View.GONE);
        getNotifications();
        RealmResults<Notification> notifications = realm.where(Notification.class).findAll();

        if(notifications.size() == 0){
            mProgressBar.setVisibility(View.VISIBLE);
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
                getNotifications();
                mAdapter.notifyDataSetChanged();
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
        registerReceiver(mReceiver, new IntentFilter(NotificationService.class.getCanonicalName()));
        mAdapter.notifyDataSetChanged();

        startService(new Intent(this, ExpirationCheckerService.class));
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
            Log.e(TAG, intent.getStringExtra("response"));
            mProgressBar.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();

            if(intent.getIntExtra("result",0) == -1){
                Snackbar.make(list,"No internet connection",Snackbar.LENGTH_SHORT).show();
            }
        }

    }

}
