package citu.teknoybuyandselladmin.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import citu.teknoybuyandselladmin.Utils;

public class Transaction {

    private static final String TAG = "Transaction";

    public static final String DATE_CLAIMED = "date_claimed";
    public static final String ID = "id";
    public static final String ITEM = "item";
    public static final String ITEM_NAME = "name";
    public static final String BUYER = "buyer";
    public static final String STUDENT = "student";
    public static final String STUDENT_FIRST_NAME = "first_name";
    public static final String STUDENT_LAST_NAME = "last_name";
    public static final String STUDENT_ID_NUMBER = "id_number";
    public static final String SELLER = "seller";

    private String transactionId;
    private String buyer;
    private String seller;
    private String itemName;
    private String studentId;
    private String date;

    public String getTransactionId() {
        return transactionId;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getSeller() {
        return seller;
    }

    public String getDate() {
        return date;
    }

    public String getItemName() {
        return itemName;
    }

    public String getStudentId() {
        return studentId;
    }

    public static Transaction asSingle(JSONObject jsonObject) {
        Transaction transaction = new Transaction();
        JSONObject itemObj, buyerObj, sellerObj, studentObj;

        try {
            transaction.date = Utils.parseToDateOnly(jsonObject.getLong(DATE_CLAIMED));
            transaction.transactionId = jsonObject.getString(ID);

            itemObj = jsonObject.getJSONObject(ITEM);
            transaction.itemName = itemObj.getString(ITEM_NAME);

            buyerObj = jsonObject.getJSONObject(BUYER);

            studentObj = buyerObj.getJSONObject(STUDENT);
            transaction.buyer = studentObj.getString(STUDENT_ID_NUMBER);

            sellerObj = jsonObject.getJSONObject(SELLER);

            studentObj = sellerObj.getJSONObject(STUDENT);
            transaction.seller = studentObj.getString(STUDENT_ID_NUMBER);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating Transaction object from JSONObject", e);
        }

        return transaction;
    }

    public static ArrayList<Transaction> asList(JSONArray jsonArray) {
        int length = jsonArray.length();
        ArrayList<Transaction> transactions = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            try {
                JSONObject transObject = jsonArray.getJSONObject(i);
                Transaction transaction = Transaction.asSingle(transObject);
                transactions.add(transaction);
            } catch (JSONException e) {
                Log.e(TAG, "Error getting JSONObject at index#" + i, e);
            }
        }

        return transactions;
    }
}
