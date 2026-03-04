package com.example.complaint.adapter.out.messaging

import java.time.Instant

sealed class ComplaintEvent {
    abstract val complaintId: String
    abstract val timestamp: Instant
}

data class ComplaintSubmittedEvent(
    override val complaintId: String,
    val orderId: String,
    val customerId: String,
    val description: String,
    override val timestamp: Instant
): ComplaintEvent()

data class ComplaintStatusChangedEvent(
    override val complaintId: String,
    val newStatus: String,
    val previousStatus: String,
    override val timestamp: Instant
): ComplaintEvent()

data class ComplaintDeletedEvent(
    override val complaintId: String,
    val orderId: String,
    val customerId: String,
    override val timestamp: Instant
): ComplaintEvent()

data class ComplaintPermanentlyDeletedEvent(
    override val complaintId: String,
    val orderId: String,
    val customerId: String,
    val reason: String = "ADMIN_MAINTENANCE",
    override val timestamp: Instant
): ComplaintEvent()