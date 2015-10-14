package citu.teknoybuyandselladmin.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Category {

    private static final String TAG = "Category";

    public static final String CATEGORY_NAME = "category_name";

    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static String[] asArray (JSONArray jsonArray) {
        String[] categories = new String[jsonArray.length()];

        for (int i = 0; i < categories.length; i++) {
            try {
                JSONObject category = jsonArray.getJSONObject(i);
                categories[i] = category.getString(CATEGORY_NAME);
            } catch (JSONException e) {
                Log.e(TAG, "Exception while getting category name", e);
            }
        }

        return categories;
    }
}
