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
import citu.teknoybuyandselladmin.models.SellApproval;

public class SellApprovalAdapter extends ArrayAdapter<SellApproval>{
    private static final String TAG = "SellApprovalAdapter";
    private Context mContext;
    private int id;
    private ArrayList<SellApproval> items ;
    private String requestDate;
    private Date request_date;
    private DateFormat df = new SimpleDateFormat("E, y-M-d 'at' h:m:s a");

    public SellApprovalAdapter(Context context, int textViewResourceId, ArrayList<SellApproval> list)
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
                request_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(items.get(position).getRequestDate());
                requestDate = df.format(request_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String message;
            message = "<b>"+items.get(position).getItemName()+"</b><br><small>"+requestDate+"</small>";
            text.setText(Html.fromHtml(message));
        }

        return mView;
    }

}
