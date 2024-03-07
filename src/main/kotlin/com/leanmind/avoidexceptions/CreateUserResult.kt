package com.leanmind.avoidexceptions

sealed interface Error {
    object UserAlreadyExists: Error
    object TooManyAdmins: Error
    object CannotSaveUser: Error
}

class CreateUserResult private constructor(
        val error: Error?,
) {
    fun isSuccess(): Boolean {
        return error === null
    }

    companion object {
        fun success(): CreateUserResult {
            return CreateUserResult(null)
        }

        fun userAlreadyExistsError(): CreateUserResult {
            return CreateUserResult(Error.UserAlreadyExists)
        }

        fun tooManyAdminsError(): CreateUserResult {
            return CreateUserResult(Error.TooManyAdmins)
        }

        fun cannotSaveUser(): CreateUserResult {
            return CreateUserResult(Error.CannotSaveUser)
        }
    }
}
