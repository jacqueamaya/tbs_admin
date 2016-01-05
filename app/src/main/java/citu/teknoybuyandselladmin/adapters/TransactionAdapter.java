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
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.border);

            TextView id = new TextView(mContext);
            id.setText(mItems.get(i).getId()+"");
            id.setGravity(Gravity.CENTER_HORIZONTAL);
            id.setPadding(5, 5, 5, 5);
            id.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.1f));
            row.addView(id);

            TextView seller = new TextView(mContext);
            seller.setText(mItems.get(i).getSeller().getUser().getUsername());
            seller.setGravity(Gravity.CENTER_HORIZONTAL);
            id.setPadding(5, 5, 5, 5);
            seller.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
            row.addView(seller);

            TextView buyer = new TextView(mContext);
            buyer.setText(mItems.get(i).getBuyer().getUser().getUsername());
            buyer.setGravity(Gravity.CENTER_HORIZONTAL);
            buyer.setPadding(5, 5, 5, 5);
            buyer.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
            row.addView(buyer);

            TextView date = new TextView(mContext);
            date.setText(Utils.parseDate(mItems.get(i).getDate_claimed()));
            date.setGravity(Gravity.CENTER_HORIZONTAL);
            date.setPadding(5, 5, 5, 5);
            date.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.3f));
            row.addView(date);
            tableLayout.addView(row);
        }

        tableLayout.requestLayout();
        tableLayout.invalidate();
        tableLayout.postInvalidate();
    }
}
