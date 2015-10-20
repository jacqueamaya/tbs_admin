package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ReservedDetailActivity extends BaseActivity {

    private static final String REQUEST_ID = "request_id";
    private static final String TAG = "ReservedDetailActivity";
    private static final String ITEM_ID = "item_id";

    private int mRequestId;
    private int mItemId;

    private float mItemPrice;

    private String mItemName;
    private String mItemDetail;
    private String mItemStatus;
    private String mItemLink;

    private TextView mTxtTitle;
    private TextView mTxtPrice;
    private TextView mTxtDetails;

    private ImageView mThumbnail;
    private Button mBtnAvailable;
    private Button mBtnClaimed;

    private ProgressDialog mReserveProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_detail);
        setupUI();

        Intent intent = getIntent();
        mRequestId = intent.getIntExtra("requestId", 0);
        mItemId = intent.getIntExtra("itemId", 0);
        mItemName = intent.getStringExtra("itemName");
        mItemDetail = intent.getStringExtra("itemDetail");
        mItemPrice = intent.getFloatExtra("itemPrice", 0);
        mItemLink = intent.getStringExtra("itemLink");
        mItemStatus = intent.getStringExtra("itemStatus");

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mTxtPrice = (TextView) findViewById(R.id.txtPriceLabel);
        mTxtDetails = (TextView) findViewById(R.id.txtDetails);
        mThumbnail = (ImageView) findViewById(R.id.imgThumbnail);
        mBtnAvailable = (Button) findViewById(R.id.imgAvailable);
        mBtnClaimed = (Button) findViewById(R.id.imgClaimed);

        mReserveProgress = new ProgressDialog(this);
        mReserveProgress.setCancelable(false);

        getReservedDetails();
    }

    public void getReservedDetails(){
        setTitle(mItemName);
        Picasso.with(ReservedDetailActivity.this)
                .load(mItemLink)
                .into(mThumbnail);
        mTxtTitle.setText(mItemName);
        mTxtDetails.setText(mItemDetail);
        mTxtPrice.setText("Price: PHP " +mItemPrice);

        if("Reserved".equals(mItemStatus)){
            Log.v(TAG,mItemStatus+" Item reserved. Claimed button disabled");
            mBtnClaimed.setEnabled(false);
        }
        else if("Available".equals(mItemStatus)){
            Log.v(TAG,mItemStatus+" Item available. Available button disabled");
            mBtnAvailable.setEnabled(false);
        }

    }

    public void onAvailable(View view) {
        Utils.alert(ReservedDetailActivity.this, "Item Available", "Set item status to available?", new Utils.Callbacks() {
            @Override
            public void ok() {
                setItemAvailable();
            }
        });
    }

    public void setItemAvailable(){
        Log.v(TAG, "Item REQUEST_ID: " + mItemId);
        Map<String, String> data = new HashMap<>();

        data.put(ITEM_ID, mItemId + "");
        data.put(REQUEST_ID, mRequestId + "");

        mReserveProgress.setIndeterminate(true);
        mReserveProgress.setMessage("Please wait. . .");

        Server.itemAvailable(data, mReserveProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Successfully set item imgAvailable");
                        Toast.makeText(ReservedDetailActivity.this, "Item successfully set available", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.v(TAG, "failed");
                        Toast.makeText(ReservedDetailActivity.this, "Error: Item set availability failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(ReservedDetailActivity.this, "Connection Error: Item set availability failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClaimed(View view) {
        Utils.alert(ReservedDetailActivity.this, "Item Claimed", "Item claimed by the buyer/donee?", new Utils.Callbacks() {
            @Override
            public void ok() {
                claimItem();
            }
        });
    }

    public void claimItem(){
        Log.v(TAG, "Item ITEM_ID: " + mItemId);
        Log.v(TAG, "Item REQUEST_ID: " + mRequestId);
        Map<String, String> data = new HashMap<>();

        data.put(ITEM_ID, mItemId + "");
        data.put(REQUEST_ID, mRequestId + "");

        mReserveProgress.setIndeterminate(true);
        mReserveProgress.setMessage("Please wait. . .");

        Server.itemClaimed(data, mReserveProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Successfully claimed");
                        Toast.makeText(ReservedDetailActivity.this, "Item successfully claimed", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.v(TAG, "failed");
                        Toast.makeText(ReservedDetailActivity.this, "Error: Failed to claim item", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(ReservedDetailActivity.this, "Connection Error: Failed to claim item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reserved_detail, menu);
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
        return menuItem.getItemId() != R.id.nav_reserved_items;
    }
}
