package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import citu.teknoybuyandselladmin.services.ItemReturnedService;


public class RentedItemDetailActivity extends AppCompatActivity {

    public static final String RENT_ID = "rent_id";
    private static final String TAG = "RentedItemDetail";
    public static final String ITEM_ID = "item_id";

    private int mRentId;
    private int mItemId;
    private int mItemStarsRequired;
    private int mItemQuantity;
    private int mRentDuration;

    private float mPenalty;

    private String mItemName;
    private String mItemDetail;
    private String mItemLink;
    private String mItemCode;
    private String mRenter;
    private String mOwner;

    private long mRentDate;
    private long mRentExpiry;

    private TextView mTxtTitle;
    private TextView mTxtPenalty;
    private TextView mTxtDetails;
    private TextView mTxtRenter;
    private TextView mTxtOwner;
    private TextView mTxtQuantity;
    private TextView mTxtItemCode;
    private TextView mTxtRentDate;
    private TextView mTxtRentExpiry;
    private TextView mTxtRentDuration;

    private SimpleDraweeView mItem;

    private Button mBtnReturned;
   // private Button mBtnNotify;

    private ProgressDialog mRentProgress;
    private RentedBroadcastReceiver mReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_item_detail);
        setupToolbar();

        Intent intent = getIntent();
        mRentId = intent.getIntExtra("rentId", 0);
        mItemId = intent.getIntExtra("itemId", 0);
        mItemName = intent.getStringExtra("itemName");
        mItemDetail = intent.getStringExtra("itemDetail");
        mPenalty = intent.getFloatExtra("itemPenalty", 0);
        mItemLink = intent.getStringExtra("itemLink");
        mItemQuantity = intent.getIntExtra("itemQuantity", 0);
        mItemCode = intent.getStringExtra("itemCode");
        mRentDuration = intent.getIntExtra("rentDuration",0);
        mRenter = intent.getStringExtra("renter");
        mOwner = intent.getStringExtra("owner");
        mRentDate = intent.getLongExtra("rentDate", 0);
        mRentExpiry = intent.getLongExtra("rentExpiry", 0);

        mTxtTitle = (TextView) findViewById(R.id.txtItem);
        mTxtItemCode = (TextView) findViewById(R.id.txtItemCode);
        mTxtQuantity = (TextView) findViewById(R.id.txtQuantity);
        mTxtPenalty = (TextView) findViewById(R.id.txtPenalty);
        mTxtRentDuration = (TextView) findViewById(R.id.txtRentDuration);
        mTxtRenter = (TextView) findViewById(R.id.txtRenter);
        mTxtOwner = (TextView) findViewById(R.id.txtOwner);
        mTxtDetails = (TextView) findViewById(R.id.txtDescription);
        mTxtRentDate = (TextView) findViewById(R.id.txtRentDate);
        mTxtRentExpiry = (TextView) findViewById(R.id.txtRentExpiry);

        mBtnReturned = (Button) findViewById(R.id.btnReturned);
        mItem = (SimpleDraweeView) findViewById(R.id.imgItem);

        mReceiver = new RentedBroadcastReceiver();
        mRentProgress = new ProgressDialog(this);
        mRentProgress.setCancelable(false);

        getRentDetails();
    }

    public void getRentDetails() {
        setTitle(mItemName);
        /*Picasso.with(RentedItemDetailActivity.this)
                .load(mItemLink)
                .into(mThumbnail);*/
        mItem.setImageURI(Uri.parse(mItemLink));
        mTxtTitle.setText(mItemName);
        mTxtItemCode.setText(" "+mItemCode);
        mTxtQuantity.setText(" "+mItemQuantity);
        mTxtPenalty.setText("Php "+mPenalty);
        mTxtRentDuration.setText(mRentDuration+" days");
        mTxtOwner.setText(mOwner);
        mTxtRenter.setText(mRenter);
        mTxtRentDate.setText(Utils.parseDate(mRentDate));
        mTxtRentExpiry.setText(Utils.parseDate(mRentExpiry));
        mTxtDetails.setText(mItemDetail);

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
