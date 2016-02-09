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

import citu.teknoybuyandselladmin.DonationsDetailActivity;
import citu.teknoybuyandselladmin.QueueItemDetailActivity;
import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.models.DonateApproval;
import io.realm.RealmResults;

/**
 * Created by Batistil on 1/31/2016.
 */
public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {
    private static final String TAG = "DonationAdapter";

    private RealmResults<DonateApproval> mDonations;

    public DonationAdapter(RealmResults<DonateApproval> donations){
        mDonations = donations;
    }

    @Override
    public DonationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonationViewHolder holder, int position) {
        DonateApproval donation = mDonations.get(position);
        holder.simpleDraweeView.setImageURI(Uri.parse(donation.getItem().getPicture()));
        holder.textView.setText(donation.getItem().getName());
        Log.e(TAG,donation.getItem().getDescription());
    }

    @Override
    public int getItemCount() {
        return mDonations.size();
    }

    public class DonationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SimpleDraweeView simpleDraweeView;
        TextView textView;

        public DonationViewHolder(View itemView) {
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

            DonateApproval donation = mDonations.get(position);

            Intent intent = new Intent(context, DonationsDetailActivity.class);
            intent.putExtra("requestId",donation.getId());
            intent.putExtra("itemId", donation.getItem().getId());
            intent.putExtra("itemName", donation.getItem().getName());
            intent.putExtra("itemDetail", donation.getItem().getDescription());
            intent.putExtra("itemLink", donation.getItem().getPicture());
            intent.putExtra("itemOwner", donation.getItem().getOwner().getUser().getUsername());
            intent.putExtra("itemQuantity", donation.getItem().getQuantity());
            intent.putExtra("requestDate", donation.getRequest_date());
            context.startActivity(intent);
        }
    }
}
