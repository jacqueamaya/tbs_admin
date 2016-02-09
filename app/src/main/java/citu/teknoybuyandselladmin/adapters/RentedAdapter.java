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
import citu.teknoybuyandselladmin.models.RentedItem;
import io.realm.RealmResults;

/**
 * Created by Batistil on 1/31/2016.
 */
public class RentedAdapter extends RecyclerView.Adapter<RentedAdapter.RentedViewHolder> {

    private static final String TAG = "RentedAdapter";
    private RealmResults<RentedItem> mRented;

    public RentedAdapter(RealmResults<RentedItem> rented){
        mRented = rented;
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
    }

    @Override
    public int getItemCount() {
        return mRented.size();
    }

    public class RentedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SimpleDraweeView simpleDraweeView;
        TextView textView;

        public RentedViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.txtItem);

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
            intent.putExtra("itemId", rentedItem.getItem().getId());
            intent.putExtra("itemName", rentedItem.getItem().getName());
            intent.putExtra("itemDetail", rentedItem.getItem().getDescription());
            intent.putExtra("itemPenalty", rentedItem.getPenalty());
            intent.putExtra("itemLink", rentedItem.getItem().getPicture());
            intent.putExtra("itemQuantity", rentedItem.getQuantity());
            intent.putExtra("itemCode", rentedItem.getItem_code());
            intent.putExtra("owner", rentedItem.getItem().getOwner().getUser().getUsername());
            intent.putExtra("renter", rentedItem.getRenter().getUsername());
            intent.putExtra("rentDuration", rentedItem.getItem().getRent_duration());
            intent.putExtra("rentDate", rentedItem.getRent_date());
            intent.putExtra("rentExpiry", rentedItem.getRent_expiration());
            context.startActivity(intent);
        }
    }
}
