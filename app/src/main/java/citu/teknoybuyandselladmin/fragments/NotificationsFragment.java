package citu.teknoybuyandselladmin.fragments;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import citu.teknoybuyandselladmin.Ajax;
import citu.teknoybuyandselladmin.CustomListAdapterNotification;
import citu.teknoybuyandselladmin.DashboardActivity;
import citu.teknoybuyandselladmin.DonationsActivity;
import citu.teknoybuyandselladmin.ItemsOnQueueActivity;
import citu.teknoybuyandselladmin.ListAdapters.NotificationListAdapter;
import citu.teknoybuyandselladmin.LoginActivity;
import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.ReservedItemsActivity;
import citu.teknoybuyandselladmin.Server;
import citu.teknoybuyandselladmin.models.Notification;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";
    private View view = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment NotificationsFragment.
     */
    public static NotificationsFragment newInstance(String user) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_notifications, container, false);

        view = inflater.inflate(R.layout.fragment_notifications, container, false);
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

                    ListView lv = (ListView) view.findViewById(R.id.listViewNotif);
                    NotificationListAdapter listAdapter = new NotificationListAdapter(getActivity().getBaseContext(), R.layout.activity_notification_item, notifications);
                    lv.setAdapter(listAdapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Notification notif = (Notification) parent.getItemAtPosition(position);
                            String notificationType = notif.getNotification_type();
                            Log.v(TAG, notificationType);

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
                                fragmentClass = DonationsFragment.class;
                                title = "Donations";
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
                    });

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
