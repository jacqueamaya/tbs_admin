package citu.teknoybuyandselladmin;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class NotificationActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        List<String> notifications = new ArrayList<String>();
        notifications.add("Janna bought Louie's item");
        notifications.add("Louie sold an item and is waiting for your approval");
        notifications.add("Jacque donated an item and is waiting for your approval");
       /** ListView listview = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  android.R.id.text1,notifications);
        listview.setAdapter(adapter);**/

        ListView lv = (ListView)findViewById(R.id.listViewNotif);
        CustomListAdapterNotification listAdapter = new CustomListAdapterNotification(NotificationActivity.this, R.layout.activity_notification_item , notifications);
        lv.setAdapter(listAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
