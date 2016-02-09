package citu.teknoybuyandselladmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.services.AddCategoryService;
import citu.teknoybuyandselladmin.services.ApproveDonationService;
import citu.teknoybuyandselladmin.services.DenyDonationService;
import citu.teknoybuyandselladmin.services.GetCategoriesService;
import io.realm.Realm;
import io.realm.RealmResults;

public class DonationsDetailActivity extends AppCompatActivity {
    private static final String REQUEST_ID = "request_id";
    private static final String TAG = "DonationsDetailActivity";
    private static final String ITEM_ID = "item_id";
    private static final String STARS_REQUIRED = "stars_required";
    public static final String CATEGORY_ITEM = "category";

    private int mRequestId;
    private int mItemId;
    private int mQuantity;

    private long mDatePosted;

    private String mItemOwner;
    private String mItemName;
    private String mItemDetail;
    private String mItemCategory;
    private String mItemLink;
    private String mCategoryList[];

    private SimpleDraweeView mItem;
    private ImageView mAddCategory;
    private ImageView mRentImg;

    private EditText mTxtStarsRequired;

    private TextView mTxtTitle;
    private TextView mTxtQuantity;
    private TextView mTxtOwner;
    private TextView mTxtDatePosted;
    private TextView mTxtDetails;

    private Button mApprove;
    private Button mDeny;

    private Spinner mCategories;
    private ArrayAdapter categoryAdapter;

    private ProgressDialog donationProgress;
    private ProgressBar mProgressBar;

    private Gson gson = new Gson();

    private DonationDetailBroadcastReceiver mReceiver;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_detail);
        setupToolbar();

        Intent intent = getIntent();

        mRequestId = intent.getIntExtra("requestId",0);
        mItemId = intent.getIntExtra("itemId", 0);
        mItemName = intent.getStringExtra("itemName");
        mItemDetail = intent.getStringExtra("itemDetail");
        mItemLink = intent.getStringExtra("itemLink");
        mItemOwner = intent.getStringExtra("itemOwner");
        mQuantity = intent.getIntExtra("itemQuantity", 0);
        mDatePosted = intent.getLongExtra("requestDate", 0);

        mTxtStarsRequired = (EditText) findViewById(R.id.txtStarsRequired);
        mTxtTitle = (TextView) findViewById(R.id.txtItem);
        mTxtDetails = (TextView) findViewById(R.id.txtDescription);
        mTxtOwner = (TextView) findViewById(R.id.txtOwner);
        mTxtDatePosted = (TextView) findViewById(R.id.txtDatePosted);
        mTxtQuantity = (TextView) findViewById(R.id.txtQuantity);

        mAddCategory = (ImageView) findViewById(R.id.addCategory);
        mItem = (SimpleDraweeView) findViewById(R.id.imgItem);

        mReceiver = new DonationDetailBroadcastReceiver();
        realm = Realm.getDefaultInstance();
        //donationProgress = new ProgressDialog(this);
        //donationProgress.setCancelable(false);
        //mProgressBar = (ProgressBar) findViewById(R.id.progressGetCategoryDonate);

        getCategories();
        updateCategoryList();
        getDonatedItemDetails();

    }

    public void getDonatedItemDetails(){
        setTitle(mItemName);
        /*Picasso.with(DonationsDetailActivity.this)
                .load(mItemLink)
                .into(thumbnail);*/
        mItem.setImageURI(Uri.parse(mItemLink));
        mTxtTitle.setText(mItemName);
        mTxtOwner.setText(" " + Utils.capitalize(mItemOwner));
        mTxtQuantity.setText(" " + mQuantity);
        mTxtDetails.setText(mItemDetail);
        mTxtDatePosted.setText(Utils.parseDate(mDatePosted));

        if(mCategoryList.length != 0)
            setCategoryAdapter(mCategories, mCategoryList);

    }

    public void setCategoryAdapter(Spinner spinner, String[] data){
        categoryAdapter  = new ArrayAdapter(this, R.layout.spinner_item, data);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void addCategory(View view){
        LayoutInflater li = LayoutInflater.from(this);
        View addCategPrompt = li.inflate(R.layout.activity_add_category, null);
        AlertDialog.Builder alertDialogBuilder =  new AlertDialog.Builder(this);
        alertDialogBuilder.setView(addCategPrompt);

        final EditText category = (EditText) addCategPrompt.findViewById(R.id.txtCategoryAdded);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id){
                                if ("".equals(category.getText().toString().trim()) || category.getText().toString() == null) {
                                    Utils.alertInfo(DonationsDetailActivity.this, "Please input the category to add.");
                                } else {
                                    saveCategory(category);
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog =  alertDialogBuilder.create();
        alertDialog.show();
    }

    public void saveCategory(EditText category){
        Log.v(TAG, category.getText().toString());
        Intent intent = new Intent(this, AddCategoryService.class);
        intent.putExtra("category", category.getText().toString());
        startService(intent);
    }

    public void onApprove(View view){
        Log.e(TAG, "Approve item?");
        Utils.alert(DonationsDetailActivity.this, "Approve Item", "Are you sure you want to approve this item?", new Utils.Callbacks() {
            @Override
            public void ok() {
                if ("".equals(mCategories.getSelectedItem().toString()) || mCategories.getSelectedItem().toString() == null || "".equals(mTxtStarsRequired.getText().toString().trim()) || mTxtStarsRequired.getText().toString().trim() == null) {
                    Utils.alertInfo(DonationsDetailActivity.this, "Please select a category first and input the  number of stars required.");
                } else {
                    approveDonation();
                    Log.e(TAG, "yes, approve item");
                }
            }
        });
    }

    public void approveDonation(){
        Log.v(TAG, "Item REQUEST_ID: " + mItemId);
        int starsRequired = Integer.parseInt(mTxtStarsRequired.getText().toString());
        Intent intent = new Intent(this, ApproveDonationService.class);
        intent.putExtra("requestId", mRequestId);
        intent.putExtra("itemId", mItemId);
        intent.putExtra("category", mCategories.getSelectedItem().toString());
        intent.putExtra("starsRequired", starsRequired);
        startService(intent);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void onDeny(View view){
        Log.e(TAG, "Approve item?");
        Utils.alert(DonationsDetailActivity.this, "Deny Item", "Are you sure you want to deny this item?", new Utils.Callbacks() {
            @Override
            public void ok() {
                denyDonation();
                Log.e(TAG, "yes, approve item");
            }
        });
    }

    public void denyDonation(){
        Log.v(TAG, "Item REQUEST_ID: " + mItemId);
        Intent intent  = new Intent(this, DenyDonationService.class);
        intent.putExtra("requestId", mRequestId);
        intent.putExtra("itemId", mItemId);
        startService(intent);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void getCategories(){
        Intent intent = new Intent(this, GetCategoriesService.class);
        startService(intent);
    }

    public void updateCategoryList(){
        RealmResults<Category> categories = realm.where(Category.class).findAll();
        mCategoryList = new String[categories.size()];

        for(int i=0; i<categories.size(); i++){
            mCategoryList[i] =  categories.get(i).getCategory_name();
        }
    }

    /*public void onSelect(View view) {
        getCategories();
        RealmResults<Category> categories = realm.where(Category.class).findAll();

        if(categories.size() == 0){
            mProgressBar.setVisibility(View.VISIBLE);
            Log.e(TAG, "no categories from realm");
        } else{
            updateCategoryList();
            new AlertDialog.Builder(DonationsDetailActivity.this)
                    .setTitle("Categories")
                    .setItems(mCategories, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTxtCategory.setText(mCategories[which]);
                        }
                    })
                    .create()
                    .show();
        }*/
        /*Server.getCategories(mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                    Category[] categories = gson.fromJson(responseBody, Category[].class);
                    String categoryNames[] = new String[categories.length];
                    for(int i=0; i<categories.length; i++){
                        categoryNames[i] =  categories[i].getCategory_name();
                    }
                    final String categoryNamesFinal[] = categoryNames;
                    new AlertDialog.Builder(DonationsDetailActivity.this)
                            .setTitle("Categories")
                            .setItems(categoryNames, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mTxtCategory.setText(categoryNamesFinal[which]);
                                }
                            })
                            .create()
                            .show();
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.e(TAG, "Error: Cannot connect to server");
            }
        });
    }*/

    public void closeActivity(){
        DonationsDetailActivity.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(GetCategoriesService.ACTION);
        filter.addAction(ApproveDonationService.ACTION);
        filter.addAction(DenyDonationService.ACTION);
        filter.addAction(AddCategoryService.ACTION);
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

    private class DonationDetailBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, intent.getStringExtra("response"));
            mProgressBar.setVisibility(View.GONE);
            String response = intent.getStringExtra("response");

            if(intent.getIntExtra("result",0) == -1){
                Log.e(TAG, response);
                //Toast.makeText(QueueItemDetailActivity.this, response , Toast.LENGTH_SHORT).show();
                Snackbar.make(mTxtDetails, response, Snackbar.LENGTH_SHORT).show();
            }else{
                if("get_categories".equals(response)){
                    Log.e(TAG, "fetched all categories");
                    updateCategoryList();
                }else if("add_category".equals(response)){
                    Log.e(TAG, "add category");
                    updateCategoryList();
                    Snackbar.make(mTxtDetails, "Category successfully added", Snackbar.LENGTH_SHORT).show();
                }else if("approved_donation".equals(response)){
                    Log.e(TAG, "approved item");
                    mProgressBar.setVisibility(View.GONE);
                    Snackbar.make(mTxtDetails, "Item successfully approved", Snackbar.LENGTH_SHORT).show();
                    closeActivity();
                }else if("disapproved_donation".equals(response)){
                    Log.e(TAG, "denied item");
                    mProgressBar.setVisibility(View.GONE);
                    Snackbar.make(mTxtDetails, "Item successfully denied", Snackbar.LENGTH_SHORT).show();
                    closeActivity();
                }
            }
        }

    }
}
