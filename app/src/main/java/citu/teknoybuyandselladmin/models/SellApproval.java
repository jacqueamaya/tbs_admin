package citu.teknoybuyandselladmin.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SellApproval {

    private static final String TAG = "SellApproval";

    public static final String REQUEST_DATE = "request_date";
    public static final String REQUEST_EXPIRATION = "request_expiration";
    public static final String REQUEST_ID = "id";
    public static final String ITEM = "item";
    public static final String ITEM_NAME = "name";
    public static final String ITEM_ID = "id";
    public static final String ITEM_PRICE = "price";
    public static final String ITEM_DESCRIPTION = "description";
    public static final String ITEM_PICTURE = "picture";

    private String itemName;
    private String requestDate;
    private String requestExpiration;
    private int itemId;
    private int requestId;
    private float price;
    private String details;
    private String link;


    public String getItemName() {
        return itemName;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRequestId() {
        return requestId;
    }

    public float getPrice() {
        return price;
    }

    public String getDetails() {
        return details;
    }

    public String getLink() {
        return link;
    }

    public String getRequestExpiration() {
        return requestExpiration;
    }

    public static SellApproval asSingle(JSONObject jsonObject) {
        SellApproval sell = new SellApproval();

        try {
            sell.requestDate = jsonObject.getString(REQUEST_DATE);
            sell.requestExpiration = jsonObject.getString(REQUEST_EXPIRATION);
            sell.requestId = jsonObject.getInt(REQUEST_ID);

            JSONObject item = jsonObject.getJSONObject(ITEM);
            sell.itemName = item.getString(ITEM_NAME);
            sell.itemId = item.getInt(ITEM_ID);
            sell.price = (float) item.getDouble(ITEM_PRICE);
            sell.details = item.getString(ITEM_DESCRIPTION);
            sell.link = item.getString(ITEM_PICTURE);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating SellApproval object from JSONObject", e);
        }

        return sell;
    }

    public static ArrayList<SellApproval> asList(JSONArray jsonArray) {
        int length = jsonArray.length();
        ArrayList<SellApproval> reserved = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            try {
                JSONObject requestObject = jsonArray.getJSONObject(i);
                SellApproval sell = SellApproval.asSingle(requestObject);
                reserved.add(sell);
            } catch (JSONException e) {
                Log.e(TAG, "Error getting JSONObject at index#" + i, e);
            }
        }

        return reserved;
    }
}
