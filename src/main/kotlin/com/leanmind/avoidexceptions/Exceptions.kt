package com.leanmind.avoidexceptions

class UserAlreadyExistsException : RuntimeException(
    "User already exists."
)

class EmptyDataNotAllowedException : RuntimeException(
    "User data is invalid."
)

class CannotCreateUserException(exception: Exception) : RuntimeException(
    "Cannot create user.",
    exception
)

class PasswordTooShortException : RuntimeException(
    "Password is too short."
)

class TooManyAdminsException : RuntimeException(
    "Too many admins."
)

class CannotPersistUserException(cause: Exception) : RuntimeException(
    "Cannot persist user.",
    cause
)
