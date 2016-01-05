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
import citu.teknoybuyandselladmin.models.RentedItem;

/**
 * Created by Batistil on 1/3/2016.
 */
public class RentedItemAdapter extends BaseAdapter implements Filterable {
    private static final String TAG = "RentedItemAdapter";
    private Context mContext;
    private int id;
    private String rentDate;

    private ArrayList<RentedItem> mOriginalValues;
    private ArrayList<RentedItem> mDisplayedValues;

    public RentedItemAdapter(Context context, int textViewResourceId, ArrayList<RentedItem> list)
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
        RentedItem rentedItem = mDisplayedValues.get(position);
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textViewItem);

        ImageView image = (ImageView) mView.findViewById(R.id.image);
        Log.d(TAG, "Link: " + rentedItem.getItem().getPicture());
        Picasso.with(mContext)
                .load(rentedItem.getItem().getPicture())
                .placeholder(R.drawable.thumbsq_24dp)
                .resize(50, 50)
                .centerCrop()
                .into(image);

        if(rentedItem != null )
        {
            rentDate = Utils.parseDate(rentedItem.getRent_date());
            String message;
            message = "<b>" + rentedItem.getItem().getName() + "</b><br><small>"+ rentDate +"</small>";
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
                List<RentedItem> FilteredArrList = new ArrayList<RentedItem>();
                String searchByCategory[] = constraint.toString().split(",");

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<RentedItem>(mDisplayedValues); // saves the original data in mOriginalValues
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
                mDisplayedValues = (ArrayList<RentedItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<RentedItem> getDisplayView() {
        return mDisplayedValues;
    }

    public void sortItems(String sortBy) {
        switch (sortBy) {
            case "price":
                Comparator<RentedItem> priceComparator = new Comparator<RentedItem>() {
                    public int compare(RentedItem obj1, RentedItem obj2) {
                        return obj1.getItem().getPrice() < obj2.getItem().getPrice() ? -1 : obj1.getItem().getPrice() > obj2.getItem().getPrice() ? 1 : 0;
                    }
                };
                Collections.sort(mDisplayedValues, priceComparator);
                break;
            case "name":
                Comparator<RentedItem> nameComparator = new Comparator<RentedItem>() {
                    public int compare(RentedItem obj1, RentedItem obj2) {
                        return obj1.getItem().getName().compareTo(obj2.getItem().getName());
                    }
                };
                Collections.sort(mDisplayedValues, nameComparator);
                break;
            default:
                Comparator<RentedItem> dateComparator = new Comparator<RentedItem>() {
                    public int compare(RentedItem obj1, RentedItem obj2) {
                        return obj1.getRent_date() < obj2.getRent_date() ? -1 : obj1.getRent_date() > obj2.getRent_date() ? 1 : 0;
                    }
                };
                Collections.sort(mDisplayedValues, Collections.reverseOrder(dateComparator));
                break;
        }
        notifyDataSetChanged();
    }
}
