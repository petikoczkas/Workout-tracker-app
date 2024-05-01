package hu.bme.aut.workout_tracker.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Locale
import java.util.regex.Pattern


private const val EMAIL_PATTERN =
    "^[_A-Za-z\\d-+]+(\\.[_A-Za-z\\d-]+)*@[A-Za-z\\d-]+(\\.[A-Za-z\\d]+)*(\\.[A-Za-z]{2,})$"
private const val PASS_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$"

fun String.isValidEmail(): Boolean =
    this.isNotBlank() && Pattern.compile(EMAIL_PATTERN).matcher(this).matches()

fun String.isValidPassword(): Boolean =
    this.isNotBlank() && Pattern.compile(PASS_PATTERN).matcher(this).matches()

fun String.passwordMatches(repeated: String): Boolean =
    this == repeated

fun String.removeEmptyLines(): String {
    if (this.isEmpty()) return this
    var tmp = this
    while (tmp[0].isWhitespace()) {
        tmp = tmp.drop(1)
    }
    while (tmp[tmp.length - 1].isWhitespace()) {
        tmp = tmp.dropLast(1)
    }
    return tmp
}

fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { word ->
        if (word[0] == '(') {
            word.replace(word[1], word[1].uppercaseChar())
        } else {
            word.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
        }
    }

fun InputStream?.getByteArray(width: Int, height: Int, quality: Int): ByteArray {

    var bitmap = BitmapFactory.decodeStream(this)

    bitmap = Bitmap.createScaledBitmap(bitmap, height, width, true)
    val matrix = Matrix()
    matrix.postRotate(90F)
    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    val byteArray = outputStream.toByteArray()
    outputStream.close()
    return byteArray
}