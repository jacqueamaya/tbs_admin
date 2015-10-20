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
import java.util.Date;
import java.util.List;

import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.ReservedItem;
import citu.teknoybuyandselladmin.models.SellApproval;

public class ReservedItemListAdapter extends BaseAdapter implements Filterable{
    private static final String TAG = "ReservedItemListAdapter";
    private Context mContext;
    private int id;
    private String reservedDate;

    private ArrayList<ReservedItem> mOriginalValues;
    private ArrayList<ReservedItem> mDisplayedValues;

    public ReservedItemListAdapter(Context context, int textViewResourceId, ArrayList<ReservedItem> list)
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
        ReservedItem reservedItem = mDisplayedValues.get(position);
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textViewItem);

        ImageView image = (ImageView) mView.findViewById(R.id.image);
        Log.d(TAG, "Link: " + reservedItem.getLink());
        Picasso.with(mContext)
                .load(reservedItem.getLink())
                .placeholder(R.drawable.thumbsq_24dp)
                .resize(50, 50)
                .centerCrop()
                .into(image);

        if(reservedItem != null )
        {
            reservedDate = Utils.parseDate(reservedItem.getReservedDate());
            String message;
            message = "<b>" + reservedItem.getItemName() + "</b><br><small>"+reservedDate+"</small>";
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
                List<ReservedItem> FilteredArrList = new ArrayList<ReservedItem>();
                String searchByCategory[] = constraint.toString().split(",");

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<ReservedItem>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                if (constraint == "" || constraint.length() == 0 || searchByCategory.length == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String name = mOriginalValues.get(i).getItemName();
                        String category = mOriginalValues.get(i).getCategory();
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
                mDisplayedValues = (ArrayList<ReservedItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<ReservedItem> getDisplayView() {
        return mDisplayedValues;
    }

    public void sortItems(String sortBy) {
        switch (sortBy) {
            case "price":
                Comparator<ReservedItem> priceComparator = new Comparator<ReservedItem>() {
                    public int compare(ReservedItem obj1, ReservedItem obj2) {
                        return obj1.getPrice() < obj2.getPrice() ? -1 : obj1.getPrice() > obj2.getPrice() ? 1 : 0;
                    }
                };
                Collections.sort(mDisplayedValues, priceComparator);
                break;
            case "name":
                Comparator<ReservedItem> nameComparator = new Comparator<ReservedItem>() {
                    public int compare(ReservedItem obj1, ReservedItem obj2) {
                        return obj1.getItemName().compareTo(obj2.getItemName());
                    }
                };
                Collections.sort(mDisplayedValues, nameComparator);
                break;
            default:
                Comparator<ReservedItem> dateComparator = new Comparator<ReservedItem>() {
                    public int compare(ReservedItem obj1, ReservedItem obj2) {
                        return obj1.getReservedDate() < obj2.getReservedDate() ? -1 : obj1.getReservedDate() > obj2.getReservedDate() ? 1 : 0;
                    }
                };
                Collections.sort(mDisplayedValues, dateComparator);
                break;
        }
        notifyDataSetChanged();
    }
}
