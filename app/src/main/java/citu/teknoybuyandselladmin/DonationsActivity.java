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
                JSONArray jsonArray = null;

                ListView lv = (ListView) findViewById(R.id.listViewDonations);
                TextView txtMessage = (TextView) findViewById(R.id.txtMessage);

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        txtMessage.setText("No donate requests available");
                        txtMessage.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                    } else {
                        request = DonateApproval.asList(jsonArray);
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
        getMenuInflater().inflate(R.menu.menu_donations, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        getReservedItems();
    }
}
