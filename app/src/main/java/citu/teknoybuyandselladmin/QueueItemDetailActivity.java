package citu.teknoybuyandselladmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.SellApproval;


public class QueueItemDetailActivity extends BaseActivity {

    private static final String TAG = "QueueItemDetailActivity";
    private static final String REQUEST_ID = "request_id";
    private static final String ITEM_ID = "item_id";
    public static final String CATEGORY_ITEM = "category";

    private int requestId;
    private int itemId;
    private String mItemName;

    private TextView txtTitle;
    private TextView txtPrice;
    private TextView txtDetails;
    private TextView txtCategory;

    private ImageView thumbnail;

    private ProgressDialog queueProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_on_queue_detail);
        setupUI();

        Intent intent = getIntent();
        requestId = intent.getIntExtra("requestId",0);
        itemId = intent.getIntExtra("itemId",0);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtDetails = (TextView) findViewById(R.id.txtDetails);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        thumbnail = (ImageView) findViewById(R.id.imgThumbnail);

        queueProgress = new ProgressDialog(this);

        getQueueItemDetails(requestId);
    }

    public void getQueueItemDetails(int request){
        Map<String,String> data = new HashMap<>();
        data.put(REQUEST_ID, request + "");

        Server.getQueueItemDetails(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                ArrayList<SellApproval> request = new ArrayList<SellApproval>();
                SellApproval sell;
                Log.v(TAG, responseBody);
                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(responseBody);
                    request = SellApproval.asList(jsonArray);
                    sell = request.get(0);

                    Picasso.with(QueueItemDetailActivity.this)
                            .load(sell.getLink())
                            .into(thumbnail);

                    mItemName = sell.getItemName();
                    setTitle(mItemName);

                    txtTitle.setText(mItemName);
                    txtPrice.setText("Price: PHP " + sell.getPrice());
                    txtDetails.setText(sell.getDetails());

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Utils.alert(QueueItemDetailActivity.this,"Connection Error!");
            }
        });
    }

    public void addCategory(View view){
        LayoutInflater li = LayoutInflater.from(this);
        View addCategPrompt = li.inflate(R.layout.activity_add_category, null);
        AlertDialog.Builder alertDialogBuilder =  new AlertDialog.Builder(this);
        alertDialogBuilder.setView(addCategPrompt);

        final EditText category = (EditText) addCategPrompt.findViewById(R.id.txtCategoryAdded);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id){
                                Log.v(TAG,category.getText().toString());
                                Map<String,String> data = new HashMap<>();
                                data.put(CATEGORY_ITEM, category.getText().toString());

                                queueProgress.setIndeterminate(true);
                                queueProgress.setMessage("Please wait. . .");

                                Server.addCategory(data, queueProgress, new Ajax.Callbacks() {
                                    @Override
                                    public void success(String responseBody) {
                                        try {
                                            JSONObject json = new JSONObject(responseBody);
                                            if (json.getInt("status") == 200) {
                                                Log.v(TAG, "Category Added Successfully");
                                                Snackbar.make(findViewById(R.id.appbar), "Category successfully added", Snackbar.LENGTH_SHORT).show();
                                            } else {
                                                Log.v(TAG, "Failed to add activity_category");
                                                Snackbar.make(findViewById(R.id.appbar), "Failed to add category", Snackbar.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void error(int statusCode, String responseBody, String statusText) {
                                        Log.v(TAG, "Request error");
                                        Snackbar.make(findViewById(R.id.appbar), "Connection Error: Failed to add category", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog =  alertDialogBuilder.create();
        alertDialog.show();
    }

    public void onApprove(View view){
        Log.v(TAG, "Item REQUEST_ID: " + itemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID,this.itemId+"");
        data.put(REQUEST_ID, this.requestId + "");
        data.put(CATEGORY_ITEM, txtCategory.getText().toString());

        Log.v(TAG, "Item REQUEST_ID: " + this.itemId);
        Log.v(TAG, "Request REQUEST_ID: " + this.requestId);

        queueProgress.setIndeterminate(true);
        queueProgress.setMessage("Please wait. . .");

        Server.approveQueuedItem(data, queueProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Successful Approval");
                        Toast.makeText(QueueItemDetailActivity.this, "Item successfully approved", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.v(TAG, "approval failed");
                        Toast.makeText(QueueItemDetailActivity.this, "Error; Item approval failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(QueueItemDetailActivity.this, " Connection Error: Item approval failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDeny(View view){
        Log.v(TAG, "Item REQUEST_ID: " + itemId);
        Map<String,String> data = new HashMap<>();

        data.put(ITEM_ID,this.itemId+"");
        data.put(REQUEST_ID, this.requestId + "");

        queueProgress.setIndeterminate(true);
        queueProgress.setMessage("Please wait. . .");

        Server.denyQueuedItem(data, queueProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Successful Disapproval");
                        Toast.makeText(QueueItemDetailActivity.this, "Item successfully disapproved", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.v(TAG, "Disapproval failed");
                        Toast.makeText(QueueItemDetailActivity.this, "Error; Item disapproval failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v(TAG, "Request error");
                Toast.makeText(QueueItemDetailActivity.this, "Error: Item disapproval failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items_on_queue_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean checkItemClicked(MenuItem menuItem) {
        return menuItem.getItemId() != R.id.nav_items_queue;
    }

    public void onSelect(View view) {
        Server.getCategories(new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    final String categories[] = Category.asArray(new JSONArray(responseBody));
                    new AlertDialog.Builder(QueueItemDetailActivity.this)
                            .setTitle("Categories")
                            .setItems(categories, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    txtCategory.setText(categories[which]);
                                }
                            })
                            .create()
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.e(TAG, "Error: Cannot connect to server");
            }
        });



    }
}
