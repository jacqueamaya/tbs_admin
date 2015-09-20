package citu.teknoybuyandselladmin.ListAdapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
    private Date request_date;

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
                request_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(items.get(position).getRequest_date());
                // String notificationDate = new SimpleDateFormat("yyyy-MM-dd").format(notif_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String message;
            message = "<b>"+items.get(position).getItemName()+"</b><br><small> Date: "+request_date+"</small>";
            text.setText(Html.fromHtml(message));
        }

        return mView;
    }

}
