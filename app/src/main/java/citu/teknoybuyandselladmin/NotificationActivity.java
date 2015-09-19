package citu.teknoybuyandselladmin;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import citu.teknoybuyandselladmin.models.Notification;


public class NotificationActivity extends ActionBarActivity {

    private static final String TAG = "NotificationActivity";
    private String ownerFirstName,ownerLastName,ownerIdNumber,buyerFirstName,buyerLastName,buyerIdNumber,itemName,type,notification_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Log.v(TAG,"This should be printed first");

        List<String> notifications = new ArrayList<String>();
        notifications.add("Janna bought Louie's item dsagddfhghfgjhgjytut");
        notifications.add("Louie sold an item and is waiting for your approval");
        notifications.add("Jacque donated an item and is waiting for your approval");

        ListView lv = (ListView)findViewById(R.id.listViewNotif);
        CustomListAdapterNotification listAdapter = new CustomListAdapterNotification(NotificationActivity.this, R.layout.activity_notification_item , notifications);
        lv.setAdapter(listAdapter);

        getNotifications();
    }

    public void getNotifications(){
        Log.v(TAG,"notificATION activity");
        String username = "admin";
        Server.getNotifications(username, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Notification> notifications = new ArrayList<Notification>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    notifications = Notification.allNotifications(jsonArray);

                    ListView lv = (ListView)findViewById(R.id.listViewNotif);
                    NotificationListAdapter listAdapter = new NotificationListAdapter(NotificationActivity.this, R.layout.activity_notification_item , notifications);
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
