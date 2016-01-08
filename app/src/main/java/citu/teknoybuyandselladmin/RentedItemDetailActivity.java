package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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


public class RentedItemDetailActivity extends BaseActivity {

    public static final String RENT_ID = "rent_id";
    private static final String TAG = "RentedItemDetail";
    public static final String ITEM_ID = "item_id";

    private int mRentId;
    private int mItemId;
    private int mItemStarsRequired;
    private int mItemQuantity;

    private float mItemPrice;
    private String mItemName;
    private String mItemDetail;
    private String mItemLink;
    private String mItemCode;
    private String mRenter;

    private long mRentDate;
    private long mRentExpiry;

    private TextView mTxtTitle;
    private TextView mTxtPrice;
    private TextView mTxtDetails;
    private TextView mTxtRentedBy;
    private TextView mTxtRentDate;
    private TextView mTxtRentExpiry;

    private ImageView mThumbnail;
    private Button  mBtnReturned;
    private Button mBtnNotify;

    private ProgressDialog mRentProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_item_detail);
        setupUI();

        Intent intent = getIntent();
        mRentId = intent.getIntExtra("rentId", 0);
        mItemId = intent.getIntExtra("itemId", 0);
        mItemName = intent.getStringExtra("itemName");
        mItemDetail = intent.getStringExtra("itemDetail");
        mItemPrice = intent.getFloatExtra("itemPrice", 0);
        mItemLink = intent.getStringExtra("itemLink");
        mItemStarsRequired = intent.getIntExtra("itemStarsRequired", 0);
        mItemQuantity =  intent.getIntExtra("itemQuantity", 0);
        mItemCode = intent.getStringExtra("itemCode");
        mRenter = intent.getStringExtra("renter");
        mRentDate = intent.getLongExtra("rentDate", 0);
        mRentExpiry = intent.getLongExtra("rentExpiry", 0);

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mTxtRentedBy = (TextView) findViewById(R.id.txtRentedBy);
        mTxtPrice = (TextView) findViewById(R.id.txtPriceLabel);
        mTxtDetails = (TextView) findViewById(R.id.txtDetails);
        mThumbnail = (ImageView) findViewById(R.id.imgThumbnail);
        mBtnReturned = (Button) findViewById(R.id.btnReturned);
        mBtnNotify = (Button) findViewById(R.id.btnNotify);
        mTxtRentDate = (TextView) findViewById(R.id.txtRentDate);
        mTxtRentExpiry = (TextView) findViewById(R.id.txtRentExpiry);

        mRentProgress = new ProgressDialog(this);
        mRentProgress.setCancelable(false);

        getRentDetails();
    }

    public void getRentDetails(){
        setTitle(mItemName);
        Picasso.with(RentedItemDetailActivity.this)
                .load(mItemLink)
                .into(mThumbnail);
        mTxtTitle.setText(mItemName);
        mTxtRentedBy.setText("Rented by: " + mRenter);
        mTxtRentDate.setText("Rent Date: " + Utils.parseDate(mRentDate));
        mTxtRentExpiry.setText("Rent Expiration: " + Utils.parseDate(mRentExpiry));
        mTxtDetails.setText(mItemDetail);

        if(mItemStarsRequired == 0) {
            mTxtPrice.setText("Price: PHP " + Utils.formatFloat(mItemPrice));
        } else {
            mTxtPrice.setText("Stars Required: " + mItemStarsRequired);
        }

    }

    public void onReturned(View view) {
        Utils.alert(RentedItemDetailActivity.this, "Return Item", "Return rented item?", new Utils.Callbacks() {
            @Override
            public void ok() {
                setItemReturned();
            }
        });
    }

    public void setItemReturned(){
        Log.v(TAG, "Item REQUEST_ID: " + mItemId);
        Map<String, String> data = new HashMap<>();

        data.put(ITEM_ID, mItemId + "");
        data.put(RENT_ID, mRentId + "");

        mRentProgress.setIndeterminate(true);
        mRentProgress.setMessage("Please wait. . .");

        Server.returnRentedItem(data, mRentProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Successfully set item returned");
                        Toast.makeText(RentedItemDetailActivity.this, "Rented item was returned", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.v(TAG, "failed");
                        Toast.makeText(RentedItemDetailActivity.this, "Error: Failed to return item", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(RentedItemDetailActivity.this, "Error: Failed to return item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onNotifyRenter(View view) {
        Utils.alert(RentedItemDetailActivity.this, "Notify Renter", "Remind renter?", new Utils.Callbacks() {
            @Override
            public void ok() {
                notifyRenter();
            }
        });
    }

    public void notifyRenter(){
        Log.v(TAG, "Item ITEM_ID: " + mItemId);
        Log.v(TAG, "Item REQUEST_ID: " + mRentId);
        Map<String, String> data = new HashMap<>();

        data.put(ITEM_ID, mItemId + "");
        data.put(RENT_ID, mRentId + "");

        mRentProgress.setIndeterminate(true);
        mRentProgress.setMessage("Please wait. . .");

        Server.notifyRenter(data, mRentProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Successfully claimed");
                        Toast.makeText(RentedItemDetailActivity.this, "Notification sent", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.v(TAG, "failed");
                        Toast.makeText(RentedItemDetailActivity.this, "Error: Failed to send notification", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(RentedItemDetailActivity.this, "Error: Failed to send notification", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_reserved_items;
    }
}
