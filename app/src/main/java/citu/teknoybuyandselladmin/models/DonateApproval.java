package citu.teknoybuyandselladmin.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Batistil on 9/20/2015.
 */
public class DonateApproval {

    private static final String TAG = "DonateApproval";

    public static final String REQUEST_DATE = "request_date";
    public static final String REQUEST_EXPIRATION = "request_expiration";
    public static final String REQUEST_ID = "id";
    public static final String ITEM = "item";
    public static final String ITEM_ID = "id";
    public static final String ITEM_NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PICTURE = "picture";
    public static final String CATEGORY = "category";

    private String itemName;
    private String itemCategory;
    private String details;
    private String link;

    private int requestId;
    private int itemId;

    private long requestDate;
    private long requestExpiration;

    public String getItemName() {
        return itemName;
    }

    public long getRequestDate() {
        return requestDate;
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

    public String getLink() {
        return link;
    }

    public long getRequestExpiration() {
        return requestExpiration;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public static DonateApproval asSingle(JSONObject jsonObject) {
        DonateApproval donateApproval = new DonateApproval();

        try {
            donateApproval.requestDate = jsonObject.getLong(REQUEST_DATE);
            donateApproval.requestId = jsonObject.getInt(REQUEST_ID);
            donateApproval.requestExpiration = jsonObject.getLong(REQUEST_EXPIRATION);

            JSONObject item = jsonObject.getJSONObject(ITEM);
            donateApproval.itemName = item.getString(ITEM_NAME);
            donateApproval.itemId = item.getInt(ITEM_ID);
            donateApproval.details = item.getString(DESCRIPTION);
            donateApproval.link = item.getString(PICTURE);
            donateApproval.itemCategory = item.optString(CATEGORY);

        } catch (JSONException e) {
            Log.e(TAG, "Error extracting data from JSON", e);
        }

        return donateApproval;
    }

    public static ArrayList<DonateApproval> asList(JSONArray jsonArray) {
        ArrayList<DonateApproval> donatedItems = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject requestObject = jsonArray.getJSONObject(i);
                DonateApproval donate = DonateApproval.asSingle(requestObject);
                donatedItems.add(donate);
            } catch (JSONException e) {
                Log.e(TAG, "Error getting JSONObject at index#" + i, e);
            }
        }

        return donatedItems;
    }
}
