package com.leanmind.avoidexceptions

data class User private constructor(
    var username: String,
    var password: String,
    var role: UserRole = UserRole.STANDARD
) {
    fun isAdmin(): Boolean = role == UserRole.ADMIN

    companion object {
        fun from(username: String, password: String, role: UserRole = UserRole.STANDARD): User {
            if (username.isEmpty() || password.isEmpty()) {
                throw EmptyDataNotAllowedException()
            }
            if (password.length < 8) {
                throw PasswordTooShortException()
            }

            return User(username, password, role)
        }
    }
}

enum class UserRole {
    ADMIN, STANDARD
}
