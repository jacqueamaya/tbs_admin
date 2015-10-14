package citu.teknoybuyandselladmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.DonateApproval;
import citu.teknoybuyandselladmin.models.SellApproval;


public class DonationsDetailActivity extends BaseActivity {
    private static final String REQUEST_ID = "request_id";
    private static final String TAG = "DonationsDetailActivity";
    private static final String ITEM_ID = "item_id";
    private static final String STARS_REQUIRED = "stars_required";
    private static final String CATEGORY = "activity_category";
    public static final String CATEGORY_ITEM = "category";

    private int requestId;
    private int itemId;
    private String mItemName;

    private TextView txtTitle;
    private TextView txtPrice;
    private TextView txtDetails;
    private TextView txtStars;
    private TextView txtCategory;

    private ImageView thumbnail;

    private ProgressDialog donationProgress;
    private ProgressDialog queueProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_detail);
        setupUI();

        Intent intent = getIntent();

        requestId = intent.getIntExtra("requestId",0);
        itemId = intent.getIntExtra("itemId",0);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDetails = (TextView) findViewById(R.id.txtDetails);
        txtStars = (TextView) findViewById(R.id.txtNumOfStars);
        thumbnail = (ImageView) findViewById(R.id.imgThumbnail);

        donationProgress = new ProgressDialog(this);

        getDonatedItemDetails(requestId);

    }

    public void getDonatedItemDetails(int request){
        Map<String,String> data = new HashMap<>();
        data.put(REQUEST_ID,request+"");

        Server.getDonatedItemDetails(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<DonateApproval> request = new ArrayList<DonateApproval>();
                DonateApproval donate;
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    request = DonateApproval.allDonateRequest(jsonArray);
                    donate = request.get(0);

                    Picasso.with(DonationsDetailActivity.this)
                            .load(donate.getLink())
                            .into(thumbnail);

                    mItemName = donate.getItemName();
                    setTitle(mItemName);

                    txtTitle.setText(mItemName);
                    txtDetails.setText(donate.getDetails());

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                // Toast.makeText(LoginActivity.this, "Error: Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
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

                                queueProgress.setIndeterminate(true);
                                queueProgress.setMessage("Please wait. . .");

                                Server.addCategory(data, queueProgress, new Ajax.Callbacks() {
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
        Log.v(TAG, "Item ID: " + itemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID,this.itemId+"");
        data.put(REQUEST_ID, this.requestId + "");
        data.put(STARS_REQUIRED, txtStars.getText().toString());

        //static activity_category**** to be modified
        data.put(CATEGORY, "Static activity_category");
        //Log.v(TAG,activity_category.getSelectedItem().toString());

        donationProgress.setIndeterminate(true);
        donationProgress.setMessage("Please wait. . ");

        Server.approveDonatedItem(data,donationProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if(json.getInt("status") == 200){
                        Log.v(TAG,"Successful Donation Approval");
                        Toast.makeText(DonationsDetailActivity.this, "Donation successfully approved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.v(TAG,"approval failed");
                        Toast.makeText(DonationsDetailActivity.this, "Error: Donation approval failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG,"Request error");
                Toast.makeText(DonationsDetailActivity.this, "Error: Donation approval failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDeny(View view){
        Log.v(TAG,"Item ID: "+itemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID,this.itemId+"");
        data.put(REQUEST_ID,this.requestId+"");

        donationProgress.setIndeterminate(true);
        donationProgress.setMessage("Please wait. . ");

        Server.denyDonatedItem(data, donationProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if(json.getInt("status") == 200){
                        Log.v(TAG,"Successful Disapproval");
                        Toast.makeText(DonationsDetailActivity.this, "Item successfully disapproved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.v(TAG,"Disapproval failed");
                        Toast.makeText(DonationsDetailActivity.this, "Error; Item disapproval failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG,"Request error");
                Toast.makeText(DonationsDetailActivity.this, "Error: Item disapproval failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donations_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        Server.getCategories(new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    final String categories[] = Category.getAllCategories(new JSONArray(responseBody));
                    new AlertDialog.Builder(DonationsDetailActivity.this)
                            .setTitle("Categories")
                            .setItems(categories, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    txtCategory.setText(categories[which]);
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
