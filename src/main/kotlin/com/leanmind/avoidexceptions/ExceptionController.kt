package com.leanmind.avoidexceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception

@ControllerAdvice
class ExceptionController {
    @ExceptionHandler(
        UserAlreadyExistsException::class,
        EmptyDataNotAllowedException::class,
        PasswordTooShortException::class,
        TooManyAdminsException::class
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleBadRequest(exception: Exception): ErrorResponse {
        return ErrorResponse("Bad request: ${exception.message}")
    }

    @ExceptionHandler(CannotCreateUserException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleInternalServerError(exception: Exception): ErrorResponse {
        return ErrorResponse("Internal server error: ${exception.message}")
    }
}

data class ErrorResponse(val message: String)