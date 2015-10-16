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

import citu.teknoybuyandselladmin.adapters.SellApprovalAdapter;
import citu.teknoybuyandselladmin.models.SellApproval;


public class ItemsOnQueueActivity extends BaseActivity {

    private static final String TAG = "ItemsOnQueueActivity";
    private JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_on_queue);
        setupUI();

        getReservedItems();
    }

    public void getReservedItems(){
        Server.getSellRequests(new Ajax.Callbacks() {
            TextView txtMessage = (TextView) findViewById(R.id.txtMessage);

            @Override
            public void success(final String responseBody) {
                ArrayList<SellApproval> request = new ArrayList<SellApproval>();
                Log.v(TAG, responseBody);

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        txtMessage.setText("No sell requests available");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        request = SellApproval.asList(jsonArray);

                        ListView lv = (ListView) findViewById(R.id.listViewQueue);
                        SellApprovalAdapter listAdapter = new SellApprovalAdapter(ItemsOnQueueActivity.this, R.layout.activity_item, request);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                SellApproval sell = (SellApproval) parent.getItemAtPosition(position);
                                int itemId = sell.getItemId();
                                int requestId = sell.getRequestId();

                                Intent intent;
                                intent = new Intent(ItemsOnQueueActivity.this, QueueItemDetailActivity.class);
                                intent.putExtra("itemId",itemId);
                                intent.putExtra("requestId",requestId);
                                startActivity(intent);

                            }
                        });
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                txtMessage.setText("Connection Error: Cannot connect to server. Please check your internet connection");
                txtMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items_on_queue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_items_queue;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReservedItems();
    }
}
