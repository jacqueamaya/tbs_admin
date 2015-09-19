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

    public String getItemName() {
        return itemName;
    }

    public String getStatus() {
        return status;
    }

    public String getReserved_date() {
        return reserved_date;
    }

    public static ReservedItem getReservedItems(JSONObject jsonObject){
        ReservedItem ri = new ReservedItem();
        JSONObject item;

        try {
            ri.status = jsonObject.getString("status");
            ri.reserved_date = jsonObject.getString("reserved_date");

            if(!jsonObject.isNull("item")){
                item = jsonObject.getJSONObject("item");
                ri.itemName = item.getString("name");
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
