package citu.teknoybuyandselladmin;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import citu.teknoybuyandselladmin.adapters.RentedAdapter;
import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.RentedItem;
import citu.teknoybuyandselladmin.services.ExpirationCheckerService;
import citu.teknoybuyandselladmin.services.RentedItemService;
import io.realm.Realm;
import io.realm.RealmResults;


public class RentedItemsActivity extends BaseActivity {

    private static final String TAG = "RentedItemsActivity";
    private ProgressBar mProgressBar;

    private Realm realm;
    private RealmResults<RentedItem> results;
    private RentedAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView list;
    private RentedBroadcastReceiver mReceiver;
    private TextView mTxtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_items);
        setupUI();

        realm = Realm.getDefaultInstance();
        mReceiver = new RentedBroadcastReceiver();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        list = (RecyclerView) findViewById(R.id.listViewRented);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetReserveRequests);
        mTxtMessage = (TextView) findViewById(R.id.txtMessage);
        mProgressBar.setVisibility(View.GONE);

        getRentedItems();
        results = realm.where(RentedItem.class).findAll();
        if(results.size() == 0){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mAdapter = new RentedAdapter(results);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(RentedItemsActivity.this));
        list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        list.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRentedItems();
                mAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void getRentedItems() {
        Intent intent = new Intent(this, RentedItemService.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rented_items, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
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
        return menuItem.getItemId() != R.id.nav_rented_items;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(RentedItemService.class.getCanonicalName()));
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
            Log.e(TAG, "No Rented Items");
            mTxtMessage.setVisibility(View.VISIBLE);
            mTxtMessage.setText("No Rented Items");
        } else {
            mTxtMessage.setVisibility(View.GONE);
        }
    }

    private class RentedBroadcastReceiver extends BroadcastReceiver {

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
