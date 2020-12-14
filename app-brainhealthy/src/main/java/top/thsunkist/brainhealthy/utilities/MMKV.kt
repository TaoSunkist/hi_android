package top.thsunkist.brainhealthy.utilities

import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKV.mmkvWithID

/* MMKV storage level, default USER, if level of common, when user went to logout, couldn't clear it */
enum class MMKVID {
    COMMON, USER
}

fun MMKV.getBoolean(mmkvID: MMKVID = MMKVID.USER, key: String, defValue: Boolean): Boolean {
    return when (mmkvID) {
        MMKVID.COMMON -> mmkvWithID(mmkvID.name).getBoolean(key, defValue)
        else -> this.getBoolean(key, defValue)
    }
}

fun MMKV.putBoolean(mmkvID: MMKVID = MMKVID.USER, key: String, defValue: Boolean) {
    when (mmkvID) {
        MMKVID.COMMON -> mmkvWithID(mmkvID.name).putBoolean(key, defValue)
        else -> this.putBoolean(key, defValue)
    }
}

fun MMKV.getString(mmkvID: MMKVID = MMKVID.USER, key: String, defValue: String): String {
    return when (mmkvID) {
        MMKVID.COMMON -> mmkvWithID(mmkvID.name).getString(key, defValue)!!
        else -> this.getString(key, defValue)!!
    }
}

fun MMKV.putString(mmkvID: MMKVID = MMKVID.USER, key: String, defValue: String) {
    when (mmkvID) {
        MMKVID.COMMON -> mmkvWithID(mmkvID.name).putString(key, defValue)
        else -> this.putString(key, defValue)
    }
}