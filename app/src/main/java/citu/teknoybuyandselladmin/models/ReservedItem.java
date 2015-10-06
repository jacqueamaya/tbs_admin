package citu.teknoybuyandselladmin.models;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReservedItem {
    private static final String TAG = "ReservedIten";
    private String itemName;
    private String status;
    private String reserved_date;
    private String details;
    private int requestId;
    private int itemId;
    private float price;
    private String link;


    public String getItemName() {
        return itemName;
    }

    public String getStatus() {
        return status;
    }

    public String getReserved_date() {
        return reserved_date;
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

    public static ReservedItem getReservedItems(JSONObject jsonObject){
        ReservedItem ri = new ReservedItem();
        JSONObject item;

        try {
            ri.status = jsonObject.getString("status");
            ri.reserved_date = jsonObject.getString("reserved_date");
            ri.requestId = jsonObject.getInt("id");

            if(!jsonObject.isNull("item")){
                item = jsonObject.getJSONObject("item");
                ri.itemName = item.getString("name");
                ri.itemId = item.getInt("id");
                ri.details = item.getString("description");
                ri.price = (float) item.getDouble("price");
                ri.link = item.getString("picture");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ri;
    }

    public static ArrayList<ReservedItem> allReservedItems(JSONArray jsonArray){
        ArrayList<ReservedItem> reserved = new ArrayList<ReservedItem>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject reservedObject = null;
            try {
                reservedObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            ReservedItem ri = ReservedItem.getReservedItems(reservedObject);
            if (ri != null) {
                reserved.add(ri);
            }
        }
        return reserved;

    }
}
