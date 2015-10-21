package citu.teknoybuyandselladmin;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.adapters.ReservedItemListAdapter;
import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.ReservedItem;


public class ReservedItemsActivity extends BaseActivity {

    private static final String TAG = "ReservedActivity";
    private ProgressBar mProgressBar;
    private TextView txtCategory;
    private ReservedItemListAdapter listAdapter;

    private String categories[];
    private String sortBy[];

    private String searchQuery = "";
    private String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_items);
        setupUI();

        txtCategory = (TextView) findViewById(R.id.txtCategory);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetReserveRequests);
        mProgressBar.setVisibility(View.GONE);

        sortBy = getResources().getStringArray(R.array.sort_by);
        getReservedItems();
        getCategories();

        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog displayCategories = new AlertDialog.Builder(ReservedItemsActivity.this)
                        .setTitle("Categories")
                        .setItems(categories, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtCategory.setText(categories[which]);
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
        });
    }

    public void getReservedItems(){
        Server.getReservedItems(mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<ReservedItem> reserved;
                JSONArray jsonArray;

                TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
                ListView lv = (ListView) findViewById(R.id.listViewReserved);

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        txtMessage.setText("No reserved items");
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        txtMessage.setVisibility(View.GONE);
                        reserved = ReservedItem.asList(jsonArray);
                        listAdapter = new ReservedItemListAdapter(ReservedItemsActivity.this, R.layout.list_item, reserved);
                        listAdapter.sortItems("date");
                        lv.setAdapter(listAdapter);

                        Spinner spinnerSortBy = (Spinner) findViewById(R.id.spinnerSortBy);
                        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String lowerCaseSort = sortBy[position].toLowerCase();
                                Log.d(TAG, lowerCaseSort);
                                listAdapter.sortItems(lowerCaseSort);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ReservedItem reserve = listAdapter.getDisplayView().get(position);

                                Intent intent;
                                intent = new Intent(ReservedItemsActivity.this, ReservedDetailActivity.class);
                                intent.putExtra("itemId", reserve.getItemId());
                                intent.putExtra("requestId", reserve.getRequestId());
                                intent.putExtra("itemName", reserve.getItemName());
                                intent.putExtra("itemStatus", reserve.getStatus());
                                intent.putExtra("itemPrice", reserve.getPrice());
                                intent.putExtra("itemDetail", reserve.getDetails());
                                intent.putExtra("itemLink", reserve.getLink());
                                startActivity(intent);
                            }
                        });
                    }
                } catch (JSONException e1) {
                    Log.e(TAG, "JSONException", e1);
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.e(TAG, "Request error");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reserved_items, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
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
                listAdapter.getFilter().filter(searchQuery + "," + category);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                listAdapter.getFilter().filter(searchQuery + "," + category);
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
        return menuItem.getItemId() != R.id.nav_reserved_items;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReservedItems();
    }

    public void getCategories() {
        mProgressBar.setVisibility(View.GONE);
        Server.getCategories(mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONArray json = new JSONArray(responseBody);
                    if (json.length() != 0) {
                        categories = Category.asArray(new JSONArray(responseBody));
                    } else {
                        Toast.makeText(ReservedItemsActivity.this, "Empty categories", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                categories = null;
                Toast.makeText(ReservedItemsActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
