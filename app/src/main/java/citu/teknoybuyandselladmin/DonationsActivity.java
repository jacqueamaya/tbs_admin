package citu.teknoybuyandselladmin;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import citu.teknoybuyandselladmin.ListAdapters.DonateApprovalAdapter;
import citu.teknoybuyandselladmin.ListAdapters.SellApprovalAdapter;
import citu.teknoybuyandselladmin.models.DonateApproval;
import citu.teknoybuyandselladmin.models.SellApproval;


public class DonationsActivity extends ActionBarActivity {

    private static final String TAG = "DonatedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);

        /*List<String> soldItems = new ArrayList<String>();
        soldItems.add("PE T-shirt");
        soldItems.add("Rizal Book");
        soldItems.add("Uniform");

        ListView lv = (ListView)findViewById(R.id.listViewDonations);
        CustomListAdapterQueue listAdapter = new CustomListAdapterQueue(DonationsActivity.this, R.layout.activity_item, soldItems);
        lv.setAdapter(listAdapter);*/
        getReservedItems();
    }

    public void getReservedItems(){
        Server.getDonateRequests(new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<DonateApproval> request = new ArrayList<DonateApproval>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    request = DonateApproval.allDonateRequest(jsonArray);

                    ListView lv = (ListView) findViewById(R.id.listViewDonations);
                    DonateApprovalAdapter listAdapter = new DonateApprovalAdapter(DonationsActivity.this, R.layout.activity_item, request);
                    lv.setAdapter(listAdapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DonateApproval donate = (DonateApproval) parent.getItemAtPosition(position);
                            int itemId = donate.getItemId();
                            int requestId = donate.getRequestId();
                            Log.v(TAG,"Item id: "+itemId);
                            Log.v(TAG,"Request id: "+ requestId);

                            Intent intent;
                            intent = new Intent(DonationsActivity.this, DonationsDetailActivity.class);
                            intent.putExtra("itemId",itemId);
                            intent.putExtra("requestId",requestId);
                            startActivity(intent);

                        }
                    });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donations, menu);
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
