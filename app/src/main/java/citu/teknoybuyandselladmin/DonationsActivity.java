package citu.teknoybuyandselladmin;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import citu.teknoybuyandselladmin.adapters.DonationAdapter;
import citu.teknoybuyandselladmin.models.DonateApproval;
import citu.teknoybuyandselladmin.services.DonationService;
import citu.teknoybuyandselladmin.services.ExpirationCheckerService;
import io.realm.Realm;
import io.realm.RealmResults;


public class DonationsActivity extends BaseActivity {

    private static final String TAG = "DonatedActivity";

    private ProgressBar mProgressBar;

    private String sortBy[];

    private String searchQuery = "";
    private String lowerCaseSort = "date";

    private Realm realm;
    private DonationAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView list;
    private DonationBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);
        setupUI();

        realm = Realm.getDefaultInstance();
        mReceiver = new DonationBroadcastReceiver();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        list = (RecyclerView) findViewById(R.id.listViewDonations);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetDonationsRequests);
        mProgressBar.setVisibility(View.GONE);

        sortBy = getResources().getStringArray(R.array.donate_sort_by);

        getDonatedItems();
        RealmResults<DonateApproval> results = realm.where(DonateApproval.class).findAll();
        if(results.size() == 0){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mAdapter = new DonationAdapter(results);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(DonationsActivity.this));
        list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        list.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDonatedItems();
                mAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void getDonatedItems() {
        Intent intent = new Intent(this, DonationService.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_donations, menu);

        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                //listAdapter.getFilter().filter(searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
               // listAdapter.getFilter().filter(searchQuery);
                return true;
            }
        });*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_search || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_donations;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(DonationService.class.getCanonicalName()));
        mAdapter.notifyDataSetChanged();

        startService(new Intent(this, ExpirationCheckerService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private class DonationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            refreshLayout.setRefreshing(false);
            Log.e(TAG, intent.getStringExtra("response"));
            mProgressBar.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();

            if(intent.getIntExtra("result",0) == -1){
                Snackbar.make(list, "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }

    }
}
