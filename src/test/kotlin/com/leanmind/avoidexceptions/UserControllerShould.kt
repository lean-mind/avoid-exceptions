package com.leanmind.avoidexceptions

import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
class UserControllerShould {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `respond is created when user has been created`() {
        val user = User.from("username", "password", UserRole.STANDARD)
        `when`(userService.create(user)).thenReturn(CreateUserResult.success())

        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content(buildRequestBodyFrom(user.username, user.password, user.role.toString()))
        ).andExpect(status().isCreated)
        verify(userService).create(user)
    }

    @Test
    fun `respond bad request when user data is empty`() {
        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content(buildRequestBodyFrom("", "", ""))
        ).andExpect(status().isBadRequest)
    }


    @Test
    fun `respond bad request when user password is too short`() {
        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content(buildRequestBodyFrom("username", "pwd", UserRole.STANDARD.toString()))
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `respond bad request when user already exists`() {
        val user = User.from("existingUser", "password", UserRole.STANDARD)
        `when`(userService.create(user)).thenThrow(UserAlreadyExistsException())

        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content(buildRequestBodyFrom(user.username, user.password, user.role.toString()))
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `respond bad request when max number of admins is reached`() {
        val user = User.from("username", "password", UserRole.ADMIN)
        `when`(userService.create(user)).thenThrow(TooManyAdminsException())

        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content(buildRequestBodyFrom(user.username, user.password, user.role.toString()))
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `respond internal server error when something fails creating the user`() {
        val user = User.from("username", "password", UserRole.STANDARD)
        `when`(userService.create(user)).thenThrow(CannotCreateUserException(RuntimeException()))

        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content(buildRequestBodyFrom(user.username, user.password, user.role.toString()))
        ).andExpect(status().isInternalServerError)
    }

    private fun buildRequestBodyFrom(
            username: String,
            password: String,
            role: String
    ) =
            """
            {
                "username": "$username",
                "password": "$password",
                "role": "$role"
            }
        """.trimIndent()
}
