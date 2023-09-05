package com.leanmind.avoidexceptions

data class User(
    var username: String,
    var password: String,
    var role: UserRole = UserRole.STANDARD
) {
    init {
        if (username.isEmpty() || password.isEmpty()) {
            throw EmptyDataNotAllowedException()
        }
        if (password.length < 8) {
            throw PasswordTooShortException()
        }
    }
}

enum class UserRole {
    ADMIN, STANDARD
}
