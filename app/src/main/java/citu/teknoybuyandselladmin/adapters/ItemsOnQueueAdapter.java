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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import citu.teknoybuyandselladmin.QueueItemDetailActivity;
import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.Item;
import citu.teknoybuyandselladmin.models.SellApproval;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Batistil on 1/31/2016.
 */
public class ItemsOnQueueAdapter extends RecyclerView.Adapter<ItemsOnQueueAdapter.ItemsOnQueueViewHolder>{

    private static final String TAG = "ItemsOnQueueAdapter";

    public RealmResults<SellApproval> mItemsOnQueue;
    private Realm realm;

    public ItemsOnQueueAdapter(RealmResults<SellApproval> itemsOnQueue){
        mItemsOnQueue = itemsOnQueue;
        realm = Realm.getDefaultInstance();
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
        holder.txtDate.setText(Utils.parseDate(sellApproval.getRequest_date()));
    }

    @Override
    public int getItemCount() {
        return mItemsOnQueue.size();
    }

    public void search(String query){
        RealmResults<SellApproval> results = realm.where(SellApproval.class).contains("item.name",query, Case.INSENSITIVE).findAll();
        mItemsOnQueue = results;
        notifyDataSetChanged();
    }

    public void sortItems(String sortBy) {
        switch (sortBy) {
            case "price":
                //mItemsOnQueue.sort("item");
                /*Comparator<SellApproval> priceComparator = new Comparator<SellApproval>() {
                    public int compare(SellApproval obj1, SellApproval obj2) {
                        return obj1.getItem().getPrice() < obj2.getItem().getPrice() ? -1 : obj1.getItem().getPrice() > obj2.getItem().getPrice() ? 1 : 0;
                    }
                };
                Collections.sort(mItemsOnQueue, priceComparator);*/
                break;
            case "name":
                /*Comparator<SellApproval> nameComparator = new Comparator<SellApproval>() {
                    public int compare(SellApproval obj1, SellApproval obj2) {
                        return obj1.getItem().getName().compareTo(obj2.getItem().getName());
                    }
                };
                Collections.sort(mItemsOnQueue, nameComparator);*/
                break;
            default:
                mItemsOnQueue.sort("request_date", Sort.DESCENDING);
                /*Comparator<SellApproval> dateComparator = new Comparator<SellApproval>() {
                    public int compare(SellApproval obj1, SellApproval obj2) {
                        return Utils.parseDate(obj1.getRequest_date()).compareTo(Utils.parseDate(obj2.getRequest_date()));
                    }
                };
                Collections.sort(mItemsOnQueue, Collections.reverseOrder(dateComparator));*/
                break;
        }
        notifyDataSetChanged();
    }


    public class ItemsOnQueueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleDraweeView simpleDraweeView;
        TextView textView;
        TextView txtDate;

        public ItemsOnQueueViewHolder(View itemView) {
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

            SellApproval sellApproval = mItemsOnQueue.get(position);

            Intent intent = new Intent(context, QueueItemDetailActivity.class);
            intent.putExtra("requestId",sellApproval.getId());
            context.startActivity(intent);
        }
    }
}
