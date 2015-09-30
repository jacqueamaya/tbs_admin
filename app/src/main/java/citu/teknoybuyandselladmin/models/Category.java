package citu.teknoybuyandselladmin.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Category {
    private String categoryName;
    private int categoryId;

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public static ArrayList<String> allCategories(JSONArray jsonArray){
        ArrayList<String> categories = new ArrayList<String>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject categObject = null;
            try {
                categObject = jsonArray.getJSONObject(i);
                categories.add(categObject.getString("category_name"));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return categories;

    }
}
