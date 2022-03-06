package me.theseems.thrut.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.web.HttpMediaTypeException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.persistence.EntityNotFoundException

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInternalError(exception: Exception): ResponseEntity<Map<String, Any>> {
        logger.error("Internal error", exception)
        return ResponseEntity
            .internalServerError()
            .body(
                mapOf(
                    "success" to false,
                    "error" to "internal error has occurred"
                )
            )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleEntityNotFound(exception: EntityNotFoundException): ResponseEntity<Map<String, Any?>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                mapOf(
                    "success" to false,
                    "error" to "requested resource not found",
                    "details" to exception.message
                )
            )
    }

    @ExceptionHandler(HttpMediaTypeException::class, HttpMessageConversionException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleMalformedMessage(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .unprocessableEntity()
            .body(
                mapOf(
                    "success" to false,
                    "error" to "malformed request"
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintFailure(exception: MethodArgumentNotValidException): ResponseEntity<Map<String, Any?>> {
        return ResponseEntity
            .badRequest()
            .body(
                mapOf(
                    "success" to false,
                    "error" to "incorrect data provided",
                    "details" to exception.fieldErrors.map {
                        mapOf(
                            "field" to it.field,
                            "error" to (it.defaultMessage ?: "unknown")
                        )
                    }
                )
            )
    }
}
