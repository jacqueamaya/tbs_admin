package citu.teknoybuyandselladmin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import citu.teknoybuyandselladmin.Ajax;
import citu.teknoybuyandselladmin.CustomListAdapterNotification;
import citu.teknoybuyandselladmin.DashboardActivity;
import citu.teknoybuyandselladmin.LoginActivity;
import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Server;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";

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
        View view = null;
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        List<String> notifications = new ArrayList<String>();
        notifications.add("Janna bought Louie's item");
        notifications.add("Louie sold an item and is waiting for your approval");
        notifications.add("Jacque donated an item and is waiting for your approval");

        ListView lv = (ListView) view.findViewById(R.id.listViewNotif);
        CustomListAdapterNotification listAdapter = new CustomListAdapterNotification(getActivity().getBaseContext(), R.layout.activity_notification_item , notifications);
        lv.setAdapter(listAdapter);
        return view;
    }

}
