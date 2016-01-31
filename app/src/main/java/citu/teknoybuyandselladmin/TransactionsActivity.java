package citu.teknoybuyandselladmin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.adapters.TransactionAdapter;
import citu.teknoybuyandselladmin.models.Transaction;
import citu.teknoybuyandselladmin.services.ExpirationCheckerService;
import citu.teknoybuyandselladmin.services.NotificationService;
import citu.teknoybuyandselladmin.services.TransactionService;
import io.realm.Realm;
import io.realm.RealmResults;


public class TransactionsActivity extends BaseActivity {

    private static final String TAG = "TransactionsActivity";

    private TableLayout mTableLayout;
    private ProgressBar mProgressBar;

    private TextView mTxtMessage;

    private Gson gson = new Gson();

    private TransactionBroadcastReceiver mReceiver;
    private Realm realm;
    private TransactionAdapter transAdapter;
    private RealmResults<Transaction> transactions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        setupUI();

        realm = Realm.getDefaultInstance();
        mReceiver = new TransactionBroadcastReceiver();
        mTableLayout = (TableLayout) findViewById(R.id.transactionTable);
        mProgressBar = (ProgressBar) findViewById(R.id.progressGetTransactions);
        mTxtMessage = (TextView) findViewById(R.id.txtMessage);
        mProgressBar.setVisibility(View.GONE);
        getTransactions();
        transactions = realm.where(Transaction.class).findAll();
        Log.e(TAG, transactions.get(0).getItem().getName());
        if(transactions.size() == 0){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        transAdapter = new TransactionAdapter(TransactionsActivity.this, transactions);
        transAdapter.addRows(mTableLayout);
    }

    public void getTransactions() {
        Intent intent = new Intent(this, TransactionService.class);
        startService(intent);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_transactions;
    }

    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(TransactionService.class.getCanonicalName()));
        transAdapter.update(transactions);

        startService(new Intent(this, ExpirationCheckerService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }


    private class TransactionBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, intent.getStringExtra("response"));
            mProgressBar.setVisibility(View.GONE);

            transactions = realm.where(Transaction.class).findAll();
            transAdapter.update(transactions);

            if(intent.getIntExtra("result",0) == -1){
                Snackbar.make(mTableLayout, "No internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }

    }
}
