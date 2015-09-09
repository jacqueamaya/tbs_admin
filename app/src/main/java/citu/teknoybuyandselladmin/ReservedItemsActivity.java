package citu.teknoybuyandselladmin;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ReservedItemsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_items);

        List<String> soldItems = new ArrayList<String>();
        soldItems.add("PE T-shirt");
        soldItems.add("Rizal Book");
        soldItems.add("Uniform");

        ListView lv = (ListView)findViewById(R.id.listViewReserved);
        CustomListAdapterQueue listAdapter = new CustomListAdapterQueue(ReservedItemsActivity.this, R.layout.activity_item, soldItems);
        lv.setAdapter(listAdapter);
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
