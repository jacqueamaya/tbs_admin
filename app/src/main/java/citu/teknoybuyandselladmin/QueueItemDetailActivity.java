package citu.teknoybuyandselladmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.services.AddCategoryService;
import citu.teknoybuyandselladmin.services.ApproveItemService;
import citu.teknoybuyandselladmin.services.DenyItemService;
import citu.teknoybuyandselladmin.services.GetCategoriesService;
import io.realm.Realm;
import io.realm.RealmResults;


public class QueueItemDetailActivity extends BaseActivity {

    private static final String TAG = "QueueItemDetailActivity";
    private static final String REQUEST_ID = "request_id";
    private static final String ITEM_ID = "item_id";
    public static final String CATEGORY_ITEM = "category";

    private int mRequestId;
    private int mItemId;

    private float mItemPrice;

    private String mItemName;
    private String mItemDetail;
    private String mItemLink;
    private String mItemCategory;
    private String mItemPurpose;
    private String mCategories[];

    private TextView mTxtTitle;
    private TextView mTxtPrice;
    private TextView mTxtDetails;
    private TextView mTxtCategory;
    private TextView mTxtPurpose;

    private ImageView mThumbnail;

    private ProgressDialog mQueueProgress;
    private ProgressBar mProgressBar;

    private QueuedDetailBroadcastReceiver mReceiver;
    private Realm realm;

    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_on_queue_detail);
        setupUI();

        Intent intent = getIntent();
        mRequestId = intent.getIntExtra("requestId",0);
        mItemId = intent.getIntExtra("itemId", 0);
        mItemName = intent.getStringExtra("itemName");
        mItemDetail = intent.getStringExtra("itemDetail");
        mItemLink = intent.getStringExtra("itemLink");
        mItemPrice = intent.getFloatExtra("itemPrice", 0);
        mItemPurpose = intent.getStringExtra("itemPurpose");

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mTxtPrice = (TextView) findViewById(R.id.txtPrice);
        mTxtDetails = (TextView) findViewById(R.id.txtDetails);
        mTxtCategory = (TextView) findViewById(R.id.txtCategory);
        mTxtPurpose = (TextView) findViewById(R.id.txtPurpose);
        mThumbnail = (ImageView) findViewById(R.id.imgThumbnail);

        realm = Realm.getDefaultInstance();
        mReceiver = new QueuedDetailBroadcastReceiver();
        mQueueProgress = new ProgressDialog(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetCategory);
        mQueueProgress.setCancelable(false);

        getQueueItemDetails();
        getCategories();

    }
    public void getQueueItemDetails(){
        setTitle(mItemName);
        Picasso.with(QueueItemDetailActivity.this)
                .load(mItemLink)
                .into(mThumbnail);

        mTxtTitle.setText(mItemName);
        mTxtPrice.setText("Price: PHP " + Utils.formatFloat(mItemPrice));
        mTxtPurpose.setText("Purpose: " + mItemPurpose);
        mTxtDetails.setText(mItemDetail);
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
                if ("".equals(mTxtCategory.getText().toString()) || mTxtCategory.getText().toString() == null) {
                    Utils.alertInfo(QueueItemDetailActivity.this, "Please select a category first");
                } else {
                    approveItem();
                }

            }
        });
    }

    public void approveItem(){
        Log.v(TAG, "Item REQUEST_ID: " + mItemId);
        Intent intent = new Intent(this, ApproveItemService.class);
        intent.putExtra("requestId", mRequestId);
        intent.putExtra("itemId", mItemId);
        intent.putExtra("category", mTxtCategory.getText().toString());
        startService(intent);
        mProgressBar.setVisibility(View.VISIBLE);
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
        Log.v(TAG, "Item REQUEST_ID: " + mItemId);
        Intent intent  = new Intent(this, DenyItemService.class);
        intent.putExtra("requestId", mRequestId);
        intent.putExtra("itemId", mItemId);
        startService(intent);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_items_queue;
    }

    public void getCategories(){
        Intent intent = new Intent(this, GetCategoriesService.class);
        startService(intent);
    }

    public void updateCategoryList(){
        RealmResults<Category> categories = realm.where(Category.class).findAll();
        mCategories = new String[categories.size()];

        for(int i=0; i<categories.size(); i++){
            mCategories[i] =  categories.get(i).getCategory_name();
        }
    }

    public void onSelect(View view) {
        getCategories();
        RealmResults<Category> categories = realm.where(Category.class).findAll();

        if(categories.size() == 0){
            mProgressBar.setVisibility(View.VISIBLE);
        } else{
            updateCategoryList();
            new AlertDialog.Builder(QueueItemDetailActivity.this)
                    .setTitle("Categories")
                    .setItems(mCategories, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTxtCategory.setText(mCategories[which]);
                        }
                    })
                    .create()
                    .show();
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

    private class QueuedDetailBroadcastReceiver extends BroadcastReceiver {

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
                }else if("approved_item".equals(response)){
                    Log.e(TAG, "approved item");
                    mProgressBar.setVisibility(View.GONE);
                    Snackbar.make(mTxtDetails, "Item successfully approved", Snackbar.LENGTH_SHORT).show();
                    closeActivity();
                }else if("disapproved_item".equals(response)){
                    Log.e(TAG, "denied item");
                    mProgressBar.setVisibility(View.GONE);
                    Snackbar.make(mTxtDetails, "Item successfully denied", Snackbar.LENGTH_SHORT).show();
                    closeActivity();
                }
            }
        }

    }
}
