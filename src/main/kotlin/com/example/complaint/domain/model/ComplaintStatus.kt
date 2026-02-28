package com.example.complaint.domain.model

sealed class ComplaintStatus {
    data object Submitted : ComplaintStatus()
    data object InProgress : ComplaintStatus()
    data object Resolved : ComplaintStatus()
    data object Closed : ComplaintStatus()

    fun canTransitionTo(newStatus: ComplaintStatus): Boolean {
        return when (this) {
            is Submitted -> newStatus is InProgress || newStatus is Closed
            is InProgress -> newStatus is Resolved || newStatus is Closed
            is Resolved -> newStatus is Closed
            is Closed -> false
        }

    }
}

fun ComplaintStatus.toStatusString(): String = when (this) {
    is ComplaintStatus.Submitted -> "SUBMITTED"
    is ComplaintStatus.InProgress -> "IN_PROGRESS"
    is ComplaintStatus.Resolved -> "RESOLVED"
    is ComplaintStatus.Closed -> "CLOSED"
}

fun String.toComplaintStatus(): ComplaintStatus = when (this.uppercase()) {
    "SUBMITTED" -> ComplaintStatus.Submitted
    "IN_PROGRESS" -> ComplaintStatus.InProgress
    "RESOLVED" -> ComplaintStatus.Resolved
    "CLOSED" -> ComplaintStatus.Closed
    else -> throw IllegalArgumentException("Unknown ComplaintStatus: $this")
}