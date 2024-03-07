package com.leanmind.avoidexceptions

data class UserDto(
        val username: String,
        val password: String,
        val role: UserRole,
)
