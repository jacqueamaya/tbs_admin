package citu.teknoybuyandselladmin.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Category {

    private static final String TAG = "Category";

    public static final String CATEGORY_NAME = "category_name";

    public static String[] asArray(JSONArray jsonArray) {
        String[] categories = new String[jsonArray.length() + 1];
        categories[0] = "All";

        for (int i = 1; i < categories.length; i++) {
            try {
                JSONObject category = jsonArray.getJSONObject(i-1);
                categories[i] = category.getString(CATEGORY_NAME);
            } catch (JSONException e) {
                Log.e(TAG, "Exception while getting category name", e);
            }
        }

        return categories;
    }
}
