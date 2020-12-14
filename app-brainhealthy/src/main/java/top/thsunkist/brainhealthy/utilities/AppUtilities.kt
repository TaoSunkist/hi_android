package top.thsunkist.brainhealthy.utilities

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import top.thsunkist.brainhealthy.BrainhealthyApplication
import java.util.concurrent.atomic.AtomicBoolean

val isAppInForeground: AtomicBoolean = AtomicBoolean(true)

fun View.dismissKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    if (imm != null) {
        requestFocus()
        imm.showSoftInput(this, 0)
    }
}

fun View.toggleSoftInput(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.toggleSoftInput(0, 0)
}

fun getAppChannel(context: Context): String {
    val appInfo =
        context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
    val appChannel = appInfo.metaData.getString("UMENG_CHANNEL")
    return appChannel ?: "Umeng"
}

fun toByteArray(parcelable: Parcelable): ByteArray {
    val parcel = Parcel.obtain()

    parcelable.writeToParcel(parcel, 0)

    val result = parcel.marshall()

    parcel.recycle()

    return result
}

fun <T> toParcelable(
    bytes: ByteArray,
    creator: Parcelable.Creator<T>,
): T {
    val parcel = Parcel.obtain()

    parcel.unmarshall(bytes, 0, bytes.size)
    parcel.setDataPosition(0)

    val result = creator.createFromParcel(parcel)

    parcel.recycle()

    return result
}

fun isMainProcess(): Boolean {
    val pid = android.os.Process.myPid()
    val activityManager = BrainhealthyApplication.CONTEXT.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (appProcess in activityManager.runningAppProcesses) {
        if (appProcess.pid == pid) {
            return BrainhealthyApplication.CONTEXT.applicationInfo.packageName == appProcess.processName
        }
    }
    return false
}

fun isAppOnForeground(context: Context): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager.runningAppProcesses ?: return false
    val packageName = context.packageName
    for (appProcess in appProcesses) {
        if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
            return true
        }
    }
    return false
}

fun AppCompatActivity.toAppSetting() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:$packageName")
    )
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}


fun System.microsecondTime(): Long {
    return System.nanoTime() / 1000
}