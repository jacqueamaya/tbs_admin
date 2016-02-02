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
import citu.teknoybuyandselladmin.services.ItemAvailableService;
import citu.teknoybuyandselladmin.services.ItemClaimedService;
import io.realm.Realm;


public class ReservedDetailActivity extends BaseActivity {

    private static final String REQUEST_ID = "request_id";
    private static final String TAG = "ReservedDetailActivity";
    private static final String ITEM_ID = "item_id";

    private int mRequestId;
    private int mItemId;
    private int mItemStarsRequired;

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

    private ReservedDetailBroadcastReceiver mReceiver;
    private Realm realm;
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
        mItemStarsRequired = intent.getIntExtra("starsRequired", 0);

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mTxtPrice = (TextView) findViewById(R.id.txtPriceLabel);
        mTxtDetails = (TextView) findViewById(R.id.txtDetails);
        mThumbnail = (ImageView) findViewById(R.id.imgThumbnail);
        mBtnAvailable = (Button) findViewById(R.id.imgAvailable);
        mBtnClaimed = (Button) findViewById(R.id.imgClaimed);

        mReceiver = new ReservedDetailBroadcastReceiver();
        realm = Realm.getDefaultInstance();
        mReserveProgress = new ProgressDialog(this);
        mReserveProgress.setCancelable(false);

        Log.e(TAG,mItemStatus);
        getReservedDetails();
    }

    public void getReservedDetails(){
        setTitle(mItemName);
        Picasso.with(ReservedDetailActivity.this)
                .load(mItemLink)
                .into(mThumbnail);
        mTxtTitle.setText(mItemName);
        mTxtDetails.setText(mItemDetail);
        if(mItemStarsRequired == 0) {
            mTxtPrice.setText("Price: PHP " + Utils.formatFloat(mItemPrice));
        } else {
            mTxtPrice.setText("Stars Required: " + mItemStarsRequired);
        }

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

        Intent intent = new Intent(this, ItemAvailableService.class);
        intent.putExtra("requestId",mRequestId);
        intent.putExtra("itemId", mItemId);
        startService(intent);
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

        Intent intent = new Intent(this, ItemClaimedService.class);
        intent.putExtra("requestId",mRequestId);
        intent.putExtra("itemId", mItemId);
        startService(intent);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_reserved_items;
    }

    public void closeActivity(){
        ReservedDetailActivity.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ItemAvailableService.ACTION);
        filter.addAction(ItemClaimedService.ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private class ReservedDetailBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, intent.getStringExtra("response"));
            //mProgressBar.setVisibility(View.GONE);
            String response = intent.getStringExtra("response");

            if(intent.getIntExtra("result",0) == -1){
                Log.e(TAG, response);
                //Toast.makeText(QueueItemDetailActivity.this, response , Toast.LENGTH_SHORT).show();
                Snackbar.make(mTxtDetails, response, Snackbar.LENGTH_SHORT).show();
            }else{
                if("item_available".equals(response)){
                    Log.e(TAG, "item_available");
                    Snackbar.make(mTxtDetails, "Item successfully approved", Snackbar.LENGTH_SHORT).show();
                    closeActivity();
                }else if("item_claimed".equals(response)){
                    Log.e(TAG, "item_claimed");
                    Snackbar.make(mTxtDetails, "Item successfully denied", Snackbar.LENGTH_SHORT).show();
                    closeActivity();
                }
            }
        }

    }

}
