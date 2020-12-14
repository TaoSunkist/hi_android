package top.thsunkist.brainhealthy.utilities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import io.reactivex.Observable
import io.reactivex.Single

interface PermissionGranter {
    fun requestPermission(permission: Array<String>, resultHandler: (Array<String>, IntArray) -> Unit)
}

class SystemPermissionDenied(val permission: String) : SecurityException()
class SystemPermissionsDenied(val permissions: Array<String>) : SecurityException()

object PermissionRequester {

    private fun get(context: Context, permission: String): Single<Boolean> {
        return Single.create { subscriber ->
            val granter = context as PermissionGranter
            val granted = ContextCompat.checkSelfPermission(context, permission)
            if (granted != PackageManager.PERMISSION_GRANTED) {
                granter.requestPermission(arrayOf(permission)) { _, result ->
                    if (result.first() == PackageManager.PERMISSION_GRANTED) {
                        subscriber.onSuccess(true)
                    } else {
                        subscriber.onError(SystemPermissionDenied(permission))
                    }
                }
            } else {
                subscriber.onSuccess(true)
            }
        }
    }

    fun get(context: Context, permissions: Array<String>): Observable<Pair<Array<String>, IntArray>> =
        Observable.create<Pair<Array<String>, IntArray>> { subscriber ->
            val granter = context as PermissionGranter
            printf("taohui get1")
            granter.requestPermission(permissions) { permissions, results ->
                printf("taohui get2, ${permissions.size}, ${results.size}")
                subscriber.onNext(Pair(permissions, results))
            }
        }

    fun getContact(c: AppCompatActivity): Single<Boolean> {
        return get(c, Manifest.permission.READ_CONTACTS)
    }

    fun getCamera(c: AppCompatActivity): Single<Boolean> {
        return get(c, Manifest.permission.CAMERA)
    }

    fun getRecordAudio(c: AppCompatActivity): Single<Boolean> {
        return get(c, Manifest.permission.RECORD_AUDIO)
    }

    fun getLocation(c: AppCompatActivity): Observable<Pair<Array<String>, IntArray>> {
        return get(c, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getAccessNotificationPolicy(c: AppCompatActivity): Observable<Pair<Array<String>, IntArray>> {
        return get(c, arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY))
    }


    fun getReadExternalStorage(c: AppCompatActivity): Single<Boolean> {
        return get(c, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun getWriteExternalStorage(c: AppCompatActivity): Single<Boolean> {
        return get(c, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    fun launchAppDetailsSettings(context: Context) {
        val applicationContext = context.applicationContext
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + applicationContext.packageName)
        if (!isIntentAvailable(context, intent)) return
        applicationContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    private fun isIntentAvailable(context: Context, intent: Intent): Boolean {
        return context.applicationContext
            .packageManager
            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            .size > 0
    }
}