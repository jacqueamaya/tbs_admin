package citu.teknoybuyandselladmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnDashboard;
        Button btnNotifications;
        Button btnItemsQueue;
        Button btnReservedItems;
        Button btnDonations;
        Button btnTransactions;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        btnDashboard = (Button) findViewById(R.id.btnDashboard);
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, DashboardActivity.class);
                intent.putExtra("admin", "Admin");
                startActivity(intent);
            }
        });
        btnNotifications = (Button) findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, NotificationsActivity.class);
                intent.putExtra("admin", "Admin");
                startActivity(intent);
            }
        });
        btnItemsQueue = (Button) findViewById(R.id.btnItemsQueue);
        btnItemsQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, ItemsOnQueueActivity.class);
                intent.putExtra("admin", "Admin");
                startActivity(intent);
            }
        });
        btnReservedItems = (Button) findViewById(R.id.btnReservedItems);
        btnReservedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, ReservedItemsActivity.class);
                intent.putExtra("admin", "Admin");
                startActivity(intent);
            }
        });
        btnDonations = (Button) findViewById(R.id.btnDonations);
        btnDonations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, DonationsActivity.class);
                intent.putExtra("admin", "Admin");
                startActivity(intent);
            }
        });
        btnTransactions = (Button) findViewById(R.id.btnTransactions);
        btnTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, Transactions.class);
                intent.putExtra("admin", "Admin");
                startActivity(intent);
            }
        }); */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
