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

import citu.teknoybuyandselladmin.adapters.SellApprovalAdapter;
import citu.teknoybuyandselladmin.models.SellApproval;


public class ItemsOnQueueActivity extends BaseActivity {

    private static final String TAG = "ItemsOnQueueActivity";
    private JSONArray jsonArray;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_on_queue);
        setupUI();

        mProgressBar = (ProgressBar) findViewById(R.id.progressGetSellRequests);

        getReservedItems();
    }

    public void getReservedItems(){
        Server.getSellRequests(mProgressBar, new Ajax.Callbacks() {
            TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
            ListView lv = (ListView) findViewById(R.id.listViewQueue);

            @Override
            public void success(final String responseBody) {
                ArrayList<SellApproval> request = new ArrayList<SellApproval>();
                Log.v(TAG, responseBody);

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        txtMessage.setText("No sell requests available");
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        request = SellApproval.asList(jsonArray);
                        SellApprovalAdapter listAdapter = new SellApprovalAdapter(ItemsOnQueueActivity.this, R.layout.activity_item, request);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                SellApproval sell = (SellApproval) parent.getItemAtPosition(position);
                                //int itemId = sell.getItemId();
                                //int requestId = sell.getRequestId();

                                Intent intent;
                                intent = new Intent(ItemsOnQueueActivity.this, QueueItemDetailActivity.class);
                                intent.putExtra("itemId",sell.getItemId());
                                intent.putExtra("requestId",sell.getRequestId());
                                intent.putExtra("itemName", sell.getItemName());
                                intent.putExtra("itemPrice", sell.getPrice());
                                intent.putExtra("itemDetail", sell.getDetails());
                                intent.putExtra("itemLink", sell.getLink());
                                intent.putExtra("itemCategoy", sell.getCategory());
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
