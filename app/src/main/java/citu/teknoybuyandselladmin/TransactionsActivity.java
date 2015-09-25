package citu.teknoybuyandselladmin;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.ListAdapters.TransactionAdapter;
import citu.teknoybuyandselladmin.models.Transaction;


public class TransactionsActivity extends BaseActivity {
    private static final String TAG = "TransactionsActivity";
    TableLayout table_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        setupUI();

        table_layout = (TableLayout) findViewById(R.id.transactionTable);
        getTransactions(table_layout);
    }

    public void getTransactions(final TableLayout tl){
        Server.getTransactions(new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Transaction> transactions = new ArrayList<Transaction>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    transactions = Transaction.allTransactions(jsonArray);

                    TransactionAdapter transAdapter = new TransactionAdapter(TransactionsActivity.this,transactions);
                    transAdapter.addRows(tl);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transactions, menu);
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
        return menuItem.getItemId() != R.id.nav_transactions;
    }
}
