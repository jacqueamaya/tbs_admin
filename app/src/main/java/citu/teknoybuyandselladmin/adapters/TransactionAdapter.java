package citu.teknoybuyandselladmin.adapters;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.Utils;
import citu.teknoybuyandselladmin.models.Transaction;

public class TransactionAdapter {

    private Context mContext;
    private ArrayList<Transaction> mItems;

    public TransactionAdapter(Context context, ArrayList<Transaction> list) {
        mContext = context;
        mItems = list;
    }

    public void addRows(TableLayout tableLayout) {
        for (int i = 0; i < mItems.size(); i++) {
            TableRow row = new TableRow(mContext);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.border);

            TextView id = new TextView(mContext);
            id.setText(mItems.get(i).getId()+"");
            id.setGravity(Gravity.CENTER_HORIZONTAL);
            id.setPadding(5, 5, 5, 5);
            id.setTextSize(12);
            id.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(id);

            TextView type = new TextView(mContext);
            type.setText(mItems.get(i).getItem().getPurpose());
            type.setGravity(Gravity.CENTER_HORIZONTAL);
            type.setPadding(5, 5, 5, 5);
            type.setTextSize(12);
            type.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(type);

            TextView item_code = new TextView(mContext);
            item_code.setText(mItems.get(i).getItem_code());
            item_code.setGravity(Gravity.CENTER_HORIZONTAL);
            item_code.setPadding(5, 5, 5, 5);
            item_code.setTextSize(12);
            item_code.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(item_code);


            TextView seller = new TextView(mContext);
            seller.setText(mItems.get(i).getSeller().getUser().getUsername());
            seller.setGravity(Gravity.CENTER_HORIZONTAL);
            id.setPadding(5, 5, 5, 5);
            id.setTextSize(12);
            seller.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(seller);

            TextView buyer = new TextView(mContext);
            buyer.setText(mItems.get(i).getBuyer().getUser().getUsername());
            buyer.setGravity(Gravity.CENTER_HORIZONTAL);
            buyer.setPadding(5, 5, 5, 5);
            id.setTextSize(12);
            buyer.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(buyer);

            TextView date = new TextView(mContext);
            date.setText(Utils.parseDate(mItems.get(i).getDate_claimed()));
            date.setGravity(Gravity.CENTER_HORIZONTAL);
            date.setPadding(5, 5, 5, 5);
            id.setTextSize(12);
            date.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            row.addView(date);

            tableLayout.addView(row);
        }

        tableLayout.requestLayout();
        tableLayout.invalidate();
        tableLayout.postInvalidate();
    }
}
