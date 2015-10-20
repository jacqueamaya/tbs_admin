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

import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselladmin.models.Category;


public class DonationsDetailActivity extends BaseActivity {
    private static final String REQUEST_ID = "request_id";
    private static final String TAG = "DonationsDetailActivity";
    private static final String ITEM_ID = "item_id";
    private static final String STARS_REQUIRED = "stars_required";
    private static final String CATEGORY = "activity_category";
    public static final String CATEGORY_ITEM = "category";

    private int mRequestId;
    private int mItemId;

    private String mItemName;
    private String mItemDetail;
    private String mItemCategory;
    private String mItemLink;

    private TextView mTxtTitle;
    private TextView mTxtDetails;
    private TextView mTxtStars;
    private TextView mTxtCategory;

    private ImageView thumbnail;

    private ProgressDialog donationProgress;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_detail);
        setupUI();

        Intent intent = getIntent();

        mRequestId = intent.getIntExtra("requestId",0);
        mItemId = intent.getIntExtra("itemId",0);
        mItemName = intent.getStringExtra("itemName");
        mItemDetail = intent.getStringExtra("itemDetail");
        mItemCategory = intent.getStringExtra("itemCategory");
        mItemLink = intent.getStringExtra("itemLink");

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mTxtDetails = (TextView) findViewById(R.id.txtDetails);
        mTxtStars = (TextView) findViewById(R.id.txtNumOfStars);
        mTxtCategory = (TextView) findViewById(R.id.txtCategory);
        thumbnail = (ImageView) findViewById(R.id.imgThumbnail);

        donationProgress = new ProgressDialog(this);
        donationProgress.setCancelable(false);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetCategoryDonate);

        getDonatedItemDetails();

    }

    public void getDonatedItemDetails(){
        setTitle(mItemName);
        Picasso.with(DonationsDetailActivity.this)
                .load(mItemLink)
                .into(thumbnail);
        mTxtTitle.setText(mItemName);
        mTxtDetails.setText(mItemDetail);
        //mTxtCategory.setText(mItemCategory);

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
                                Log.v(TAG,category.getText().toString());
                                Map<String,String> data = new HashMap<>();
                                data.put(CATEGORY_ITEM, category.getText().toString());

                                donationProgress.setIndeterminate(true);
                                donationProgress.setMessage("Please wait. . .");

                                Server.addCategory(data, donationProgress, new Ajax.Callbacks() {
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

    public void onApprove(View view){
        Utils.alert(DonationsDetailActivity.this, "Approve Item", "Are you sure you want to approve this item?", new Utils.Callbacks() {
            @Override
            public void ok() {
                approveDonation();
            }
        });
    }

    public void approveDonation(){
        Log.v(TAG, "Item REQUEST_ID: " + mItemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID,mItemId+"");
        data.put(REQUEST_ID, mRequestId + "");
        data.put(STARS_REQUIRED, mTxtStars.getText().toString());
        data.put(CATEGORY, mTxtCategory.getText().toString());

        donationProgress.setIndeterminate(true);
        donationProgress.setMessage("Please wait. . ");

        Server.approveDonatedItem(data, donationProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Successful Donation Approval");
                        Toast.makeText(DonationsDetailActivity.this, "Donation successfully approved", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.v(TAG, "approval failed");
                        Toast.makeText(DonationsDetailActivity.this, "Error: Donation approval failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(DonationsDetailActivity.this, "Connection Error: Donation approval failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDeny(View view){
        Utils.alert(DonationsDetailActivity.this, "Deny Item", "Are you sure you want to deny this item?", new Utils.Callbacks() {
            @Override
            public void ok() {
                denyDonation();
            }
        });
    }

    public void denyDonation(){
        Log.v(TAG, "Item REQUEST_ID: " + mItemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID,mItemId+"");
        data.put(REQUEST_ID,mRequestId+"");

        donationProgress.setIndeterminate(true);
        donationProgress.setMessage("Please wait. . ");

        Server.denyDonatedItem(data, donationProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Successful Disapproval");
                        Toast.makeText(DonationsDetailActivity.this, "Item successfully disapproved", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Log.v(TAG, "Disapproval failed");
                        Toast.makeText(DonationsDetailActivity.this, "Error; Item disapproval failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(DonationsDetailActivity.this, "Connection Error: Item disapproval failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_donations_detail, menu);
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
        return menuItem.getItemId() != R.id.nav_donations;
    }
    public void onSelect(View view) {
        Server.getCategories(mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    final String categories[] = Category.asArray(new JSONArray(responseBody));
                    new AlertDialog.Builder(DonationsDetailActivity.this)
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
