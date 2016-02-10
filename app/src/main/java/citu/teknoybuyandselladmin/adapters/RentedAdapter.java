package citu.teknoybuyandselladmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import citu.teknoybuyandselladmin.QueueItemDetailActivity;
import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.RentedItemDetailActivity;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.RentedItem;
import citu.teknoybuyandselladmin.models.SellApproval;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Batistil on 1/31/2016.
 */
public class RentedAdapter extends RecyclerView.Adapter<RentedAdapter.RentedViewHolder> {

    private static final String TAG = "RentedAdapter";
    private RealmResults<RentedItem> mRented;
    private Realm realm;

    public RentedAdapter(RealmResults<RentedItem> rented){
        mRented = rented;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public RentedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new RentedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RentedViewHolder holder, int position) {
        RentedItem rentedItem = mRented.get(position);
        holder.simpleDraweeView.setImageURI(Uri.parse(rentedItem.getItem().getPicture()));
        holder.textView.setText(rentedItem.getItem().getName());
        holder.txtDate.setText(Utils.parseDate(rentedItem.getRent_date()));
    }

    @Override
    public int getItemCount() {
        return mRented.size();
    }

    public void search(String query){
        RealmResults<RentedItem> results = realm.where(RentedItem.class).contains("item.name",query, Case.INSENSITIVE).findAll();
        mRented = results;
        notifyDataSetChanged();
    }

    public class RentedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SimpleDraweeView simpleDraweeView;
        TextView textView;
        TextView txtDate;

        public RentedViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.txtItem);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.e(TAG, "on click");
            final Context context = view.getContext();
            int position = getAdapterPosition();

            RentedItem rentedItem = mRented.get(position);

            Intent intent = new Intent(context, RentedItemDetailActivity.class);
            intent.putExtra("rentId", rentedItem.getId());
            context.startActivity(intent);
        }
    }
}
