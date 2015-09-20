package citu.teknoybuyandselladmin.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.Ajax;
import citu.teknoybuyandselladmin.ListAdapters.TransactionAdapter;
import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Server;
import citu.teknoybuyandselladmin.models.Transaction;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionsFragment extends Fragment {
    private static final String TAG = "NotificationsFragment";
    private View view = null;
    private TableLayout tableLayout;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment TransactionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionsFragment newInstance(String user) {
        TransactionsFragment fragment = new TransactionsFragment();
        Bundle args = new Bundle();
        args.putString("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transactions, container, false);
        tableLayout = (TableLayout) view.findViewById(R.id.transactionTable);
        Server.getTransactions(new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<Transaction> transactions = new ArrayList<Transaction>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    transactions = Transaction.allTransactions(jsonArray);
                    TransactionAdapter transAdapter = new TransactionAdapter(getActivity().getBaseContext(), transactions);
                    transAdapter.addRows(tableLayout);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
            }
        });
        return view;
    }
}
