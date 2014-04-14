package com.av1rus.arduinoconnect.arduinolib.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.av1rus.arduinoconnect.arduinolib.R;

public class MyDialog {

    private static final String TAG = "ArduinoConnect Dialog";
    private static ProgressDialog mProgressDialog;

    /*
        Alert Dialog
     */
    public static Dialog notifyUserDialog(final Activity context, String message, final boolean shouldFinish){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (shouldFinish){
                            try {
                                context.finish();
                            } catch(Exception e){
                                Log.e(TAG, "Oops tried to finish activity that doesnt exist");
                            }
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /*
        Loading Progress Dialog
     */
    public static void showLoadingDialog(Context c, String title, String message){
        startLoadingProgress(c, title, message, false);
    }

    public static void showLoadingDialog(Context c, String message){
        startLoadingProgress(c, "", message, false);
    }

    public static void dismissLoadingDialog(){
        stopLoadingProgress();
    }

    private static void startLoadingProgress(Context context, String title, String message, boolean cancelable) {
        if (mProgressDialog != null) {
            stopLoadingProgress();
        }

//        mProgressDialog = new ProgressDialog(context, R.style.StyledDialog);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.show();
    }

    private static void stopLoadingProgress(){
        try {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        } catch(Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /*
            Notification
     */
    public static void showNotification(Context context, int id, String title, String ticker, String content){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, null, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(ticker))
                        .setContentText(content);

//        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify(id, mBuilder.build());

    }

    public static void removeNotification(Context context, int id){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

}
