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
import citu.teknoybuyandselladmin.ListAdapters.DonateApprovalAdapter;
import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Server;
import citu.teknoybuyandselladmin.models.DonateApproval;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private View view = null;
    private static final String TAG = "DonationsFragment";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment DonationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DonationsFragment newInstance(String user) {
        DonationsFragment fragment = new DonationsFragment();
        Bundle args = new Bundle();
        args.putString("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_donations, container, false);

        view = inflater.inflate(R.layout.fragment_donations, container, false);
        Server.getDonateRequests(new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<DonateApproval> request = new ArrayList<DonateApproval>();
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    request = DonateApproval.allDonateRequest(jsonArray);

                    ListView lv = (ListView) view.findViewById(R.id.listViewDonations);
                    DonateApprovalAdapter listAdapter = new DonateApprovalAdapter(getActivity().getBaseContext(), R.layout.activity_item, request);
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
