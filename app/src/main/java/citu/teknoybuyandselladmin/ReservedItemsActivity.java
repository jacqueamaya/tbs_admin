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

import citu.teknoybuyandselladmin.adapters.ReservedItemListAdapter;
import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.Reservation;


public class ReservedItemsActivity extends BaseActivity {

    private static final String TAG = "ReservedActivity";
    private ProgressBar mProgressBar;
    private TextView txtCategory;
    private ReservedItemListAdapter listAdapter;

    private Category categories[];
    private String categoryNames[];
    private String sortBy[];

    private String searchQuery = "";
    private String category = "";
    private String lowerCaseSort = "date";

    private Gson gson = new Gson();

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
        });
    }

    public void getReservedItems(){
        Server.getReservedItems(mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Reservation> reservationRequest = new ArrayList<Reservation>();
                reservationRequest = gson.fromJson(responseBody, new TypeToken<ArrayList<Reservation>>(){}.getType());

                TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
                ListView lv = (ListView) findViewById(R.id.listViewReserved);

               // try {
                //    jsonArray = new JSONArray(responseBody);
                    if (reservationRequest.size() == 0) {
                        txtMessage.setText("No reserved items");
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        txtMessage.setVisibility(View.GONE);
                        //reserved = Reservation.asList(jsonArray);
                        listAdapter = new ReservedItemListAdapter(ReservedItemsActivity.this, R.layout.list_item, reservationRequest);
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
                                Reservation reserve = listAdapter.getDisplayView().get(position);

                                Intent intent;
                                intent = new Intent(ReservedItemsActivity.this, ReservedDetailActivity.class);
                                intent.putExtra("itemId", reserve.getItem().getId());
                                intent.putExtra("requestId", reserve.getId());
                                intent.putExtra("itemName", reserve.getItem().getName());
                                intent.putExtra("itemStatus", reserve.getStatus());
                                intent.putExtra("itemPrice", reserve.getItem().getPrice());
                                intent.putExtra("itemDetail", reserve.getItem().getDescription());
                                intent.putExtra("itemLink", reserve.getItem().getPicture());
                                intent.putExtra("itemStarsRequired", reserve.getItem().getStars_required());
                                startActivity(intent);
                            }
                        });
                    }
               /* } catch (JSONException e1) {
                    Log.e(TAG, "JSONException", e1);
                }*/
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
        txtCategory.setText("Categories");
        getReservedItems();

        Intent service = new Intent(ReservedItemsActivity.this, ExpirationCheckerService.class);
        startService(service);
    }

    public void getCategories() {
        mProgressBar.setVisibility(View.GONE);
        Server.getCategories(mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                //try {
                    //JSONArray json = new JSONArray(responseBody);
                    if (!("".equals(responseBody))) {
                        categories = gson.fromJson(responseBody, Category[].class);
                        categoryNames = new String[categories.length];
                        for(int i=0; i<categories.length; i++){
                            categoryNames[i] =  categories[i].getCategory_name();
                        }
                    } else {
                        Toast.makeText(ReservedItemsActivity.this, "Empty categories", Toast.LENGTH_SHORT).show();
                    }
               /* } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                categories = null;
                Toast.makeText(ReservedItemsActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
