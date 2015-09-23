package citu.teknoybuyandselladmin.fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import citu.teknoybuyandselladmin.Ajax;
import citu.teknoybuyandselladmin.CustomListAdapterQueue;
import citu.teknoybuyandselladmin.DashboardActivity;
import citu.teknoybuyandselladmin.ListAdapters.SellApprovalAdapter;
import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Server;
import citu.teknoybuyandselladmin.models.Notification;
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
                    /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SellApproval sell = (SellApproval) parent.getItemAtPosition(position);
                            //String notificationType = notif.getNotification_type();
                            //Log.v(TAG, notificationType);

                            Fragment fragment = null;
                            Class fragmentClass = null;
                            String title = "";

                            if (notificationType.equals("sell")) {
                                Log.v(TAG, "sell");
                                fragmentClass = ItemsQueueFragment.class;
                                title = "Items on Queue";
                            } else if (notificationType.equals("buy")) {
                                Log.v(TAG, "buy");
                                fragmentClass = ReservedItemsFragment.class;
                                title = "Reserved Items";
                            } else if (notificationType.equals("donate")) {
                                Log.v(TAG, "donate");
                                fragmentClass = ItemQueueDetailFragment.class;
                                title = sell.getItemName();
                           }
                            try {
                                if (fragmentClass != null) {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ((DashboardActivity) getActivity()).setActionBarTitle(title);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.flContent, fragment);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.commit();
                        }
                    });*/

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
