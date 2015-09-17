package citu.teknoybuyandselladmin;

import android.util.Log;

import java.util.Map;

public class Server {

    private static final String URL_LOGIN = "http://10.0.3.2:8000/api/admin_login";

    public static void login (Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey(LoginActivity.USERNAME) ||
                ! data.containsKey(LoginActivity.PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_LOGIN, data, callbacks);
    }

}
