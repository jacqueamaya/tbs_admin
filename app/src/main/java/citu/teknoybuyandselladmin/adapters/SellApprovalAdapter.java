package citu.teknoybuyandselladmin.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.SellApproval;

public class SellApprovalAdapter extends BaseAdapter implements Filterable {
    private static final String TAG = "SellApprovalAdapter";
    private Context mContext;
    private int id;
    private String requestDate;
    private Date request_date;

    private ArrayList<SellApproval> mOriginalValues;
    private ArrayList<SellApproval> mDisplayedValues;

    public SellApprovalAdapter(Context context, int textViewResourceId, ArrayList<SellApproval> list)
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
        SellApproval sell  = mDisplayedValues.get(position);
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textViewItem);
        ImageView image = (ImageView) mView.findViewById(R.id.image);
        Picasso.with(mContext)
                .load(sell.getLink())
                .placeholder(R.drawable.thumbsq_24dp)
                .resize(50, 50)
                .centerCrop()
                .into(image);
        requestDate = Utils.parseDate(sell.getRequestDate());
        String message;
        message = "<b>"+sell.getItemName()+"</b><br><small>"+requestDate+"</small>";
        text.setText(Html.fromHtml(message));

        return mView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<SellApproval> FilteredArrList = new ArrayList<SellApproval>();
                String searchByCategory[] = constraint.toString().split(",");
                Log.d("ItemsListAdapter", searchByCategory.length+"");
                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<SellApproval>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0 || searchByCategory.length == 0) {
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
                mDisplayedValues = (ArrayList<SellApproval>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<SellApproval> getDisplayView() {
        return mDisplayedValues;
    }

    public void sortItems(String sortBy) {
        switch (sortBy) {
            case "price":
                Comparator<SellApproval> priceComparator = new Comparator<SellApproval>() {
                    public int compare(SellApproval obj1, SellApproval obj2) {
                        return obj1.getPrice() < obj2.getPrice() ? -1 : obj1.getPrice() > obj2.getPrice() ? 1 : 0;
                    }
                };
                Collections.sort(mDisplayedValues, priceComparator);
                break;
            case "name":
                Comparator<SellApproval> nameComparator = new Comparator<SellApproval>() {
                    public int compare(SellApproval obj1, SellApproval obj2) {
                        return obj1.getItemName().compareTo(obj2.getItemName());
                    }
                };
                Collections.sort(mDisplayedValues, nameComparator);
                break;
            default:
                Comparator<SellApproval> dateComparator = new Comparator<SellApproval>() {
                    public int compare(SellApproval obj1, SellApproval obj2) {
                        return obj1.getStrRequestDate().compareTo(obj2.getStrRequestDate());
                    }
                };
                Collections.sort(mDisplayedValues, dateComparator);
                break;
        }
        notifyDataSetChanged();
    }

}
