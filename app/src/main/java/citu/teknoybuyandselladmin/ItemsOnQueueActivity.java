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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.adapters.SellApprovalAdapter;
import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.Notification;
import citu.teknoybuyandselladmin.models.SellApproval;


public class ItemsOnQueueActivity extends BaseActivity {

    private static final String TAG = "ItemsOnQueueActivity";
   // private JSONArray jsonArray;

    private ProgressBar mProgressBar;

    private SellApprovalAdapter listAdapter;

    private String sortBy[];

    private String searchQuery = "";
    String lowerCaseSort = "date";

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_on_queue);
        setupUI();

        mProgressBar = (ProgressBar) findViewById(R.id.progressGetSellRequests);
        mProgressBar.setVisibility(View.GONE);

        sortBy = getResources().getStringArray(R.array.sort_by);

        getSellRequests();
    }

    public void getSellRequests() {
        Server.getSellRequests(mProgressBar, new Ajax.Callbacks() {
            TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
            ListView lv = (ListView) findViewById(R.id.listViewQueue);

            @Override
            public void success(final String responseBody) {
                ArrayList<SellApproval> request = new ArrayList<SellApproval>();
                Log.v(TAG, responseBody);

                //try {
                    request = gson.fromJson(responseBody, new TypeToken<ArrayList<SellApproval>>(){}.getType());
                    if (request.size() == 0) {
                        txtMessage.setText("No sell requests available");
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        txtMessage.setVisibility(View.GONE);
                        //request = SellApproval.asList(jsonArray);
                        listAdapter = new SellApprovalAdapter(ItemsOnQueueActivity.this, R.layout.list_item, request);
                        listAdapter.sortItems(lowerCaseSort);
                        lv.setAdapter(listAdapter);

                        Spinner spinnerSortBy = (Spinner) findViewById(R.id.spinnerSortBy);
                        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                lowerCaseSort = sortBy[position].toLowerCase();
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
                                SellApproval sellRequest = listAdapter.getDisplayView().get(position);

                                Intent intent;
                                intent = new Intent(ItemsOnQueueActivity.this, QueueItemDetailActivity.class);
                                intent.putExtra("itemId", sellRequest.getItem().getId());
                                intent.putExtra("requestId", sellRequest.getId());
                                intent.putExtra("itemName", sellRequest.getItem().getName());
                                intent.putExtra("itemPrice", sellRequest.getItem().getPrice());
                                intent.putExtra("itemDetail", sellRequest.getItem().getDescription());
                                intent.putExtra("itemLink", sellRequest.getItem().getPicture());
                                intent.putExtra("itemCategory", sellRequest.getItem().getCategory().getCategory_name());
                                intent.putExtra("itemPurpose", sellRequest.getItem().getPurpose());
                                startActivity(intent);
                            }
                        });
                    }

               /* } catch (JSONException e1) {
                    e1.printStackTrace();
                }*/
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                txtMessage.setText("Connection Error: Cannot connect to server.");
                txtMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items_on_queue, menu);

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
                listAdapter.getFilter().filter(searchQuery);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                listAdapter.getFilter().filter(searchQuery);
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
        getSellRequests();
    }
}
