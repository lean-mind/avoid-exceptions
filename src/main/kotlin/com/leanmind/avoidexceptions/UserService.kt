package com.leanmind.avoidexceptions

import com.leanmind.avoidexceptions.UserRole.ADMIN
import org.springframework.stereotype.Service

private const val MAX_ADMIN_NUMBER = 2

@Service
class UserService(private val userRepository: UserRepository) {
    fun create(user: User) {
        try {
            checkIfMaxAdminNumberHasBeenReached(user)
            userRepository.save(user)
        } catch (exception: Exception) {
            throw CannotCreateUserException(exception)
        }
    }

    private fun checkIfMaxAdminNumberHasBeenReached(user: User) {
        user.role
            .takeIf { it == ADMIN }
            ?.let { userRepository.countOfAdmins() }
            ?.takeIf { it >= MAX_ADMIN_NUMBER }
            ?.let { throw TooManyAdminsException() }
    }
}
