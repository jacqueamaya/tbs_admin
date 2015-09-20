package citu.teknoybuyandselladmin;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private static final String TAG = "LoginActivity";

    private EditText txtUsername;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
    }

    public void onLogin(View view){
        Log.v("LoginActivity","login function");
        Map<String,String> data = new HashMap<>();

        data.put(USERNAME,txtUsername.getText().toString());
        data.put(PASSWORD,txtPassword.getText().toString());
        Log.v("LoginActivity",txtUsername.getText().toString()+'&'+txtPassword.getText().toString());
        Server.login(data, new Ajax.Callbacks() {
            @Override
            public void success(String responseBody) {
                try {
                    JSONObject json = new JSONObject(responseBody);
                    if(json.getInt("status") == 200){
                        Log.v(TAG,"Successful Login");
                        Intent intent;
                        intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra("admin", "Admin");
                        startActivity(intent);
                    }
                    else{
                        Log.v(TAG,"Invalid username or password");
                        Toast.makeText(LoginActivity.this, "Error: Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                        e.printStackTrace();
                }
            }

            @Override
            public void error(int statusCode, String responseBody, String statusText) {
                Log.v("LoginActivity","Request error");
                Toast.makeText(LoginActivity.this, "Error: Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return id == R.id.action_settings ? true : super.onOptionsItemSelected(item);

    }
}
