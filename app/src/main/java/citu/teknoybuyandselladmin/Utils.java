package citu.teknoybuyandselladmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public final class Utils {

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

    public static String parseDate(long unix){
        Date date = new Date(unix*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d 'at' h:m:s a"); // the format of the date
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Manila")); // timezone reference for formating
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
    public static String parseToDateOnly(long unix){
        Date date = new Date(unix*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the format of the date
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Manila")); // timezone reference for formating
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

}
