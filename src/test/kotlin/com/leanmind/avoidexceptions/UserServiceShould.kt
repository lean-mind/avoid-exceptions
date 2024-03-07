package com.leanmind.avoidexceptions

import org.assertj.core.api.Assertions.assertThat
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
        val user = User.from("username", "password", UserRole.STANDARD)
        `when`(userRepository.exists(User.from("username", "password", UserRole.STANDARD))).thenReturn(false)

        val createUserResult = userService.create(user)

        verify(userRepository).save(user)
        assertThat(createUserResult.isSuccess()).isTrue()
    }

    @Test
    fun `fails creating user if it already exists`() {
        val user = User.from("existingUser", "password", UserRole.STANDARD)
        `when`(userRepository.exists(User.from("existingUser", "password", UserRole.STANDARD))).thenReturn(true)

        val createUserResult = userService.create(user)

        assertThat(createUserResult.isSuccess()).isFalse()
        assertThat(createUserResult.error).isInstanceOf(UserAlreadyExistsError::class.java)
    }

    @Test
    fun `fails creating user if it cannot be saved`() {
        val user = User.from("username", "password", UserRole.STANDARD)
        `when`(userRepository.exists(User.from("username", "password", UserRole.STANDARD))).thenReturn(false)
        `when`(userRepository.save(user)).thenThrow(RuntimeException())

        assertThrows<CannotCreateUserException> {
            userService.create(user)
        }
    }

    @Test
    fun `fails creating user if maximum number of admin is reached`() {
        val user = User.from("username", "password", UserRole.ADMIN)
        `when`(userRepository.exists(User.from("username", "password", UserRole.ADMIN))).thenReturn(false)
        `when`(userRepository.countOfAdmins()).thenReturn(2)

        val createUserResult = userService.create(user)

        assertThat(createUserResult.isSuccess()).isFalse()
        assertThat(createUserResult.error).isInstanceOf(TooManyAdminsError::class.java)
    }
}
