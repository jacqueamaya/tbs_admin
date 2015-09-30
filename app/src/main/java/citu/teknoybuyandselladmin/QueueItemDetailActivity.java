package citu.teknoybuyandselladmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.SellApproval;


public class QueueItemDetailActivity extends BaseActivity {

    private static final String TAG = "QueueItemDetailActivity";
    private static final String REQUEST_ID = "request_id";
    private static final String ITEM_ID = "item_id";
    private static final String CATEGORY = "activity_category";

    private int requestId;
    private int itemId;
    private String mItemName;

    private TextView txtTitle;
    private TextView txtPrice;
    private TextView txtDetails;

    private Spinner category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_on_queue_detail);
        setupUI();

        Intent intent = getIntent();
        requestId = intent.getIntExtra("requestId",0);
        itemId = intent.getIntExtra("itemId",0);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtPrice = (TextView) findViewById(R.id.txtPriceLabel);
        txtDetails = (TextView) findViewById(R.id.txtDetails);
        category = (Spinner) findViewById(R.id.spinner);

        getQueueItemDetails(requestId);
        getCategories();
    }

    public void getQueueItemDetails(int request){
        Map<String,String> data = new HashMap<>();
        data.put(REQUEST_ID,request+"");

        Server.getQueueItemDetails(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<SellApproval> request = new ArrayList<SellApproval>();
                SellApproval sell;
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    request = SellApproval.allSellRequest(jsonArray);
                    sell = request.get(0);

                    mItemName = sell.getItemName();
                    setTitle(mItemName);

                    txtTitle.setText(mItemName);
                    txtPrice.setText("Price: PHP " + sell.getPrice());
                    txtDetails.setText(sell.getDetails());

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                // Toast.makeText(LoginActivity.this, "Error: Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCategories(){
        Server.getCategories(new Ajax.Callbacks(){
            @Override
            public void success(String responseBody) {
                ArrayList<String> categories = new ArrayList<String>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    categories = Category.allCategories(jsonArray);
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(QueueItemDetailActivity.this,R.layout.activity_category,R.id.txtCategory,categories);
                    spinnerAdapter.setDropDownViewResource(R.layout.activity_category);
                    category.setAdapter(spinnerAdapter);
                    spinnerAdapter.notifyDataSetChanged();

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                // Toast.makeText(LoginActivity.this, "Error: Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onApprove(View view){
        Log.v(TAG,"Item ID: "+itemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID,this.itemId+"");
        data.put(REQUEST_ID,this.requestId+"");
        data.put(CATEGORY,category.getSelectedItem().toString());
        Log.v(TAG,category.getSelectedItem().toString());

        Server.approveQueuedItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if(json.getInt("status") == 200){
                        Log.v(TAG,"Successful Approval");
                        Toast.makeText(QueueItemDetailActivity.this, "Item successfully approved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.v(TAG,"approval failed");
                        Toast.makeText(QueueItemDetailActivity.this, "Error; Item approval failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG,"Request error");
                Toast.makeText(QueueItemDetailActivity.this, "Error: Item approval failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDeny(View view){
        Log.v(TAG,"Item ID: "+itemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID,this.itemId+"");
        data.put(REQUEST_ID,this.requestId+"");

        Server.denyQueuedItem(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if(json.getInt("status") == 200){
                        Log.v(TAG,"Successful Disapproval");
                        Toast.makeText(QueueItemDetailActivity.this, "Item successfully disapproved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.v(TAG,"Disapproval failed");
                        Toast.makeText(QueueItemDetailActivity.this, "Error; Item disapproval failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG,"Request error");
                Toast.makeText(QueueItemDetailActivity.this, "Error: Item disapproval failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items_on_queue_detail, menu);
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
