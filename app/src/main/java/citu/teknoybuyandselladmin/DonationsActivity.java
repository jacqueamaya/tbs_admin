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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.adapters.DonateApprovalAdapter;
import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.DonateApproval;
import citu.teknoybuyandselladmin.models.SellApproval;


public class DonationsActivity extends BaseActivity {

    private static final String TAG = "DonatedActivity";

    private DonateApprovalAdapter listAdapter;

    private ProgressBar mProgressBar;

    private String sortBy[];

    private String searchQuery = "";
    private String lowerCaseSort = "date";

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);
        setupUI();

        mProgressBar = (ProgressBar) findViewById(R.id.progressGetDonationsRequests);
        mProgressBar.setVisibility(View.GONE);

        sortBy = getResources().getStringArray(R.array.donate_sort_by);
        getDonatedItems();
    }

    public void getDonatedItems(){
        Server.getDonateRequests(mProgressBar, new Ajax.Callbacks() {

            ListView lv = (ListView) findViewById(R.id.listViewDonations);
            TextView txtMessage = (TextView) findViewById(R.id.txtMessage);

            @Override
            public void success(String responseBody) {
                ArrayList<DonateApproval> donateRequests = new ArrayList<DonateApproval>();
               // JSONArray jsonArray = null;
               // try {
               //     jsonArray = new JSONArray(responseBody);
                donateRequests = gson.fromJson(responseBody,new TypeToken<ArrayList<DonateApproval>>(){}.getType());
                    if (donateRequests.size() == 0) {
                        txtMessage.setText("No donate requests available");
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        txtMessage.setVisibility(View.GONE);
                        //request = DonateApproval.asList(jsonArray);
                        listAdapter = new DonateApprovalAdapter(DonationsActivity.this, R.layout.list_item, donateRequests);
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
                                DonateApproval donate = listAdapter.getDisplayView().get(position);
                                Log.e(TAG, "item id: " + donate.getItem().getId());
                                Log.e(TAG,"request id: "+donate.getId());

                                Intent intent;
                                intent = new Intent(DonationsActivity.this, DonationsDetailActivity.class);
                                intent.putExtra("itemId", donate.getItem().getId());
                                intent.putExtra("requestId", donate.getId());
                                intent.putExtra("itemName", donate.getItem().getName());
                                intent.putExtra("itemDetail", donate.getItem().getDescription());
                                intent.putExtra("itemCategory", donate.getItem().getCategory().getCategory_name());
                                intent.putExtra("itemLink", donate.getItem().getPicture());


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
        inflater.inflate(R.menu.menu_donations, menu);

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
        return menuItem.getItemId() != R.id.nav_donations;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDonatedItems();
    }
}
