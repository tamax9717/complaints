package com.example.complaint.domain.model

import java.time.Instant

data class Complaint(
    val id: ComplaintId,
    val orderId: String,
    val customerId: String,
    val description: String,
    val status: ComplaintStatus,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    init {
        require(description.isNotBlank()) { "description cannot be blank" }
        require(orderId.isNotBlank()) { "orderId cannot be blank" }
    }
    fun updateStatus(newStatus: ComplaintStatus): Complaint {
        require(status.canTransitionTo(newStatus)){
            "cannot transition to $newStatus from $status"
        }
        return copy(status = newStatus, updatedAt = Instant.now())
    }

    companion object {
        fun create(
            orderId: String,
            customerId: String,
            description: String
        ): Complaint {
            val now = Instant.now()
            return Complaint(
                id = ComplaintId.generate(),
                orderId = orderId,
                customerId = customerId,
                description = description,
                status = ComplaintStatus.Submitted,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}