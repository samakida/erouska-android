package cz.covid19cz.app.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

fun Intent.wrapAsService(context: Context) = PendingIntent.getService(context, 0, this, 0)

fun Intent.wrapAsForegroundService(context: Context): PendingIntent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        PendingIntent.getForegroundService(context, 0, this, 0)
    } else {
        wrapAsService(context)
    }
}

fun Intent.wrapAsActivity(context: Context) = PendingIntent.getActivity(context, 0, this, 0)