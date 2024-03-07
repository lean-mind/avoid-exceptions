package com.leanmind.avoidexceptions

class UserAlreadyExistsException : RuntimeException()

class EmptyDataNotAllowedException : RuntimeException()

class CannotCreateUserException(exception: Exception) : RuntimeException(exception)

class PasswordTooShortException : RuntimeException()

class TooManyAdminsException : RuntimeException()

class CannotPersistUserException(cause: Exception) : RuntimeException(
        "Cannot persist user.",
        cause
)