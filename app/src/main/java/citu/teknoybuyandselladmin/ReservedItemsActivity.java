package citu.teknoybuyandselladmin;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.ListAdapters.ReservedItemListAdapter;
import citu.teknoybuyandselladmin.models.ReservedItem;


public class ReservedItemsActivity extends ActionBarActivity {

    private static final String TAG = "ReservedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_items);

        /*List<String> soldItems = new ArrayList<String>();
        soldItems.add("PE T-shirt");
        soldItems.add("Rizal Book");
        soldItems.add("Uniform");

        ListView lv = (ListView)findViewById(R.id.listViewReserved);
        CustomListAdapterQueue listAdapter = new CustomListAdapterQueue(ReservedItemsActivity.this, R.layout.activity_item, soldItems);
        lv.setAdapter(listAdapter);*/
        getReservedItems();
    }

    public void getReservedItems(){
        Server.getReservedItems(new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<ReservedItem> reserved = new ArrayList<ReservedItem>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    reserved = ReservedItem.allReservedItems(jsonArray);

                    ListView lv = (ListView)findViewById(R.id.listViewReserved);
                    ReservedItemListAdapter listAdapter = new ReservedItemListAdapter(ReservedItemsActivity.this, R.layout.activity_item , reserved);
                    lv.setAdapter(listAdapter);

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
        getMenuInflater().inflate(R.menu.menu_reserved_items, menu);
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
