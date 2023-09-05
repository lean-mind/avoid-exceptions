package com.leanmind.avoidexceptions

import com.leanmind.avoidexceptions.UserRole.ADMIN
import org.springframework.stereotype.Service
import java.util.logging.Level
import java.util.logging.Logger

@Service
class UserService(private val userRepository: UserRepository) {
    val logger: Logger = Logger.getLogger(UserService::class.java.name)

    fun create(user: User) {
        try {
            if (userRepository.findByUsername(user.username) != null) {
                throw UserAlreadyExistsException()
            }
            if (user.role == ADMIN) {
                userRepository.countOfAdmins().let { admins ->
                    if (admins >= 2) {
                        throw TooManyAdminsException()
                    }
                }
            }
            userRepository.save(user)
            logger.log(Level.INFO, "User created.")
        } catch (exception: UserAlreadyExistsException) {
            logger.log(Level.WARNING, "User already exists.", exception)
            throw exception
        } catch (exception: TooManyAdminsException) {
            logger.log(Level.WARNING, "Too many admins.", exception)
            throw exception
        } catch (exception: Exception) {
            logger.log(Level.SEVERE, "Cannot create user.", exception)
            throw CannotCreateUserException(exception)
        }
    }
}
