package citu.teknoybuyandselladmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.adapters.ReservedItemListAdapter;
import citu.teknoybuyandselladmin.models.ReservedItem;


public class ReservedItemsActivity extends BaseActivity {

    private static final String TAG = "ReservedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_items);
        setupUI();
        getReservedItems();
    }

    public void getReservedItems(){
        Server.getReservedItems(new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<ReservedItem> reserved = new ArrayList<ReservedItem>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
                        txtMessage.setText("No reserved items");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        reserved = ReservedItem.asList(jsonArray);

                        ListView lv = (ListView) findViewById(R.id.listViewReserved);
                        ReservedItemListAdapter listAdapter = new ReservedItemListAdapter(ReservedItemsActivity.this, R.layout.activity_item, reserved);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ReservedItem reserve = (ReservedItem) parent.getItemAtPosition(position);
                                int itemId = reserve.getItemId();
                                int requestId = reserve.getRequestId();

                                Intent intent;
                                intent = new Intent(ReservedItemsActivity.this, ReservedDetailActivity.class);
                                intent.putExtra("itemId", itemId);
                                intent.putExtra("requestId", requestId);
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
