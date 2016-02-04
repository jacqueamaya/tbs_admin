package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import citu.teknoybuyandselladmin.services.LoginService;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    public static final String MY_PREFS_NAME = "MyPreferences";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private EditText mTxtUsername;
    private EditText mTxtPassword;

    private ProgressDialog mLoginProgress;

    private LoginBroadcastReceiver mReceiver;

    private String username;
    private String password;

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
        if (sp.getString(USERNAME, null) != null) {
            Intent intent;
            intent = new Intent(LoginActivity.this, NotificationsActivity.class);
            finish();
            startActivity(intent);
        }
        mReceiver = new LoginBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(LoginService.class.getCanonicalName()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void onLogin(View view) {
        username = mTxtUsername.getText().toString();
        password = mTxtPassword.getText().toString();

        Map<String, String> data = new HashMap<>();
        data.put(USERNAME, username);
        data.put(PASSWORD, password);

        Log.e(TAG, "Logging in to server");
        Log.e(TAG, "Username: " + username + " -- Password: " + password);

        if (username.isEmpty() || password.isEmpty() || "".equals(username.trim()) || "".equals(password.trim())) {
            Toast.makeText(LoginActivity.this, "Please input username and password", Toast.LENGTH_SHORT).show();
        } else {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                mLoginProgress.show();

                Intent intent = new Intent(this, LoginService.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startService(intent);
                Log.e(TAG, "starting service");
            } else {
                Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private class LoginBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra("result", 0);
            String statusText = intent.getStringExtra("response");

            Log.e(TAG,result+"");
            Log.e(TAG,statusText);

            if (result == 1) {
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("username", Utils.capitalize(username));
                editor.apply();

                startActivity(new Intent(LoginActivity.this, NotificationsActivity.class));
                finish();
            } else {
                mLoginProgress.hide();
                Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
