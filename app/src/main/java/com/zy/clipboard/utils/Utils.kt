package com.zy.clipboard.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipboardManager
import android.content.Context
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast

/**
 * Created by zy on 17-5-29.
 */

fun Activity.toast(msg: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, time).show()
}

fun Activity.progress(msg: String, title: String = ""): ProgressDialog {
    return ProgressDialog.show(this, title, msg)
}

fun Activity.permission(per: String) {
    ActivityCompat.requestPermissions(this, arrayOf(per), 0)
}

fun Activity.permission(pers: Array<String>) {
    ActivityCompat.requestPermissions(this, pers, 0)
}

class Utils {
    companion object {
        fun copyToClipboard(context: Context, msg: String) {
            var cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setText(msg);
        }
    }
}

class Logger {
    companion object {
        fun d(msg: String?) {
            Log.d("CLIPBOARD", msg)
        }
    }
}


