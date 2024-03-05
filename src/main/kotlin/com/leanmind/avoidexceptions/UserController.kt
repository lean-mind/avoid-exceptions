package com.leanmind.avoidexceptions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping
    @ResponseStatus(CREATED)
    fun createUser(@RequestBody userDto: UserDto) = userService.create(userDto.toDomain())
}

data class UserDto(val username: String, val password: String, val role: String) {
    fun toDomain() = User.from(username, password, UserRole.valueOf(role))
}