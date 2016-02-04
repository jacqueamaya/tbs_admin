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
import citu.teknoybuyandselladmin.models.Item;
import citu.teknoybuyandselladmin.models.SellApproval;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Batistil on 1/31/2016.
 */
public class ItemsOnQueueAdapter extends RecyclerView.Adapter<ItemsOnQueueAdapter.ItemsOnQueueViewHolder>{

    private static final String TAG = "ItemsOnQueueAdapter";

    private RealmResults<SellApproval> mItemsOnQueue;

    public ItemsOnQueueAdapter(RealmResults<SellApproval> itemsOnQueue){
        mItemsOnQueue = itemsOnQueue;
    }

    @Override
    public ItemsOnQueueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item,parent,false);
        return new ItemsOnQueueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemsOnQueueViewHolder holder, int position) {
        SellApproval sellApproval = mItemsOnQueue.get(position);
        holder.simpleDraweeView.setImageURI(Uri.parse(sellApproval.getItem().getPicture()));
        holder.textView.setText(sellApproval.getItem().getName());
    }

    @Override
    public int getItemCount() {
        return mItemsOnQueue.size();
    }


    public class ItemsOnQueueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleDraweeView simpleDraweeView;
        TextView textView;

        public ItemsOnQueueViewHolder(View itemView) {
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

            SellApproval sellApproval = mItemsOnQueue.get(position);

            Intent intent = new Intent(context, QueueItemDetailActivity.class);
            intent.putExtra("requestId", sellApproval.getId());
            intent.putExtra("itemId", sellApproval.getItem().getId());
            intent.putExtra("itemName", sellApproval.getItem().getName());
            intent.putExtra("itemDetail", sellApproval.getItem().getDescription());
            intent.putExtra("itemLink", sellApproval.getItem().getPicture());
            intent.putExtra("itemPrice", sellApproval.getItem().getPrice());
            intent.putExtra("itemPurpose", sellApproval.getItem().getPurpose());
            intent.putExtra("itemOwner", sellApproval.getItem().getOwner().getUser().getUsername());
            intent.putExtra("itemQuantity", sellApproval.getItem().getQuantity());
            intent.putExtra("rentDuration", sellApproval.getItem().getRent_duration());
            intent.putExtra("requestDate", sellApproval.getRequest_date());
            context.startActivity(intent);
        }
    }
}
