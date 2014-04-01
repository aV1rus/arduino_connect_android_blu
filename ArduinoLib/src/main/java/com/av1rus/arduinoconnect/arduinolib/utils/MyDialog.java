package com.av1rus.arduinoconnect.arduinolib.utils;

import android.app.ProgressDialog;

public class MyDialog {

    private static final String TAG = "ArduinoConnect Dialog";
    private static ProgressDialog mProgressDialog;

    /*
        Alert Dialog
     */
//    public static Dialog notifyUserDialog(final Activity context, String message, final boolean shouldFinish){
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage(message)
//                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                        if (shouldFinish){
//                            try {
//                                context.finish();
//                            } catch(Exception e){
//                                Log.e(TAG, "Oops tried to finish activity that doesnt exist");
//                            }
//                        }
//                    }
//                });
//        // Create the AlertDialog object and return it
//        return builder.create();
//    }
//
//    /*
//        Loading Progress Dialog
//     */
//    public static void showLoadingDialog(Context c, String title, String message){
//        startLoadingProgress(c, title, message, false);
//    }
//
//    public static void showLoadingDialog(Context c, String message){
//        startLoadingProgress(c, "", message, false);
//    }
//
//    public static void dismissLoadingDialog(){
//        stopLoadingProgress();
//    }
//
//    private static void startLoadingProgress(Context context, String title, String message, boolean cancelable) {
//        if (mProgressDialog != null) {
//            stopLoadingProgress();
//        }
//
//        mProgressDialog = new ProgressDialog(context, R.style.StyledDialog);
//        mProgressDialog.setMessage(message);
//        mProgressDialog.setCancelable(cancelable);
//        mProgressDialog.show();
//    }
//
//    private static void stopLoadingProgress(){
//        try {
//            mProgressDialog.dismiss();
//            mProgressDialog = null;
//        } catch(Exception e) {
//            Log.e(TAG, e.toString());
//        }
//    }

}
