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
import citu.teknoybuyandselladmin.ReservedDetailActivity;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.Reservation;
import io.realm.RealmResults;

/**
 * Created by Batistil on 1/31/2016.
 */
public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>{

    private static final String TAG = "ReservationAdapter";

    private RealmResults<Reservation> mReservation;

    public ReservationAdapter(RealmResults<Reservation> reservations){
        mReservation = reservations;
    }

    @Override
    public ReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservationViewHolder holder, int position) {
        Reservation reservation = mReservation.get(position);
        holder.simpleDraweeView.setImageURI(Uri.parse(reservation.getItem().getPicture()));
        holder.textView.setText(reservation.getItem().getName());
        holder.txtDate.setText(Utils.parseDate(reservation.getReserved_date()));
    }

    @Override
    public int getItemCount() {
        return mReservation.size();
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SimpleDraweeView simpleDraweeView;
        TextView textView;
        TextView txtDate;

        public ReservationViewHolder(View itemView) {
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

            Reservation reservation = mReservation.get(position);

            Intent intent = new Intent(context, ReservedDetailActivity.class);
            intent.putExtra("requestId", reservation.getId());
            context.startActivity(intent);
        }
    }
}
