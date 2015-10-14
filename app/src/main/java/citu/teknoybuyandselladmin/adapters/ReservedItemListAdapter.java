package citu.teknoybuyandselladmin.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.models.ReservedItem;

public class ReservedItemListAdapter extends ArrayAdapter<ReservedItem>{
    private static final String TAG = "ReservedItemListAdapter";
    private Context mContext;
    private int id;
    private ArrayList<ReservedItem> items ;
    private String reservedDate;
    private Date reserved_date;
    private Date request_date;
    private DateFormat df = new SimpleDateFormat("E, y-M-d 'at' h:m:s a");

    public ReservedItemListAdapter(Context context, int textViewResourceId, ArrayList<ReservedItem> list)
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list ;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textViewItem);

        if(items.get(position) != null )
        {
            try {
                reserved_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(items.get(position).getReservedDate());
                reservedDate = df.format(reserved_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String message;
            message = "<b>"+items.get(position).getItemName()+"</b><br><small>"+reservedDate+"</small>";
            text.setText(Html.fromHtml(message));
        }

        return mView;
    }

}
