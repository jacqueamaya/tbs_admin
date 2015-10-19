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
import java.util.TimeZone;

import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.DonateApproval;

/**
 * Created by Batistil on 9/20/2015.
 */
public class DonateApprovalAdapter extends ArrayAdapter<DonateApproval>{
    private static final String TAG = "DonateApprovalAdapter";
    private Context mContext;
    private int id;
    private ArrayList<DonateApproval> items;
    private String requestDate;

    public DonateApprovalAdapter(Context context, int textViewResourceId, ArrayList<DonateApproval> list)
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
            //try {
                //read datetime
                /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
                Date date = sdf.parse(Utils.parseDate(items.get(position).getRequestDate()));

                //format datetime
                SimpleDateFormat sdf2 = new SimpleDateFormat("E, y-M-d 'at' h:m:s a");
                sdf2.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
                requestDate = sdf2.format(date);*/

                //Date date = Utils.FORMATTED_DATE_FORMAT.parse(items.get(position).getRequestDate());
                requestDate = Utils.parseDate(items.get(position).getRequestDate());

            /*} catch (ParseException e) {
                e.printStackTrace();
            }*/
            String message;
            message = "<b>"+items.get(position).getItemName()+"</b><br><small>"+requestDate+"</small>";
            text.setText(Html.fromHtml(message));
        }

        return mView;
    }
}
