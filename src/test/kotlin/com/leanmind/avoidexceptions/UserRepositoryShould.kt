package com.leanmind.avoidexceptions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserRepositoryShould {

    @Test
    fun `persist user`() {
        val userRepository = UserRepository()
        val user = User.from("username", "password", UserRole.STANDARD)

        userRepository.save(user)

        assertEquals(user, userRepository.findByUsername("username"))
    }

    @Test
    fun `fails when user already exists`() {
        val userRepository = UserRepository()
        val user = User.from("username", "password", UserRole.STANDARD)
        userRepository.save(user)

        assertThrows<CannotPersistUserException> {
            userRepository.save(user)
        }.let {
            assert(it.cause is UserAlreadyExistsException)
        }
    }
}