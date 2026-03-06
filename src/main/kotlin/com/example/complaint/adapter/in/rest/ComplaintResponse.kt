package com.example.complaint.adapter.`in`.rest

import com.example.complaint.domain.model.Complaint
import com.example.complaint.domain.model.toStatusString
import java.time.Instant

data class ComplaintResponse(
    val id: String,
    val orderId: String,
    val customerId: String,
    val description: String,
    val status: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant? = null
)

fun Complaint.toResponse() = ComplaintResponse(
    id = this.id.toString(),
    orderId = this.orderId,
    customerId = this.customerId,
    description = this.description,
    status = this.status.toStatusString(),
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    deletedAt = this.deletedAt
)