package com.leanmind.avoidexceptions

import org.springframework.stereotype.Service

private const val MAX_NUMBER_OF_ADMINS = 2

@Service
class UserService(private val userRepository: UserRepository) {
    fun create(user: User): CreateUserResult {
        if (userRepository.exists(user)) {
            return CreateUserResult.userAlreadyExistsError()
        }
        if (user.isAdmin() && cannotExistsMoreAdmins()) {
            return CreateUserResult.tooManyAdminsError()
        }
        return userRepository.save(user)
    }

    private fun cannotExistsMoreAdmins() = userRepository.countOfAdmins() >= MAX_NUMBER_OF_ADMINS
}
