package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    public static final String MY_PREFS_NAME = "MyPreferences";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private EditText mTxtUsername;
    private EditText mTxtPassword;

    private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTxtUsername = (EditText) findViewById(R.id.txtUsername);
        mTxtPassword = (EditText) findViewById(R.id.txtPassword);

        mLoginProgress = new ProgressDialog(this);
        mLoginProgress.setIndeterminate(true);
        mLoginProgress.setMessage("Please wait. . .");

        SharedPreferences sp = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if(sp.getString(USERNAME, null) != null) {
            Intent intent;
            intent = new Intent(LoginActivity.this, NotificationsActivity.class);
            finish();
            startActivity(intent);
        }
    }

    public void onLogin(View view) {
        final String username = mTxtUsername.getText().toString();
        String password = mTxtPassword.getText().toString();

        Map<String, String> data = new HashMap<>();
        data.put(USERNAME, username);
        data.put(PASSWORD, password);

        Log.v(TAG, "Logging in to server");
        Log.v(TAG, "Username: " + username + " -- Password: " + password);

        Server.login(data, mLoginProgress, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if (json.getInt("status") == 200) {
                        Log.v(TAG, "Successful Login");
                        Log.d(TAG, "Response: " + json.toString());

                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("username", Utils.capitalize(username));
                        editor.apply();

                        startActivity(new Intent(LoginActivity.this, NotificationsActivity.class));
                        finish();
                    } else {
                        Log.v(TAG, "Invalid username or password");
                        Toast.makeText(LoginActivity.this, "Error: Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v("LoginActivity", "Request error");
                Toast.makeText(LoginActivity.this, "Error: Cannot connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
