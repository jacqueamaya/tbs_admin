package citu.teknoybuyandselladmin.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.Reservation;

public class ReservedItemListAdapter extends BaseAdapter implements Filterable{
    private static final String TAG = "ReservedItemListAdapter";
    private Context mContext;
    private int id;
    private String reservedDate;

    private ArrayList<Reservation> mOriginalValues;
    private ArrayList<Reservation> mDisplayedValues;

    public ReservedItemListAdapter(Context context, int textViewResourceId, ArrayList<Reservation> list)
    {
        mContext = context;
        id = textViewResourceId;

        mOriginalValues = list;
        mDisplayedValues = list;
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        Reservation reservedItem = mDisplayedValues.get(position);
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textViewItem);

        ImageView image = (ImageView) mView.findViewById(R.id.image);
        Log.d(TAG, "Link: " + reservedItem.getItem().getPicture());
        Picasso.with(mContext)
                .load(reservedItem.getItem().getPicture())
                .placeholder(R.drawable.thumbsq_24dp)
                .resize(50, 50)
                .centerCrop()
                .into(image);

        if(reservedItem != null )
        {
            reservedDate = Utils.parseDate(reservedItem.getReserved_date());
            String message;
            message = "<b>" + reservedItem.getItem().getName() + "</b><br><small>"+reservedDate+"</small>";
            text.setText(Html.fromHtml(message));
        }

        return mView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Reservation> FilteredArrList = new ArrayList<Reservation>();
                String searchByCategory[] = constraint.toString().split(",");

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Reservation>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                if (constraint == "" || constraint.length() == 0 || searchByCategory.length == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String name = mOriginalValues.get(i).getItem().getName();
                        String category = mOriginalValues.get(i).getItem().getCategory().getCategory_name();
                        if(searchByCategory.length == 2) {
                            if (category.equals(searchByCategory[1]) && name.toLowerCase().contains(searchByCategory[0].toLowerCase())) {
                                FilteredArrList.add(mOriginalValues.get(i));
                            }
                        } else {
                            if (category.equals(constraint.toString()) || name.toLowerCase().contains(searchByCategory[0].toLowerCase())) {
                                FilteredArrList.add(mOriginalValues.get(i));
                            }
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDisplayedValues = (ArrayList<Reservation>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<Reservation> getDisplayView() {
        return mDisplayedValues;
    }

    public void sortItems(String sortBy) {
        switch (sortBy) {
            case "price":
                Comparator<Reservation> priceComparator = new Comparator<Reservation>() {
                    public int compare(Reservation obj1, Reservation obj2) {
                        return obj1.getItem().getPrice() < obj2.getItem().getPrice() ? -1 : obj1.getItem().getPrice() > obj2.getItem().getPrice() ? 1 : 0;
                    }
                };
                Collections.sort(mDisplayedValues, priceComparator);
                break;
            case "name":
                Comparator<Reservation> nameComparator = new Comparator<Reservation>() {
                    public int compare(Reservation obj1, Reservation obj2) {
                        return obj1.getItem().getName().compareTo(obj2.getItem().getName());
                    }
                };
                Collections.sort(mDisplayedValues, nameComparator);
                break;
            default:
                Comparator<Reservation> dateComparator = new Comparator<Reservation>() {
                    public int compare(Reservation obj1, Reservation obj2) {
                        return obj1.getReserved_date() < obj2.getReserved_date() ? -1 : obj1.getReserved_date() > obj2.getReserved_date() ? 1 : 0;
                    }
                };
                Collections.sort(mDisplayedValues, Collections.reverseOrder(dateComparator));
                break;
        }
        notifyDataSetChanged();
    }
}
