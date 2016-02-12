package citu.teknoybuyandselladmin.adapters;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.Transaction;
import io.realm.RealmResults;

public class TransactionAdapter {

    private Context mContext;
    private RealmResults<Transaction> mItems;

    public TransactionAdapter(Context context, RealmResults<Transaction> list) {
        mContext = context;
        mItems = list;
    }

    public void update(RealmResults<Transaction> list){
        mItems = list;
    }

    public void addRows(TableLayout tableLayout) {
        TableRow header = new TableRow(mContext);
        header.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        header.setBackgroundResource(R.drawable.border);

        TextView hid = new TextView(mContext);
        hid.setText("NO");
        hid.setGravity(Gravity.CENTER_HORIZONTAL);
        hid.setPadding(15, 10, 15, 10);
        hid.setTextSize(14);
        hid.setTextAppearance(mContext, R.style.boldText);
        hid.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
        header.addView(hid);

        TextView htype = new TextView(mContext);
        htype.setText("TYPE");
        htype.setGravity(Gravity.CENTER_HORIZONTAL);
        htype.setPadding(15, 10, 15, 10);
        htype.setTextSize(14);
        htype.setTextAppearance(mContext, R.style.boldText);
        htype.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
        header.addView(htype);

        TextView hitem_name = new TextView(mContext);
        hitem_name.setText("ITEM NAME");
        hitem_name.setGravity(Gravity.CENTER_HORIZONTAL);
        hitem_name.setPadding(15,10,15,10);
        hitem_name.setTextSize(14);
        hitem_name.setTextAppearance(mContext, R.style.boldText);
        hitem_name.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
        header.addView(hitem_name);

        TextView hitem_code = new TextView(mContext);
        hitem_code.setText("ITEM CODE");
        hitem_code.setGravity(Gravity.CENTER_HORIZONTAL);
        hitem_code.setPadding(15,10,15,10);
        hitem_code.setTextSize(14);
        hitem_code.setTextAppearance(mContext, R.style.boldText);
        hitem_code.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
        header.addView(hitem_code);


        TextView hseller = new TextView(mContext);
        hseller.setText("OWNED BY");
        hseller.setGravity(Gravity.CENTER_HORIZONTAL);
        hseller.setPadding(15,10,15,10);
        hseller.setTextSize(14);
        hseller.setTextAppearance(mContext, R.style.boldText);
        hseller.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
        header.addView(hseller);

        TextView hbuyer = new TextView(mContext);
        hbuyer.setText("RECEIVED BY");
        hbuyer.setGravity(Gravity.CENTER_HORIZONTAL);
        hbuyer.setPadding(15,10,15,10);
        hbuyer.setTextSize(14);
        hbuyer.setTextAppearance(mContext, R.style.boldText);
        hbuyer.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
        header.addView(hbuyer);


        TextView htotalPayment = new TextView(mContext);
        htotalPayment.setText("TOTAL PAYMENT");
        htotalPayment.setGravity(Gravity.CENTER_HORIZONTAL);
        htotalPayment.setPadding(15,10,15,10);
        htotalPayment.setTextSize(14);
        htotalPayment.setTextAppearance(mContext, R.style.boldText);
        htotalPayment.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
        header.addView(htotalPayment);


        TextView huserShare = new TextView(mContext);
        huserShare.setText("USER SHARE");
        huserShare.setGravity(Gravity.CENTER_HORIZONTAL);
        huserShare.setPadding(15,10,15,10);
        huserShare.setTextSize(14);
        huserShare.setTextAppearance(mContext, R.style.boldText);
        huserShare.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
        header.addView(huserShare);


        TextView htbsShare = new TextView(mContext);
        htbsShare.setText("TBS SHARE");
        htbsShare.setGravity(Gravity.CENTER_HORIZONTAL);
        htbsShare.setPadding(15,10,15,10);
        htbsShare.setTextSize(14);
        htbsShare.setTextAppearance(mContext, R.style.boldText);
        htbsShare.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
        header.addView(htbsShare);


        TextView hdate = new TextView(mContext);
        hdate.setText("DATE");
        hdate.setGravity(Gravity.CENTER_HORIZONTAL);
        hdate.setPadding(15,10,15,10);
        hdate.setTextSize(14);
        hdate.setTextAppearance(mContext, R.style.boldText);
        hdate.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
        header.addView(hdate);

        tableLayout.addView(header);

        for (int i = 0; i < mItems.size(); i++) {
            TableRow row = new TableRow(mContext);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.border);

            TextView id = new TextView(mContext);
            id.setText(mItems.get(i).getId() + "");
            id.setGravity(Gravity.CENTER_HORIZONTAL);
            id.setPadding(15,10,15,10);
            id.setTextSize(13);
            id.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(id);

            TextView type = new TextView(mContext);
            type.setText(mItems.get(i).getTransaction_type());
            type.setGravity(Gravity.CENTER_HORIZONTAL);
            type.setPadding(15,10,15,10);
            type.setTextSize(13);
            type.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(type);


            TextView item_name = new TextView(mContext);
            item_name.setText(Utils.capitalize(mItems.get(i).getItem_name()));
            item_name.setGravity(Gravity.CENTER_HORIZONTAL);
            item_name.setPadding(15,10,15,10);
            item_name.setTextSize(13);
            item_name.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(item_name);


            TextView item_code = new TextView(mContext);
            item_code.setText(mItems.get(i).getItem_code());
            item_code.setGravity(Gravity.CENTER_HORIZONTAL);
            item_code.setPadding(15,10,15,10);
            item_code.setTextSize(13);
            item_code.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(item_code);


            TextView seller = new TextView(mContext);
            seller.setText(Utils.capitalize(mItems.get(i).getSeller()));
            seller.setGravity(Gravity.CENTER_HORIZONTAL);
            seller.setPadding(15,10,15,10);
            seller.setTextSize(13);
            seller.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(seller);

            TextView buyer = new TextView(mContext);
            buyer.setText(Utils.capitalize(mItems.get(i).getBuyer()));
            buyer.setGravity(Gravity.CENTER_HORIZONTAL);
            buyer.setPadding(15,10,15,10);
            buyer.setTextSize(13);
            buyer.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(buyer);


            TextView totalPayment = new TextView(mContext);
            totalPayment.setText("Php "+Utils.formatFloat(mItems.get(i).getTotal_payment()));
            totalPayment.setGravity(Gravity.CENTER_HORIZONTAL);
            totalPayment.setPadding(15,10,15,10);
            totalPayment.setTextSize(13);
            totalPayment.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(totalPayment);


            TextView userShare = new TextView(mContext);
            userShare.setText("Php "+Utils.formatFloat(mItems.get(i).getUser_share()));
            userShare.setGravity(Gravity.CENTER_HORIZONTAL);
            userShare.setPadding(15,10,15,10);
            userShare.setTextSize(13);
            userShare.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(userShare);


            TextView tbsShare = new TextView(mContext);
            tbsShare.setText("Php "+Utils.formatFloat(mItems.get(i).getTbs_share()));
            tbsShare.setGravity(Gravity.CENTER_HORIZONTAL);
            tbsShare.setPadding(15,10,15,10);
            tbsShare.setTextSize(13);
            tbsShare.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(tbsShare);


            TextView date = new TextView(mContext);
            date.setText(Utils.parseDate(mItems.get(i).getDate_claimed()));
            date.setGravity(Gravity.CENTER_HORIZONTAL);
            date.setPadding(15,10,15,10);
            date.setTextSize(13);
            date.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(date);

            tableLayout.addView(row);
        }

        tableLayout.requestLayout();
        tableLayout.invalidate();
    }

    public void clear(TableLayout tableLayout){
        tableLayout.removeAllViews();
        tableLayout.invalidate();
    }
}
