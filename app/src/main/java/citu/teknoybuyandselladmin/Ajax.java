package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class Ajax {

    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
    public static final String REQUEST_METHOD_PUT = "PUT";
    public static final String REQUEST_METHOD_DELETE = "DELETE";

    public static final int CONNECT_TIMEOUT = 10000;
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    private static final String TAG = "Ajax";

    public static void get(String url, final Callbacks callbacks) {
        new AsyncTask<String, Void, HashMap<String, Object>>() {

            @Override
            protected HashMap<String, Object> doInBackground(String... params) {

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(params[0]);
                    Log.v(TAG,"URL: "+url);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setDoInput(true);
                    connection.setRequestMethod(REQUEST_METHOD_GET);
                    connection.setUseCaches(false);

                    String responseBody = readStream(connection.getInputStream());
                    Log.v("Ajax","response body: "+responseBody);
                    Log.v("Ajax","response code: "+connection.getResponseCode());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("statusCode", connection.getResponseCode());
                    map.put("responseBody", responseBody);
                    return map;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> map) {
                super.onPostExecute(map);
                if (null == map) {
                    callbacks.error(0, null, null);
                } else {
                    int statusCode = (Integer) map.get("statusCode");
                    String responseBody = (String) map.get("responseBody");

                    if (statusCode == 200) {
                        callbacks.success(responseBody);
                    } else {
                        callbacks.error(statusCode, responseBody, null);
                    }
                }
            }
        }.execute(url);
    }

    public static void post(String url, final ProgressDialog progressDialog, final Map<String, String> data, final Callbacks callbacks) {
        new AsyncTask<String, Void, HashMap<String, Object>>() {
            @Override
            protected  void onPreExecute(){
                progressDialog.show();
            }

            @Override
            protected HashMap<String, Object> doInBackground(String... params) {

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod(REQUEST_METHOD_POST);
                    connection.setUseCaches(false);

                    Log.v("Ajax", serialize(data));
                    Log.v("Ajax", "output stream" + connection.getOutputStream());

                    writeToStream(connection.getOutputStream(), serialize(data));
                    connection.connect();

                    String responseBody = readStream(connection.getInputStream());
                    Log.v("Ajax",responseBody);
                    Log.v("Ajax","response code:"+connection.getResponseCode());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("statusCode", connection.getResponseCode());
                    map.put("responseBody", responseBody);
                    return map;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> map) {
                progressDialog.dismiss();
                super.onPostExecute(map);
                if (null == map) {
                    callbacks.error(0, null, null);
                } else {
                    int statusCode = (Integer) map.get("statusCode");
                    String responseBody = (String) map.get("responseBody");

                    if (statusCode == 200 || statusCode == 201) {
                        callbacks.success(responseBody);
                    } else {
                        callbacks.error(statusCode, responseBody, null);
                    }
                }
            }
        }.execute(url);
    }

    /*public static void download(final Context context, final String url, final ImageView imageView) {
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected void doInBackground(String... params) {
                try {
                    Picasso.with(context)
                            .load(url)
                            .into(imageView);
                } catch (Exception e) {
                    Log.w("ImageDownloader", "Error downloading image from " + params[0]);
                }
            }
        }.execute(url);
    }*/

    public static void put(String url, Map<String, String> data, Callbacks callbacks) {

    }

    public static void delete(String url, Map<String, String> data, Callbacks callbacks) {

    }

    public static String serialize (Map<String, String> data) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (String key : data.keySet()) {
            if (first) {
                builder.append(key).append('=').append(data.get(key));
                first = false;
            } else {
                builder.append('&').append(key).append('=').append(data.get(key));
            }
        }

        return builder.toString();
    }

    public static String readStream (InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            is.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static void writeToStream (OutputStream os, String whatToWrite) {
        PrintWriter writer = new PrintWriter(os);
        try {
            writer.write(whatToWrite);
        } finally {
            writer.flush();
            writer.close();
        }
    }

    public static void writeToStream (OutputStream os) {
        PrintWriter writer = new PrintWriter(os);
            writer.flush();
            writer.close();
    }

    public interface Callbacks {
        void success(String responseBody);
        void error(int statusCode, String responseBody, String statusText);
    }

}
