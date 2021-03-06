package citu.teknoybuyandselladmin;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import citu.teknoybuyandselladmin.adapters.ItemsOnQueueAdapter;
import citu.teknoybuyandselladmin.models.SellApproval;
import citu.teknoybuyandselladmin.services.ExpirationCheckerService;
import citu.teknoybuyandselladmin.services.ItemsOnQueueService;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class ItemsOnQueueActivity extends BaseActivity {

    private static final String TAG = "ItemsOnQueueActivity";

    private ItemsOnQueueBroadcastReceiver mReceiver;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView list;
    private Realm realm;
    private RealmResults<SellApproval> results;
    private ItemsOnQueueAdapter mAdapter;
    private TextView mTxtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_on_queue);
        setupUI();

        realm = Realm.getDefaultInstance();
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetSellRequests);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mTxtMessage = (TextView) findViewById(R.id.txtMessage);
        mReceiver = new ItemsOnQueueBroadcastReceiver();
        mProgressBar.setVisibility(View.GONE);

        getSellRequests();
        results = realm.where(SellApproval.class).findAll();
        if(results.size() == 0){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mAdapter = new ItemsOnQueueAdapter(results);
        list = (RecyclerView) findViewById(R.id.listQueue);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(ItemsOnQueueActivity.this));
        list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        list.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSellRequests();
                mAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    private void getSellRequests() {
        Intent intent = new Intent(this, ItemsOnQueueService.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_on_queue, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.search(newText);
                return true;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_search || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_items_queue;
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mReceiver, new IntentFilter(ItemsOnQueueService.class.getCanonicalName()));
        mAdapter.notifyDataSetChanged();

        startService(new Intent(this, ExpirationCheckerService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void showHideErrorMessage() {
        if(results.isEmpty()) {
            Log.e(TAG, "No Items on Queue");
            mTxtMessage.setVisibility(View.VISIBLE);
            mTxtMessage.setText("No Items on Queue");
        } else {
            mTxtMessage.setVisibility(View.GONE);
        }
    }

    private class ItemsOnQueueBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            refreshLayout.setRefreshing(false);
            Log.e(TAG, intent.getStringExtra("response"));
            mProgressBar.setVisibility(View.GONE);
            showHideErrorMessage();
            mAdapter.notifyDataSetChanged();

            if(intent.getIntExtra("result",0) == -1){
                Snackbar.make(list, "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }

    }
}
