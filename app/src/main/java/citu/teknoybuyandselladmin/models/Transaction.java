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

/**
 * Created by Batistil on 9/20/2015.
 */
public class Transaction {
    private static final String TAG = "Transaction";
    private String transactionId;
    private String buyer;
    private String seller;
    private String date;
    private String itemName;

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

    public static Transaction getTransaction(JSONObject jsonObject){
        Transaction transaction = new Transaction();
        JSONObject itemObj,buyerObj,sellerObj,studentObj;
        DateFormat df=null;
        Date date=null;

        try {
            try {
                df = new SimpleDateFormat("yyyy-MM-dd");
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(jsonObject.getString("date_claimed"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transaction.date = df.format(date);
            transaction.transactionId = jsonObject.getString("id");

            if(!jsonObject.isNull("item")){
                itemObj = jsonObject.getJSONObject("item");
                transaction.itemName = itemObj.getString("name");
            }
            if(!jsonObject.isNull("buyer")){
                buyerObj = jsonObject.getJSONObject("buyer");
                if(!buyerObj.isNull("student")){
                    studentObj = buyerObj.getJSONObject("student");
                    transaction.buyer = studentObj.getString("first_name")+" "+studentObj.getString("last_name");
                }
            }
            if(!jsonObject.isNull("seller")){
                sellerObj = jsonObject.getJSONObject("seller");
                if(!sellerObj.isNull("student")){
                    studentObj = sellerObj.getJSONObject("student");
                    transaction.seller = studentObj.getString("first_name")+" "+studentObj.getString("last_name");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public static ArrayList<Transaction> allTransactions(JSONArray jsonArray){
        ArrayList<Transaction> transactions = new ArrayList<Transaction>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject transObject = null;
            try {
                transObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Transaction transaction = Transaction.getTransaction(transObject);
            if (transaction != null) {
                transactions.add(transaction);
            }
        }
        return transactions;

    }
}
