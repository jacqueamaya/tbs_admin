package citu.teknoybuyandselladmin.models;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReservedItem {

    private static final String TAG = "ReservedIten";
    public static final String CATEGORY = "category";
    public static final String CATEGORY_NAME = "category_name";
    public static final String STATUS = "status";
    public static final String RESERVED_DATE = "reserved_date";
    public static final String RESERVE_EXPIRATION = "request_expiration";
    public static final String REQUEST_ID = "id";
    public static final String ITEM = "item";
    public static final String ITEM_ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String PICTURE = "picture";

    private String category;
    private String itemName;
    private String status;
    private String details;
    private String link;

    private int requestId;
    private int itemId;

    private long reservedDate;
    private long reserveExpiration;

    private float price;

    public String getItemName() {
        return itemName;
    }

    public String getStatus() {
        return status;
    }

    public long getReservedDate() {
        return reservedDate;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getDetails() {
        return details;
    }

    public float getPrice() {
        return price;
    }

    public String getLink() {
        return link;
    }

    public long getReserveExpiration() {
        return reserveExpiration;
    }

    public String getCategory() {
        return category;
    }

    public static ReservedItem asSingle(JSONObject jsonObject) {
        ReservedItem reservedItem = new ReservedItem();
        JSONObject item;
        try {
            item = jsonObject.getJSONObject(ITEM);

            reservedItem.itemId = item.getInt(ITEM_ID);
            reservedItem.category = item.getJSONObject(CATEGORY).getString(CATEGORY_NAME);
            reservedItem.status = jsonObject.getString(STATUS);
            reservedItem.reservedDate = jsonObject.getLong(RESERVED_DATE);
            reservedItem.requestId = jsonObject.getInt(REQUEST_ID);
            reservedItem.itemName = item.getString(NAME);
            reservedItem.details = item.getString(DESCRIPTION);
            reservedItem.price = (float) item.getDouble(PRICE);
            reservedItem.link = item.getString(PICTURE);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating a ReservedItem object from JSONObject", e);
        }

        return reservedItem;
    }

    public static ArrayList<ReservedItem> asList(JSONArray jsonArray) {
        final int length = jsonArray.length();
        ArrayList<ReservedItem> reserved = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            try {
                JSONObject reservedObject = jsonArray.getJSONObject(i);
                ReservedItem ri = ReservedItem.asSingle(reservedObject);
                reserved.add(ri);
            } catch (Exception e) {
                Log.e(TAG, "Error getting JSONObject at index#" + i, e);
            }
        }

        return reserved;
    }
}
