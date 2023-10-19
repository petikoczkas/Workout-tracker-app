package hu.bme.aut.workout_tracker.utils

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