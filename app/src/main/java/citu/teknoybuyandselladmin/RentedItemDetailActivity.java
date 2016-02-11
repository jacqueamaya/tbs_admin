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

import citu.teknoybuyandselladmin.models.RentedItem;
import citu.teknoybuyandselladmin.services.ItemReturnedService;
import io.realm.Realm;


public class RentedItemDetailActivity extends AppCompatActivity {

    public static final String RENT_ID = "rent_id";
    private static final String TAG = "RentedItemDetail";
    public static final String ITEM_ID = "item_id";

    private int mRentId;

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
    private Realm realm;

    private RentedItem rentedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_item_detail);
        setupToolbar();

        mReceiver = new RentedBroadcastReceiver();
        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        mRentId = intent.getIntExtra("rentId", 0);

        rentedItem = realm.where(RentedItem.class).equalTo("id",mRentId).findFirst();

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


        mRentProgress = new ProgressDialog(this);
        mRentProgress.setCancelable(false);

        getRentDetails();
    }

    public void getRentDetails() {
        setTitle(rentedItem.getItem().getName());
        mItem.setImageURI(Uri.parse(rentedItem.getItem().getPicture()));
        mTxtTitle.setText(rentedItem.getItem().getName());
        mTxtItemCode.setText(" "+rentedItem.getItem_code());
        mTxtQuantity.setText(" "+rentedItem.getQuantity());
        mTxtPenalty.setText("Php "+ Utils.formatFloat(rentedItem.getPenalty()));
        mTxtRentDuration.setText(rentedItem.getItem().getRent_duration()+" days");
        mTxtOwner.setText(Utils.capitalize(rentedItem.getItem().getOwner().getUser().getUsername()));
        mTxtRenter.setText(Utils.capitalize(rentedItem.getRenter().getUsername()));
        mTxtRentDate.setText(Utils.parseDate(rentedItem.getRent_date()));
        mTxtRentExpiry.setText(Utils.parseDate(rentedItem.getRent_expiration()));
        mTxtDetails.setText(rentedItem.getItem().getDescription());

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
        intent.putExtra("itemId", rentedItem.getItem().getId());
        startService(intent);
    }

    public void closeActivity(){
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
                    closeActivity();
                }
            }
        }
    }

}
