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
        val user = User("username", "password")

        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content("{\"username\":\"${user.username}\",\"password\":\"${user.password}\"}")
        ).andExpect(status().isCreated)
        verify(userService).create(user)
    }

    @Test
    fun `respond bad request when user data is empty`() {
        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content("{\"username\":\"\",\"password\":\"\"}")
        ).andExpect(status().isBadRequest)
    }


    @Test
    fun `respond bad request when user password is too short`() {
        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content("{\"username\":\"username\",\"password\":\"pwd\"}")
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `respond bad request when user already exists`() {
        val user = User("existingUser", "password")
        `when`(userService.create(user)).thenThrow(UserAlreadyExistsException())

        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content("{\"username\":\"${user.username}\",\"password\":\"${user.password}\"}")
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `respond bad request when max number of admins is reached`() {
        val user = User("username", "password", UserRole.ADMIN)
        `when`(userService.create(user)).thenThrow(TooManyAdminsException())

        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content("{\"username\":\"${user.username}\",\"password\":\"${user.password}\",\"role\":\"${user.role}\"}")
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `respond internal server error when something fails creating the user`() {
        val user = User("username", "password")
        `when`(userService.create(user)).thenThrow(CannotCreateUserException(RuntimeException()))

        mockMvc.perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content("{\"username\":\"${user.username}\",\"password\":\"${user.password}\"}")
        ).andExpect(status().isInternalServerError)
    }
}
