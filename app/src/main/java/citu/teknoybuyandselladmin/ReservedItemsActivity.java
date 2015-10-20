package citu.teknoybuyandselladmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.adapters.ReservedItemListAdapter;
import citu.teknoybuyandselladmin.models.ReservedItem;


public class ReservedItemsActivity extends BaseActivity {

    private static final String TAG = "ReservedActivity";
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_items);
        setupUI();
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetReserveRequests);

        getReservedItems();
    }

    public void getReservedItems(){
        Server.getReservedItems(mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<ReservedItem> reserved = new ArrayList<ReservedItem>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
                ListView lv = (ListView) findViewById(R.id.listViewReserved);

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        txtMessage.setText("No reserved items");
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        reserved = ReservedItem.asList(jsonArray);
                        ReservedItemListAdapter listAdapter = new ReservedItemListAdapter(ReservedItemsActivity.this, R.layout.activity_item, reserved);

                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ReservedItem reserve = (ReservedItem) parent.getItemAtPosition(position);

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
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_reserved_items;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReservedItems();
    }
}
