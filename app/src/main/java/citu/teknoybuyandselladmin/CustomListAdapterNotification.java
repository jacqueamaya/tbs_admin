package citu.teknoybuyandselladmin;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Batistil on 9/9/2015.
 */
public class CustomListAdapterNotification extends ArrayAdapter {

    private Context mContext;
    private int id;
    private List<String> items ;

    public CustomListAdapterNotification(Context context, int textViewResourceId, List<String> list)
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
            text.setText(items.get(position));
            image.setImageResource(R.drawable.notif_user);
        }

        return mView;
    }

}
