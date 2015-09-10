package citu.teknoybuyandselladmin.fragments;

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
 * Use the {@link ItemsQueueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsQueueFragment extends Fragment {
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
        View view = null;
        view = inflater.inflate(R.layout.fragment_items_queue, container, false);
        List<String> soldItems = new ArrayList<String>();
        soldItems.add("PE T-shirt");
        soldItems.add("Rizal Book");
        soldItems.add("Uniform");

        ListView lv = (ListView) view.findViewById(R.id.listViewQueue);
        CustomListAdapterQueue listAdapter = new CustomListAdapterQueue(getActivity().getBaseContext(), R.layout.activity_item, soldItems);
        lv.setAdapter(listAdapter);

        return view;
    }
}
