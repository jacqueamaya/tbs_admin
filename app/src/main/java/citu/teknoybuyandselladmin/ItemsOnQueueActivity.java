package citu.teknoybuyandselladmin;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.ListAdapters.SellApprovalAdapter;
import citu.teknoybuyandselladmin.models.SellApproval;


public class ItemsOnQueueActivity extends BaseActivity {

    private static final String TAG = "ItemsOnQueueActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_on_queue);
        setupUI();

        getReservedItems();
    }

    public void getReservedItems(){
        Server.getSellRequests(new Ajax.Callbacks() {
            @Override
            public void success(final String responseBody) {
                ArrayList<SellApproval> request = new ArrayList<SellApproval>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    request = SellApproval.allSellRequest(jsonArray);

                    ListView lv = (ListView) findViewById(R.id.listViewQueue);
                    SellApprovalAdapter listAdapter = new SellApprovalAdapter(ItemsOnQueueActivity.this, R.layout.activity_item, request);
                    lv.setAdapter(listAdapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SellApproval sell = (SellApproval) parent.getItemAtPosition(position);
                            int itemId = sell.getItemId();
                            int requestId = sell.getRequestId();
                            Log.v(TAG,"Item id: "+itemId);
                            Log.v(TAG,"Request id: "+ requestId);

                            Intent intent;
                            intent = new Intent(ItemsOnQueueActivity.this, QueueItemDetailActivity.class);
                            intent.putExtra("itemId",itemId);
                            intent.putExtra("requestId",requestId);
                            startActivity(intent);

                        }
                    });

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items_on_queue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_items_queue;
    }
}
