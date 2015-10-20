package citu.teknoybuyandselladmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselladmin.adapters.SellApprovalAdapter;
import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.SellApproval;


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

    private TextView mTxtTitle;
    private TextView mTxtPrice;
    private TextView mTxtDetails;
    private TextView mTxtCategory;

    private ImageView mThumbnail;

    private ProgressDialog mQueueProgress;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_on_queue_detail);
        setupUI();

        Intent intent = getIntent();
        mRequestId = intent.getIntExtra("requestId",0);
        mItemId = intent.getIntExtra("itemId",0);
        mItemName = intent.getStringExtra("itemName");
        mItemDetail = intent.getStringExtra("itemDetail");
        mItemLink = intent.getStringExtra("itemLink");
        mItemCategory = intent.getStringExtra("itemCategory");
        mItemPrice = intent.getFloatExtra("itemPrice", 0);

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mTxtPrice = (TextView) findViewById(R.id.txtPrice);
        mTxtDetails = (TextView) findViewById(R.id.txtDetails);
        mTxtCategory = (TextView) findViewById(R.id.txtCategory);
        mThumbnail = (ImageView) findViewById(R.id.imgThumbnail);

        mQueueProgress = new ProgressDialog(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetCategory);
        mQueueProgress.setCancelable(false);

        getQueueItemDetails();
    }
    public void getQueueItemDetails(){
        setTitle(mItemName);
        Picasso.with(QueueItemDetailActivity.this)
                .load(mItemLink)
                .into(mThumbnail);

        mTxtTitle.setText(mItemName);
        mTxtPrice.setText("Price: PHP " + mItemPrice);
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
        Log.v(TAG,category.getText().toString());
        Map<String,String> data = new HashMap<>();
        data.put(CATEGORY_ITEM, category.getText().toString());

        mQueueProgress.setIndeterminate(true);
        mQueueProgress.setMessage("Please wait. . .");

        Server.addCategory(data, mQueueProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Category Added Successfully");
                        Snackbar.make(findViewById(R.id.appbar), "Category successfully added", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Log.v(TAG, "Failed to add activity_category");
                        Snackbar.make(findViewById(R.id.appbar), "Failed to add category", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Snackbar.make(findViewById(R.id.appbar), "Connection Error: Failed to add category", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void onApprove(View view){
        Utils.alert(QueueItemDetailActivity.this, "Approve Item", "Are you sure you want to approve this item?", new Utils.Callbacks() {
            @Override
            public void ok() {
                if ("".equals(mTxtCategory.getText().toString()) || mTxtCategory.getText().toString() == null) {
                    Utils.alertInfo(QueueItemDetailActivity.this, "Please select a category first");
                }
                else{
                    approveItem();
                }

            }
        });
    }

    public void approveItem(){
        Log.v(TAG, "Item REQUEST_ID: " + mItemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID, mItemId +"");
        data.put(REQUEST_ID, mRequestId + "");
        data.put(CATEGORY_ITEM, mTxtCategory.getText().toString());

        Log.v(TAG, "Item REQUEST_ID: " + mItemId);
        Log.v(TAG, "Request REQUEST_ID: " + mRequestId);

        mQueueProgress.setIndeterminate(true);
        mQueueProgress.setMessage("Please wait. . .");

        Server.approveQueuedItem(data, mQueueProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Successful Approval");
                        Toast.makeText(QueueItemDetailActivity.this, "Item successfully approved", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.v(TAG, "approval failed");
                        Toast.makeText(QueueItemDetailActivity.this, "Error; Item approval failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(QueueItemDetailActivity.this, " Connection Error: Item approval failed", Toast.LENGTH_SHORT).show();
            }
        });
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
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID, mItemId + "");
        data.put(REQUEST_ID, mRequestId + "");

        mQueueProgress.setIndeterminate(true);
        mQueueProgress.setMessage("Please wait. . .");

        Server.denyQueuedItem(data, mQueueProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Successful Disapproval");
                        Toast.makeText(QueueItemDetailActivity.this, "Item successfully disapproved", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.v(TAG, "Disapproval failed");
                        Toast.makeText(QueueItemDetailActivity.this, "Error; Item disapproval failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(QueueItemDetailActivity.this, "Error: Item disapproval failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items_on_queue_detail, menu);
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
        return menuItem.getItemId() != R.id.nav_items_queue;
    }

    public void onSelect(View view) {
        Server.getCategories(mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    final String categories[] = Category.asArray(new JSONArray(responseBody));
                    new AlertDialog.Builder(QueueItemDetailActivity.this)
                            .setTitle("Categories")
                            .setItems(categories, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mTxtCategory.setText(categories[which]);
                                }
                            })
                            .create()
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.e(TAG, "Error: Cannot connect to server");
            }
        });
    }
}
