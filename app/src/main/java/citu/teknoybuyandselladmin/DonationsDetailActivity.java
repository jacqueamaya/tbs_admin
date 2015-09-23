package citu.teknoybuyandselladmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselladmin.models.DonateApproval;
import citu.teknoybuyandselladmin.models.SellApproval;


public class DonationsDetailActivity extends ActionBarActivity {
    private static final String REQUEST_ID = "request_id";
    private static final String TAG = "DonationsDetailActivity";
    private static final String ITEM_ID = "item_id";
    private static final String STARS_REQUIRED = "stars_required";
    private static final String CATEGORY = "category";

    private int requestId;
    private int itemId;

    private TextView txtTitle;
    private TextView txtPrice;
    private TextView txtDetails;
    private TextView txtStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_detail);

        Intent intent = getIntent();

        requestId = intent.getIntExtra("requestId",0);
        itemId = intent.getIntExtra("itemId",0);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtPrice = (TextView) findViewById(R.id.txtPriceLabel);
        txtDetails = (TextView) findViewById(R.id.txtDetails);
        txtStars = (TextView) findViewById(R.id.txtNumOfStars);
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
                    txtTitle.setText(donate.getItemName());
                    txtPrice.setText("Price: PHP " + donate.getPrice());
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

    public void onApprove(View view){
        Log.v(TAG,"Item ID: "+itemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID,this.itemId+"");
        data.put(REQUEST_ID,this.requestId+"");
        data.put(STARS_REQUIRED,txtStars.getText().toString());

        //static category**** to be modified
        data.put(CATEGORY,"Static category");
        //Log.v(TAG,category.getSelectedItem().toString());

        Server.approveDonatedItem(data, new Ajax.Callbacks() {
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

        Server.denyDonatedItem(data, new Ajax.Callbacks() {
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
}
