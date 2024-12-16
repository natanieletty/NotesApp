package ua.nuop.bushniak.helper

fun maskEmail(email: String): String {
    val atIndex = email.indexOf('@')
    if (atIndex < 2) return email
    val login = email.substring(0, atIndex)
    val maskedLogin = "${login.first()}${"*".repeat(atIndex - 2)}${login.last()}"
    return "$maskedLogin${email.substring(atIndex)}"
}