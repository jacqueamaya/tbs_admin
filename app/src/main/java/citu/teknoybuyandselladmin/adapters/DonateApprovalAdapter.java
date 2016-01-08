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
import citu.teknoybuyandselladmin.models.DonateApproval;

/**
 * Created by Batistil on 9/20/2015.
 */
public class DonateApprovalAdapter extends BaseAdapter implements Filterable {
    private static final String TAG = "DonateApprovalAdapter";
    private Context mContext;
    private int id;
    private String requestDate;

    private ArrayList<DonateApproval> mOriginalValues;
    private ArrayList<DonateApproval> mDisplayedValues;

    public DonateApprovalAdapter(Context context, int textViewResourceId, ArrayList<DonateApproval> list)
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
        DonateApproval donate = mDisplayedValues.get(position);
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textViewItem);
        ImageView image = (ImageView) mView.findViewById(R.id.image);
        Picasso.with(mContext)
                .load(donate.getItem().getPicture())
                .placeholder(R.drawable.thumbsq_24dp)
                .resize(50, 50)
                .centerCrop()
                .into(image);
        requestDate = Utils.parseDate(donate.getRequest_date());
        String message;
        message = "<b>"+donate.getItem().getName()+"</b><br><small>"+requestDate+"</small>";
        text.setText(Html.fromHtml(message));

        return mView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<DonateApproval> FilteredArrList = new ArrayList<DonateApproval>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<DonateApproval>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                if (constraint == "" || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String name = mOriginalValues.get(i).getItem().getName();
                        String category = mOriginalValues.get(i).getItem().getCategory().getCategory_name();
                        if (category.equals(constraint.toString()) || name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            FilteredArrList.add(mOriginalValues.get(i));
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
                mDisplayedValues = (ArrayList<DonateApproval>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<DonateApproval> getDisplayView() {
        return mDisplayedValues;
    }

    public void sortItems(String sortBy) {
        switch (sortBy) {
            case "name":
                Comparator<DonateApproval> nameComparator = new Comparator<DonateApproval>() {
                    public int compare(DonateApproval obj1, DonateApproval obj2) {
                        return obj1.getItem().getName().compareTo(obj2.getItem().getName());
                    }
                };
                Collections.sort(mDisplayedValues, nameComparator);
                break;
            default:
                Comparator<DonateApproval> dateComparator = new Comparator<DonateApproval>() {
                    public int compare(DonateApproval obj1, DonateApproval obj2) {
                        return Utils.parseDate(obj1.getRequest_date()).compareTo(Utils.parseDate(obj2.getRequest_date()));
                    }
                };
                Collections.sort(mDisplayedValues, Collections.reverseOrder(dateComparator));
                break;
        }
        notifyDataSetChanged();
    }
}
