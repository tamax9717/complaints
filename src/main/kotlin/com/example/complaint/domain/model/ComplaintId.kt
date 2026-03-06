package com.example.complaint.domain.model

import java.util.UUID

@JvmInline
value class ComplaintId(val value: UUID) {
    companion object {
        fun generate() = ComplaintId(UUID.randomUUID())

        fun fromString(id: String): ComplaintId {
            return try {
                ComplaintId(UUID.fromString(id))
            } catch (e: IllegalArgumentException){
                throw InvalidComplaintIdException("Invalid UUID format: $id", e)
            }
        }
    }

    override fun toString() = value.toString()
}

class InvalidComplaintIdException(message: String, cause: Throwable? = null) :
    IllegalArgumentException(message, cause)