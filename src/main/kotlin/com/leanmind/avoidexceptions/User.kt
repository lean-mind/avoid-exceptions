package com.leanmind.avoidexceptions

data class User private constructor(
    var username: String,
    var password: String,
    var role: UserRole
) {
    companion object {
        fun from(username: String, password: String, role: UserRole): User {
            if (username.isBlank() || password.isBlank()) {
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
