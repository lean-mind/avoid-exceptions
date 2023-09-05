package com.leanmind.avoidexceptions

import org.springframework.stereotype.Service

@Service
class UserRepository {
    fun findByUsername(username: String): User? {
        TODO("Not yet implemented")
    }

    fun save(user: User) {
        TODO("Not yet implemented")
    }

    fun countOfAdmins(): Int {
        TODO("Not yet implemented")
    }
}
