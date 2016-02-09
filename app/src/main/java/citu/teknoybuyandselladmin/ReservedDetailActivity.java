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

import citu.teknoybuyandselladmin.models.Reservation;
import citu.teknoybuyandselladmin.services.ItemAvailableService;
import citu.teknoybuyandselladmin.services.ItemClaimedService;
import io.realm.Realm;


public class ReservedDetailActivity extends AppCompatActivity {

    private static final String REQUEST_ID = "request_id";
    private static final String TAG = "ReservedDetailActivity";
    private static final String ITEM_ID = "item_id";

    private int mRequestId;

    private TextView mTxtTitle;
    private TextView mTxtOwner;
    private TextView mTxtReceiver;
    private TextView mTxtStarsRequired;
    private TextView mTxtItemCode;
    private TextView mTxtQuantity;
    private TextView mTxtReservedDate;
    private TextView mTxtPayment;
    private TextView mTxtDetails;
    private TextView mLblReceiver;

    private SimpleDraweeView mItem;

    private Button mBtnAvailable;
    private Button mBtnClaimed;

    //private ImageView mPaymentIcon;
    //private ImageView mStarsIcon;

    private ProgressDialog mReserveProgress;

    private ReservedDetailBroadcastReceiver mReceiver;
    private Realm realm;

    private Reservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_detail);
        setupToolbar();

        mReceiver = new ReservedDetailBroadcastReceiver();
        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        mRequestId = intent.getIntExtra("requestId", 0);

        reservation = realm.where(Reservation.class).equalTo("id",mRequestId).findFirst();

        mTxtTitle = (TextView) findViewById(R.id.txtItem);
        mTxtPayment = (TextView) findViewById(R.id.txtPrice);
        mTxtStarsRequired = (TextView) findViewById(R.id.txtStarsRequired);
        mTxtDetails = (TextView) findViewById(R.id.txtDescription);
        mTxtItemCode = (TextView) findViewById(R.id.txtItemCode);
        mTxtQuantity = (TextView) findViewById(R.id.txtQuantity);
        mTxtOwner = (TextView) findViewById(R.id.txtOwner);
        mTxtReceiver = (TextView) findViewById(R.id.txtReceiver);
        mTxtReservedDate = (TextView) findViewById(R.id.txtReservedDate);
        mLblReceiver = (TextView) findViewById(R.id.lblReceiver);
        //mPaymentIcon = (ImageView) findViewById(R.id.iconPayment);
        //mStarsIcon = (ImageView) findViewById(R.id.iconStars);

        mItem = (SimpleDraweeView) findViewById(R.id.imgItem);

        mBtnAvailable = (Button) findViewById(R.id.btnAvailable);
        mBtnClaimed = (Button) findViewById(R.id.btnClaimed);


        mReserveProgress = new ProgressDialog(this);
        mReserveProgress.setCancelable(false);

        getReservedDetails();
    }

    public void getReservedDetails(){
        setTitle(reservation.getItem().getName());
        mItem.setImageURI(Uri.parse(reservation.getItem().getPicture()));
        mTxtTitle.setText(reservation.getItem().getName());
        mTxtItemCode.setText(" " + reservation.getItem_code());
        mTxtQuantity.setText(" "+reservation.getQuantity());
        mTxtReceiver.setText(Utils.capitalize(reservation.getBuyer().getUsername()));
        mTxtOwner.setText(Utils.capitalize(reservation.getItem().getOwner().getUser().getUsername()));
        mTxtReservedDate.setText(Utils.parseDate(reservation.getReserved_date()));
        mTxtDetails.setText(reservation.getItem().getDescription());

        if(("Sell".equals(reservation.getItem().getPurpose())) || ("Rent".equals(reservation.getItem().getPurpose()))) {
            if("Sell".equals(reservation.getItem().getPurpose())){
                mLblReceiver.setText("Buyer: ");
            }else{
                mLblReceiver.setText("Renter: ");
            }
            mTxtPayment.setText(" Php " + Utils.formatFloat(reservation.getItem().getPrice()));
        } else if ("Donate".equals(reservation.getItem().getPurpose())) {
            mLblReceiver.setText("Donee: ");
            //mPaymentIcon.setVisibility(View.GONE);
            mTxtPayment.setVisibility(View.GONE);
            //mStarsIcon.setVisibility(View.VISIBLE);
            mTxtStarsRequired.setVisibility(View.VISIBLE);
            mTxtStarsRequired.setText(" " + reservation.getItem().getStars_required());
        }

        if("Reserved".equals(reservation.getStatus())){
            Log.v(TAG,reservation.getStatus()+" Item reserved. Claimed button disabled");
            mBtnClaimed.setEnabled(false);
        }
        else if("Available".equals(reservation.getStatus())){
            Log.v(TAG,reservation.getStatus()+" Item available. Available button disabled");
            mBtnAvailable.setEnabled(false);
        }

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        Log.v(TAG, "Item REQUEST_ID: " + reservation.getItem().getId());

        Intent intent = new Intent(this, ItemAvailableService.class);
        intent.putExtra("requestId",mRequestId);
        intent.putExtra("itemId", reservation.getItem().getId());
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
        Log.v(TAG, "Item ITEM_ID: " + reservation.getItem().getId());
        Log.v(TAG, "Item REQUEST_ID: " + mRequestId);

        Intent intent = new Intent(this, ItemClaimedService.class);
        intent.putExtra("requestId",mRequestId);
        intent.putExtra("itemId", reservation.getItem().getId());
        startService(intent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
