package citu.teknoybuyandselladmin.ListAdapters;

import android.app.ActionBar;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.R;
import citu.teknoybuyandselladmin.models.Transaction;

/**
 * Created by Batistil on 9/20/2015.
 */
public class TransactionAdapter {

    private Context context;
    private ArrayList<Transaction> items;

    public TransactionAdapter(Context c,ArrayList<Transaction> list){
        context = c;
        items = list;
    }

    public void addRows(TableLayout table_layout){
        for(int i=0;i<items.size();i++){
            TableRow row = new TableRow(context);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.border);

            TextView id = new TextView(context);
            id.setText(items.get(i).getTransactionId());
            id.setGravity(Gravity.CENTER_HORIZONTAL);
            id.setPadding(5,5,5,5);
            id.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.1f));
            row.addView(id);

            TextView seller = new TextView(context);
            seller.setText(items.get(i).getSeller());
            seller.setGravity(Gravity.CENTER_HORIZONTAL);
            id.setPadding(5,5,5,5);
            seller.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.3f));
            row.addView(seller);

            TextView buyer = new TextView(context);
            buyer.setText(items.get(i).getBuyer());
            buyer.setGravity(Gravity.CENTER_HORIZONTAL);
            buyer.setPadding(5, 5, 5, 5);
            buyer.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.3f));
            row.addView(buyer);

            TextView date = new TextView(context);
            date.setText(items.get(i).getDate());
            date.setGravity(Gravity.CENTER_HORIZONTAL);
            date.setPadding(5,5,5,5);
            date.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.3f));
            row.addView(date);
            table_layout.addView(row);
        }
        table_layout.requestLayout();
        table_layout.invalidate();
        table_layout.postInvalidate();
    }
}
