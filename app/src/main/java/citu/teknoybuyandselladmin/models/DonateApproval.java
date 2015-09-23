package citu.teknoybuyandselladmin.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Batistil on 9/20/2015.
 */
public class DonateApproval {
    private String itemName;
    private String request_date;
    private String details;

    private int requestId;
    private int itemId;

    private float price;

    public String getItemName() {
        return itemName;
    }

    public String getRequest_date() {
        return request_date;
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

    public static DonateApproval getRequest(JSONObject jsonObject){
        DonateApproval donate = new DonateApproval();
        JSONObject item;

        try {
            donate.request_date = jsonObject.getString("request_date");
            donate.requestId = jsonObject.getInt("id");

            if(!jsonObject.isNull("item")){
                item = jsonObject.getJSONObject("item");
                donate.itemName = item.getString("name");
                donate.itemId = item.getInt("id");
                donate.details = item.getString("description");
                donate.price = (float)item.getDouble("price");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return donate;
    }

    public static ArrayList<DonateApproval> allDonateRequest(JSONArray jsonArray){
        ArrayList<DonateApproval> donatedItems = new ArrayList<DonateApproval>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject requestObject = null;
            try {
                requestObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            DonateApproval donate = DonateApproval.getRequest(requestObject);
            if (donate != null) {
                donatedItems.add(donate);
            }
        }
        return donatedItems;

    }
}
