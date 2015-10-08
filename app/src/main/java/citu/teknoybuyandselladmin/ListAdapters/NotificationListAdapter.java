package citu.teknoybuyandselladmin.ListAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Server;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.Notification;


public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private static final String TAG = "NotificationListAdapter";
    private Context mContext;
    private int id;
    private ArrayList<Notification> items ;
    private String notificationDate;
    private Date notif_date;
    private DateFormat df = new SimpleDateFormat("E, y-M-d 'at' h:m:s a");

    public NotificationListAdapter(Context context, int textViewResourceId, ArrayList<Notification> list)
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

        TextView text = (TextView) mView.findViewById(R.id.textView);
        ImageView image = (ImageView) mView.findViewById(R.id.image);

        if(items.get(position) != null )
        {
            Picasso.with(mContext)
                    .load(items.get(position).getItemLink())
                    .placeholder(R.drawable.notif_user)
                    .resize(50,50)
                    .centerCrop()
                    .into(image);
            try {
                notif_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(items.get(position).getNotification_date());
                notificationDate = df.format(notif_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String message;
            switch(items.get(position).getNotification_type()){
                case "sell": message = "<b>"+Utils.capitalize(items.get(position).getOwnerUsername())+" </b> wants to <b>sell</b> his/her <b>"+items.get(position).getItemName()+"</b>.<br><small>"+notificationDate+"</small>";
                        text.setText(Html.fromHtml(message));
                        break;
                case "donate": message = "<b>"+Utils.capitalize(items.get(position).getOwnerUsername())+" </b> wants to <b>donate</b> his/her <b>"+items.get(position).getItemName()+"</b>.<br><small>"+notificationDate+"</small>";
                    text.setText(Html.fromHtml(message));
                    break;
                case "buy": message = "<b>"+ Utils.capitalize(items.get(position).getMakerUsername())+" </b> wants to <b>buy</b> the <b>"+items.get(position).getItemName()+"</b> owned by <b>"+Utils.capitalize(items.get(position).getOwnerUsername())+"</b>.<br><small>"+notificationDate+"</small>";
                    text.setText(Html.fromHtml(message));
                    break;
                case "cancel": message = "<b>"+Utils.capitalize(items.get(position).getMakerUsername())+" cancels</b> his/her reservation for <b>"+items.get(position).getItemName()+"</b> owned by <b>"+Utils.capitalize(items.get(position).getOwnerUsername())+"</b>.<br><small>"+notificationDate+"</small>";
                    text.setText(Html.fromHtml(message));
                    break;
                case "get": message = "<b>"+Utils.capitalize(items.get(position).getMakerUsername())+" "+"</b> wants to <b>reserve</b> the donated item, <b>"+items.get(position).getItemName()+"</b> owned by <b>"+Utils.capitalize(items.get(position).getOwnerUsername())+"</b>.<br><small>"+notificationDate+"</small>";
                    text.setText(Html.fromHtml(message));
                    break;
                case "delete": message = "<b>"+Utils.capitalize(items.get(position).getOwnerUsername())+" deleted</b> his/her pending item, <b>"+ items.get(position).getItemName()+"</b>"+"</b>.<br><small>"+notificationDate+"</small>";
                    text.setText(Html.fromHtml(message));
                    break;
                default: message = "<i>This is a default notification message</i>";
                    text.setText(Html.fromHtml(message));
                    break;
            }
        }

        return mView;
    }

}
