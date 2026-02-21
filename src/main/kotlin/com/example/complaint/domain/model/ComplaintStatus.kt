package com.example.complaint.domain.model

sealed class ComplaintStatus {
    object Submitted : ComplaintStatus()
    object InProgress : ComplaintStatus()
    object Resolved : ComplaintStatus()
    object Closed : ComplaintStatus()

    fun canTransitionTo(newStatus: ComplaintStatus): Boolean {
        return when (this) {
            is Submitted -> newStatus is InProgress || newStatus is Closed
            is InProgress -> newStatus is Resolved || newStatus is Closed
            is Resolved -> newStatus is Closed
            is Closed -> false
        }

    }
}