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
    private TextView txtCategory;

    private Category categories[];
    private String categoryNames[];
    private String sortBy[];

    private String searchQuery = "";
    private String category = "";
    private String lowerCaseSort = "date";

    private Realm realm;
    private RentedAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView list;
    private RentedBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_items);
        setupUI();

        realm = Realm.getDefaultInstance();
        mReceiver = new RentedBroadcastReceiver();
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        list = (RecyclerView) findViewById(R.id.listViewRented);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetReserveRequests);
        mProgressBar.setVisibility(View.GONE);

        sortBy = getResources().getStringArray(R.array.sort_by);
        getRentedItems();
        RealmResults<RentedItem> results = realm.where(RentedItem.class).findAll();
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
        //getCategories();

        /*txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog displayCategories = new AlertDialog.Builder(RentedItemsActivity.this)
                        .setTitle("Categories")
                        .setItems(categoryNames, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtCategory.setText(categories[which].getCategory_name());
                                category = txtCategory.getText().toString();
                                if (category.equals("All")) {
                                    category = "";
                                }
                                listAdapter.getFilter().filter(category);
                            }
                        })
                        .create();
                displayCategories.show();
            }
        });*/
    }

    private void getRentedItems() {
        Intent intent = new Intent(this, RentedItemService.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rented_items, menu);

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
                //listAdapter.getFilter().filter(searchQuery + "," + category);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
               // listAdapter.getFilter().filter(searchQuery + "," + category);
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
        return menuItem.getItemId() != R.id.nav_rented_items;
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtCategory.setText("Categories");

        registerReceiver(mReceiver, new IntentFilter(RentedItemService.class.getCanonicalName()));
        mAdapter.notifyDataSetChanged();

        startService(new Intent(this, ExpirationCheckerService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    /*public void getCategories() {
        mProgressBar.setVisibility(View.GONE);
        Server.getCategories(mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                if (!("".equals(responseBody))) {
                    categories = gson.fromJson(responseBody, Category[].class);
                    categoryNames = new String[categories.length];
                    for(int i=0; i<categories.length; i++){
                        categoryNames[i] =  categories[i].getCategory_name();
                    }
                } else {
                    Toast.makeText(RentedItemsActivity.this, "Empty categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                categories = null;
                Toast.makeText(RentedItemsActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private class RentedBroadcastReceiver extends BroadcastReceiver {

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
