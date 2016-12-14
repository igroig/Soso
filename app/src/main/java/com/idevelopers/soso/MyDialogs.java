//package com.idevelopers.soso;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//
///**
// * Created by Gio on 11/14/2016.
// */
//
//public class MyDialogs {
//    private static final String TAG = "MyDialogs";
//    public static AlertDialog getSimpleDialog(Context context, String message) {
//        AlertDialog builder = new AlertDialog.Builder(context, R.style.CustomDialogStyle).setMessage(message).setPositiveButton(R.string.ok, null).create();
//        return builder;
//    }
//    public static AlertDialog getSimpleDialog(Context context, String message, String positiveText, DialogInterface.OnClickListener clickPositiveListener,
//                                              String negativeButton, DialogInterface.OnClickListener clickNegativeListener) {
//        AlertDialog builder = new AlertDialog.Builder(context, R.style.CustomDialogStyle).setMessage(message).setPositiveButton(positiveText, clickPositiveListener)
//                .setNegativeButton(negativeButton, clickNegativeListener).create();
//        return builder;
//    }
//    public static AlertDialog getListDialog(Context context, String[] items, DialogInterface.OnClickListener clickListener) {
//        AlertDialog builder = new AlertDialog.Builder(context).setSingleChoiceItems(items, -1, clickListener)
////                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
////
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                Log.e(TAG, "onClick: " + which );
////            }
////        })
//                .create();
//        return builder;
//    }
//
//}