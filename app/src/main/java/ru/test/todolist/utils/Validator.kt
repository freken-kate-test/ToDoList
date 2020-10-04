package ru.test.todolist.utils

import android.util.Patterns
import android.widget.EditText
import ru.test.todolist.customViews.TDInputField
import java.util.regex.Pattern

// Default validation messages
private val EMAIL_VALIDATION_MSG = "This address does not exist"

private fun getText(data: Any): String {
    var str = ""
    if (data is EditText) {
        str = data.text.toString()
    } else if (data is String) {
        str = data
    }
    return str
}

fun isValidEmail(data: Any, updateUI: Boolean = true): Boolean {
    val str = getText(data)
    val valid = Patterns.EMAIL_ADDRESS.matcher(str).matches()

    if (updateUI) {
        val error: String? = if (valid) "" else EMAIL_VALIDATION_MSG
        setError(data, error)
    }

    return valid
}


//Пароль обязательно должен соответствовать следующим правилам:
// не менее 8 символов
// пробелы и символы переноса строки должны отсутствовать
// не менее 2х заглавных букв
// не менее 1 спец. символа (!, @, etc)
// не менее 1 цифры
fun isValidPassword(data: Any, updateUI: Boolean = true): Boolean {
    val str = getText(data)
    var valid = true
    var errorMessage = ""

    // не менее 8 символов
    if (str.length < 8) {
        valid = false
        errorMessage = "The password must be at least 8 characters"
    }

    // не менее 1 цифры
    var exp = ".*[0-9].*"
    var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
    var matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
        errorMessage = "The password must contain at least one digit"
    }

    // пробелы и символы переноса строки должны отсутствовать
    exp = "[\\s]"
    pattern = Pattern.compile(exp)
    matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
        errorMessage = "no spaces allowed"
    }

    //  не менее 2х заглавных букв
    exp = "[A-Z][^A-Z]*[A-Z]"
    pattern = Pattern.compile(exp)
    matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
        errorMessage = "The password must contain capital letters"
    }

    // не менее 1 спец. символа (!, @, etc)
    // разрешенные спец символы : "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
    exp = ".*[~!@#\$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?].*"
    pattern = Pattern.compile(exp)
    matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
        errorMessage = "The password must contain special char"
    }

    // Set error if required
    if (updateUI) {
        setError(data, errorMessage)
    }

    return valid
}

private fun setError(data: Any, error: String?) {
    if (data is EditText) {
        if (data.parent.parent is TDInputField) {
            (data.parent.parent as TDInputField).showError(error)
        } else {
            data.setError(error)
        }
    }
}

