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