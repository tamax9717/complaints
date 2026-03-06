package com.example.complaint.adapter.`in`.rest

import com.example.complaint.application.ComplaintNotFoundException
import com.example.complaint.domain.model.InvalidComplaintIdException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ComplaintNotFoundException::class)
    fun handleNotFound(e: ComplaintNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                status = HttpStatus.NOT_FOUND.value(),
                message = e.message ?: "Complaint not found",
                timestamp = Instant.now()
            )
        )
    }
    @ExceptionHandler(ConcurrentModificationException::class)
    fun handleConcurrentModification(e: ConcurrentModificationException):
            ResponseEntity<ErrorResponse>{
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse(
                status = HttpStatus.CONFLICT.value(),
                message = e.message ?: "Concurrent modification detected",
                timestamp = Instant.now()
            )
        )
    }
    @ExceptionHandler(IllegalArgumentException::class, InvalidComplaintIdException::class)
    fun handleBadRequest(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = e.message ?: "Invalid request",
                timestamp = Instant.now()
            )
        )
    }
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(e: MethodArgumentNotValidException):
            ResponseEntity<ValidationErrorResponse> {
        val errors = e.bindingResult.allErrors.associate {
            val fieldName = (it as FieldError).field
            val errorMessage = it.defaultMessage ?: "Invalid value"
            fieldName to errorMessage
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ValidationErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = "Validation failed",
                errors = errors,
                timestamp = Instant.now()
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericError(e: Exception): ResponseEntity<ErrorResponse> {
        // Log the exception in production
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message = "An unexpected error occurred",
                timestamp = Instant.now()
            )
        )
    }

}

data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: Instant
)

data class ValidationErrorResponse(
    val status: Int,
    val message: String,
    val errors: Map<String, String>,
    val timestamp: Instant
)