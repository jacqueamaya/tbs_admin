package citu.teknoybuyandselladmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.adapters.DonateApprovalAdapter;
import citu.teknoybuyandselladmin.models.DonateApproval;


public class DonationsActivity extends BaseActivity {

    private static final String TAG = "DonatedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);
        setupUI();

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
                    if (jsonArray.length() == 0) {
                        TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
                        txtMessage.setText("No donate requests available");
                        txtMessage.setVisibility(View.VISIBLE);
                    } else {
                        request = DonateApproval.asList(jsonArray);

                        ListView lv = (ListView) findViewById(R.id.listViewDonations);
                        DonateApprovalAdapter listAdapter = new DonateApprovalAdapter(DonationsActivity.this, R.layout.activity_item, request);
                        lv.setAdapter(listAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                DonateApproval donate = (DonateApproval) parent.getItemAtPosition(position);
                                int itemId = donate.getItemId();
                                int requestId = donate.getRequestId();

                                Intent intent;
                                intent = new Intent(DonationsActivity.this, DonationsDetailActivity.class);
                                intent.putExtra("itemId", itemId);
                                intent.putExtra("requestId", requestId);
                                startActivity(intent);

                            }
                        });
                    }
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

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_donations;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReservedItems();
    }
}
