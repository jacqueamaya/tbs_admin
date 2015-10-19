package citu.teknoybuyandselladmin;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.adapters.TransactionAdapter;
import citu.teknoybuyandselladmin.models.Transaction;


public class TransactionsActivity extends BaseActivity {

    private static final String TAG = "TransactionsActivity";

    private TableLayout mTableLayout;
    private ProgressBar mProgressBar;

    private TextView mTxtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        setupUI();

        mTableLayout = (TableLayout) findViewById(R.id.transactionTable);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetTransactions);

        mTxtMessage = (TextView) findViewById(R.id.txtMessage);
        getTransactions(mTableLayout);
    }

    public void getTransactions(final TableLayout tl) {
        Server.getTransactions(mProgressBar, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Transaction> transactions = new ArrayList<Transaction>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    if (jsonArray.length() == 0) {
                        mTxtMessage.setText("No transactions");
                        mTxtMessage.setVisibility(View.VISIBLE);
                    } else {
                        transactions = Transaction.asList(jsonArray);

                        TransactionAdapter transAdapter = new TransactionAdapter(TransactionsActivity.this, transactions);
                        transAdapter.addRows(tl);
                    }

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
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_transactions;
    }
}
