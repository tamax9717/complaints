package com.example.complaint.domain.model

import java.util.UUID

@JvmInline
value class ComplaintId(val value: UUID) {
    companion object {
        fun generate() = ComplaintId(UUID.randomUUID())
    }

    override fun toString() = value.toString()
}
