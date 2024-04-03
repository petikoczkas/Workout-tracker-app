package hu.bme.aut.workout_tracker.data.util

import android.util.Base64
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ByteArrayAdapter {
    @FromJson
    fun fromJson(photoString: String?): ByteArray? {
        return photoString?.let {
            if (it.isEmpty()) {
                byteArrayOf()
            } else {
                Base64.decode(it, Base64.DEFAULT)
            }
        }
    }

    @ToJson
    fun toJson(photoByteArray: ByteArray?): String {
        return photoByteArray?.let {
            if (it.isEmpty()) {
                ""
            } else {
                Base64.encodeToString(it, Base64.DEFAULT)
            }
        } ?: ""
    }
}