package citu.teknoybuyandselladmin.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import citu.teknoybuyandselladmin.CustomListAdapterQueue;
import citu.teknoybuyandselladmin.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservedItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservedItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment ReservedItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservedItemsFragment newInstance(String user) {
        ReservedItemsFragment fragment = new ReservedItemsFragment();
        Bundle args = new Bundle();
        args.putString("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_reserved_items, container, false);
        View view = null;
        view = inflater.inflate(R.layout.fragment_reserved_items, container, false);
        List<String> soldItems = new ArrayList<String>();
        soldItems.add("PE T-shirt");
        soldItems.add("Rizal Book");
        soldItems.add("Uniform");

        ListView lv = (ListView)view.findViewById(R.id.listViewReserved);
        CustomListAdapterQueue listAdapter = new CustomListAdapterQueue(getActivity().getBaseContext(), R.layout.activity_item, soldItems);
        lv.setAdapter(listAdapter);
        return view;
    }

}
