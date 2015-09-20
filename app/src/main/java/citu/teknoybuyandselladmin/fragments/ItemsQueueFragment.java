package citu.teknoybuyandselladmin.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import citu.teknoybuyandselladmin.Ajax;
import citu.teknoybuyandselladmin.CustomListAdapterQueue;
import citu.teknoybuyandselladmin.ListAdapters.SellApprovalAdapter;
import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Server;
import citu.teknoybuyandselladmin.models.SellApproval;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemsQueueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsQueueFragment extends Fragment {
    private View view = null;
    private static final String TAG = "ItemsQueueFragment";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment ItemsQueueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsQueueFragment newInstance(String user) {
        ItemsQueueFragment fragment = new ItemsQueueFragment();
        Bundle args = new Bundle();
        args.putString("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_items_queue, container, false);

        view = inflater.inflate(R.layout.fragment_items_queue, container, false);
        Server.getSellRequests(new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<SellApproval> request = new ArrayList<SellApproval>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    request = SellApproval.allSellRequest(jsonArray);

                    ListView lv = (ListView) view.findViewById(R.id.listViewQueue);
                    SellApprovalAdapter listAdapter = new SellApprovalAdapter(getActivity().getBaseContext(), R.layout.activity_item, request);
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

        return view;
    }
}
