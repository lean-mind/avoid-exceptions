package com.leanmind.avoidexceptions

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UserServiceShould {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userService = UserService(userRepository)
    }

    @Test
    fun `create user when it's valid`() {
        val user = User.from("username", "password")
        `when`(userRepository.findByUsername(user.username)).thenReturn(null)

        userService.create(user)

        verify(userRepository).save(user)
    }

    @Test
    fun `fails creating user if it already exists`() {
        val user = User.from("existingUser", "password")
        `when`(userRepository.findByUsername(user.username)).thenReturn(user)

        assertThrows<UserAlreadyExistsException> {
            userService.create(user)
        }
    }

    @Test
    fun `fails creating user if it cannot be saved`() {
        val user = User.from("username", "password")
        `when`(userRepository.findByUsername(user.username)).thenReturn(null)
        `when`(userRepository.save(user)).thenThrow(RuntimeException())

        assertThrows<CannotCreateUserException> {
            userService.create(user)
        }
    }

    @Test
    fun `fails creating user if maximum number of admin is reached`() {
        val user = User.from("username", "password", UserRole.ADMIN)
        `when`(userRepository.findByUsername(user.username)).thenReturn(null)
        `when`(userRepository.countOfAdmins()).thenReturn(2)

        assertThrows<TooManyAdminsException> {
            userService.create(user)
        }
    }
}
