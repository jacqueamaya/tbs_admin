package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselladmin.models.ReservedItem;
import citu.teknoybuyandselladmin.models.SellApproval;


public class ReservedDetailActivity extends BaseActivity {

    private static final String REQUEST_ID = "request_id";
    private static final String TAG = "ReservedDetailActivity";
    private static final String ITEM_ID = "item_id";

    private int requestId;
    private int itemId;

    private TextView txtTitle;
    private TextView txtPrice;
    private TextView txtDetails;

    private ImageView thumbnail;

    private ProgressDialog reserveProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_detail);
        Intent intent = getIntent();
        requestId = intent.getIntExtra("requestId",0);
        itemId = intent.getIntExtra("itemId",0);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtPrice = (TextView) findViewById(R.id.txtPriceLabel);
        txtDetails = (TextView) findViewById(R.id.txtDetails);
        thumbnail = (ImageView) findViewById(R.id.imgThumbnail);

        reserveProgress = new ProgressDialog(this);

        getReservedDetails(requestId);
    }

    public void getReservedDetails(int request){
        Map<String,String> data = new HashMap<>();
        data.put(REQUEST_ID,request+"");

        Server.getReservedItemDetails(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<ReservedItem> request = new ArrayList<ReservedItem>();
                ReservedItem reserve;
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    request = ReservedItem.allReservedItems(jsonArray);
                    reserve = request.get(0);
                    Picasso.with(ReservedDetailActivity.this)
                            .load(reserve.getLink())
                            .into(thumbnail);
                    txtTitle.setText(reserve.getItemName());
                    txtPrice.setText("Price: PHP " + reserve.getPrice());
                    txtDetails.setText(reserve.getDetails());

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

    public void onAvailable(View view){
        Log.v(TAG, "Item ID: " + itemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID,this.itemId+"");
        data.put(REQUEST_ID, this.requestId + "");

        reserveProgress.setIndeterminate(true);
        reserveProgress.setMessage("Please wait. . .");

        Server.itemAvailable(data, reserveProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if(json.getInt("status") == 200){
                        Log.v(TAG,"Successfully set item available");
                        Toast.makeText(ReservedDetailActivity.this, "Item successfully set available", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.v(TAG,"failed");
                        Toast.makeText(ReservedDetailActivity.this, "Error: Item set availability failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG,"Request error");
                Toast.makeText(ReservedDetailActivity.this, "Error: Item set availability failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClaimed(View view){
        Log.v(TAG,"Item ID: "+itemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID,this.itemId+"");
        data.put(REQUEST_ID,this.requestId+"");

        reserveProgress.setIndeterminate(true);
        reserveProgress.setMessage("Please wait. . .");

        Server.itemClaimed(data,reserveProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if(json.getInt("status") == 200){
                        Log.v(TAG,"Successfully claimed");
                        Toast.makeText(ReservedDetailActivity.this, "Item successfully claimed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.v(TAG,"failed");
                        Toast.makeText(ReservedDetailActivity.this, "Error: Failed to claim item", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG,"Request error");
                Toast.makeText(ReservedDetailActivity.this, "Error: Failed to claim item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reserved_detail, menu);
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
        return menuItem.getItemId() != R.id.nav_reserved_items;
    }
}
