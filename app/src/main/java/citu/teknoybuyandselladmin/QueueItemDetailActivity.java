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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.SellApproval;
import citu.teknoybuyandselladmin.services.AddCategoryService;
import citu.teknoybuyandselladmin.services.ApproveItemService;
import citu.teknoybuyandselladmin.services.DenyItemService;
import citu.teknoybuyandselladmin.services.GetCategoriesService;
import io.realm.Realm;
import io.realm.RealmResults;


public class QueueItemDetailActivity extends AppCompatActivity {

    private static final String TAG = "QueueItemDetailActivity";
    private static final String REQUEST_ID = "request_id";
    private static final String ITEM_ID = "item_id";
    public static final String CATEGORY_ITEM = "category";

    private int mRequestId;
    private String mItemPurpose = "";
    private String mCategoryList[];

    private TextView mTxtTitle;
    private TextView mTxtPrice;
    private TextView mTxtQuantity;
    private TextView mTxtOwner;
    private TextView mTxtDatePosted;
    private TextView mTxtDetails;
    private TextView mTxtCategory;
    private TextView mTxtPurpose;
    private TextView mTxtRentDuration;

    private SimpleDraweeView mItem;

    private ImageView mAddCategory;
    private ImageView mRentImg;

    private Spinner mCategories;

    private ProgressBar mProgressBar;
    private ArrayAdapter categoryAdapter;

    private QueuedDetailBroadcastReceiver mReceiver;
    private Realm realm;

    private SellApproval sellApproval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_on_queue_detail);
        setupToolbar();

        realm = Realm.getDefaultInstance();
        mReceiver = new QueuedDetailBroadcastReceiver();

        Intent intent = getIntent();
        mRequestId = intent.getIntExtra("requestId", 0);
        sellApproval = realm.where(SellApproval.class).equalTo("id",mRequestId).findFirst();

        if("Rent".equals(sellApproval.getItem().getPurpose())){
            mItemPurpose = " For Rent";
        } else if("Sell".equals(sellApproval.getItem().getPurpose())){
            mItemPurpose = " For Sale";
        }


        mItem = (SimpleDraweeView) findViewById(R.id.imgItem);
        mTxtTitle = (TextView) findViewById(R.id.txtItem);
        mTxtPrice = (TextView) findViewById(R.id.txtPrice);
        mTxtQuantity = (TextView) findViewById(R.id.txtQuantity);
        mTxtPurpose = (TextView) findViewById(R.id.txtPurpose);
        mTxtRentDuration = (TextView) findViewById(R.id.txtRentDuration);
        mAddCategory = (ImageView) findViewById(R.id.addCategory);
        mRentImg = (ImageView) findViewById(R.id.rentImg);
        mTxtDetails = (TextView) findViewById(R.id.txtDescription);
        mTxtOwner = (TextView) findViewById(R.id.txtOwner);
        mTxtDatePosted = (TextView) findViewById(R.id.txtDatePosted);
        mCategories = (Spinner) findViewById(R.id.spinnerCategories);


        //mQueueProgress = new ProgressDialog(this);
        //mProgressBar = (ProgressBar) findViewById(R.id.progressGetCategory);
        //mQueueProgress.setCancelable(false);

        getCategories();
        updateCategoryList();
        getQueueItemDetails();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void getQueueItemDetails() {
        setTitle(sellApproval.getItem().getName());
        mItem.setImageURI(Uri.parse(sellApproval.getItem().getPicture()));
        mTxtTitle.setText(sellApproval.getItem().getName());
        mTxtPrice.setText("Php " + Utils.formatFloat(sellApproval.getItem().getPrice()));
        mTxtQuantity.setText(" "+sellApproval.getItem().getQuantity());
        mTxtPurpose.setText(mItemPurpose);
        if("Rent".equals(sellApproval.getItem().getPurpose())){
            mRentImg.setVisibility(View.VISIBLE);
            mTxtRentDuration.setVisibility(View.VISIBLE);
            mTxtRentDuration.setText(sellApproval.getItem().getRent_duration()+" days");
        }
        mTxtDetails.setText(sellApproval.getItem().getDescription());
        mTxtOwner.setText(Utils.capitalize(sellApproval.getItem().getOwner().getUser().getUsername()));
        mTxtDatePosted.setText(Utils.parseDate(sellApproval.getRequest_date()));

        if(mCategoryList.length != 0)
            setCategoryAdapter(mCategories, mCategoryList);
    }

    public void setCategoryAdapter(Spinner spinner, String[] data){
        categoryAdapter  = new ArrayAdapter(this, R.layout.spinner_item, data);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);
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
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if ("".equals(category.getText().toString().trim()) || category.getText().toString() == null) {
                                    Utils.alertInfo(QueueItemDetailActivity.this, "Please input the category to add.");
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
        Utils.alert(QueueItemDetailActivity.this, "Approve Item", "Are you sure you want to approve this item?", new Utils.Callbacks() {
            @Override
            public void ok() {
                if ("".equals(mCategories.getSelectedItem().toString()) || mCategories.getSelectedItem().toString() == null) {
                    Utils.alertInfo(QueueItemDetailActivity.this, "Please select a category first");
                } else {
                    approveItem();
                }

            }
        });
    }

    public void approveItem(){
        Log.v(TAG, "Item REQUEST_ID: " + sellApproval.getItem().getId());
        Intent intent = new Intent(this, ApproveItemService.class);
        intent.putExtra("requestId", mRequestId);
        intent.putExtra("itemId", sellApproval.getItem().getId());
        intent.putExtra("category", mCategories.getSelectedItem().toString());
        startService(intent);
        //mProgressBar.setVisibility(View.VISIBLE);
    }

    public void onDeny(View view) {
        Utils.alert(QueueItemDetailActivity.this, "Deny Item", "Are you sure you want to deny this item?", new Utils.Callbacks() {
            @Override
            public void ok() {
                denyItem();
            }
        });
    }

    public void denyItem(){
        Log.v(TAG, "Item REQUEST_ID: " + sellApproval.getItem().getId());
        Intent intent  = new Intent(this, DenyItemService.class);
        intent.putExtra("requestId", mRequestId);
        intent.putExtra("itemId", sellApproval.getItem().getId());
        startService(intent);
        //mProgressBar.setVisibility(View.VISIBLE);
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

    public void closeActivity(){
        QueueItemDetailActivity.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(GetCategoriesService.ACTION);
        filter.addAction(ApproveItemService.ACTION);
        filter.addAction(DenyItemService.ACTION);
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

    private class QueuedDetailBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, intent.getStringExtra("response"));
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
                }else if("approved_item".equals(response)){
                    Log.e(TAG, "approved item");
                    //mProgressBar.setVisibility(View.GONE);
                    Snackbar.make(mTxtDetails, "Item successfully approved", Snackbar.LENGTH_SHORT).show();
                    closeActivity();
                }else if("disapproved_item".equals(response)){
                    Log.e(TAG, "denied item");
                    //mProgressBar.setVisibility(View.GONE);
                    Snackbar.make(mTxtDetails, "Item successfully denied", Snackbar.LENGTH_SHORT).show();
                    closeActivity();
                }
            }
        }

    }
}
