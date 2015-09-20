package citu.teknoybuyandselladmin.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Batistil on 9/19/2015.
 */
public class SellApproval {

    private String itemName;
    private String request_date;

    public String getItemName() {
        return itemName;
    }

    public String getRequest_date() {
        return request_date;
    }

    public static SellApproval getRequest(JSONObject jsonObject){
        SellApproval sell = new SellApproval();
        JSONObject item;

        try {
            sell.request_date = jsonObject.getString("request_date");

            if(!jsonObject.isNull("item")){
                item = jsonObject.getJSONObject("item");
                sell.itemName = item.getString("name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sell;
    }

    public static ArrayList<SellApproval> allSellRequest(JSONArray jsonArray){
        ArrayList<SellApproval> reserved = new ArrayList<SellApproval>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject requestObject = null;
            try {
                requestObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            SellApproval sell = SellApproval.getRequest(requestObject);
            if (sell != null) {
                reserved.add(sell);
            }
        }
        return reserved;

    }
}
