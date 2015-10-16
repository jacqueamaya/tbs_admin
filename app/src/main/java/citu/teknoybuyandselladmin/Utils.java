package citu.teknoybuyandselladmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class Utils {

    public static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat READABLE_DATE_FORMAT = new SimpleDateFormat("E, y-M-d 'at' h:m:s a");
    public static final DateFormat FORMATTED_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static String capitalize (String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    public static void alert(Context context, String title, String message, final Callbacks callbacks) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callbacks.ok();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    public interface Callbacks {
        void ok();
    }

}
