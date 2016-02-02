package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import citu.teknoybuyandselladmin.services.ItemReturnedService;


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
    private Button mBtnReturned;
   // private Button mBtnNotify;

    private ProgressDialog mRentProgress;
    private RentedBroadcastReceiver mReceiver;


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
        mItemQuantity = intent.getIntExtra("itemQuantity", 0);
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
        //mBtnNotify = (Button) findViewById(R.id.btnNotify);
        mTxtRentDate = (TextView) findViewById(R.id.txtRentDate);
        mTxtRentExpiry = (TextView) findViewById(R.id.txtRentExpiry);

        mReceiver = new RentedBroadcastReceiver();
        mRentProgress = new ProgressDialog(this);
        mRentProgress.setCancelable(false);

        getRentDetails();
    }

    public void getRentDetails() {
        setTitle(mItemName);
        Picasso.with(RentedItemDetailActivity.this)
                .load(mItemLink)
                .into(mThumbnail);
        mTxtTitle.setText(mItemName);
        mTxtRentedBy.setText("Rented by: " + mRenter);
        mTxtRentDate.setText("Rent Date: " + Utils.parseDate(mRentDate));
        mTxtRentExpiry.setText("Rent Expiration: " + Utils.parseDate(mRentExpiry));
        mTxtDetails.setText(mItemDetail);

        if (mItemStarsRequired == 0) {
            mTxtPrice.setText("Price: PHP " + Utils.formatFloat(mItemPrice));
        } else {
            mTxtPrice.setText("Stars Required: " + mItemStarsRequired);
        }

    }

    public void onReturned(View view) {
        Log.e(TAG, "return item?");
        Utils.alert(RentedItemDetailActivity.this, "Return Item", "Return rented item?", new Utils.Callbacks() {
            @Override
            public void ok() {
                setItemReturned();
                Log.e(TAG, "yes, return item");
            }
        });
    }

    public void setItemReturned() {
        Intent intent = new Intent(this, ItemReturnedService.class);
        intent.putExtra("requestId", mRentId);
        intent.putExtra("itemId", mItemId);
        startService(intent);
    }

    public void onNotifyRenter(View view) {
        Utils.alert(RentedItemDetailActivity.this, "Notify Renter", "Remind renter?", new Utils.Callbacks() {
            @Override
            public void ok() {
                // notifyRenter();
            }
        });
    }

    /*public void notifyRenter(){
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
    }*/

    public void clodeActivity(){
        RentedItemDetailActivity.this.finish();
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_reserved_items;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ItemReturnedService.ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private class RentedBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, intent.getStringExtra("response"));
            String response = intent.getStringExtra("response");

            if (intent.getIntExtra("result", 0) == -1) {
                Snackbar.make(mTxtDetails, "No internet connection", Snackbar.LENGTH_SHORT).show();
            } else {
                if ("return_item".equals(response)) {
                    Log.e(TAG, "Successfully returned item");
                    clodeActivity();
                }
            }
        }
    }

}
