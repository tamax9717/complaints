package com.example.complaint.adapter.out.persistence

import com.example.complaint.domain.model.Complaint
import com.example.complaint.domain.model.ComplaintId
import com.example.complaint.domain.model.ComplaintStatus

private fun String.toComplaintStatus(): ComplaintStatus {
    return when (this) {
        "SUBMITTED" -> ComplaintStatus.Submitted
        "IN_PROGRESS" -> ComplaintStatus.InProgress
        "RESOLVED" -> ComplaintStatus.Resolved
        "CLOSED" -> ComplaintStatus.Closed
        else -> throw IllegalArgumentException("Unknown complaint status $this")
    }
}
private fun ComplaintStatus.toStatusString(): String {
    return when (this) {
        is ComplaintStatus.Submitted -> "SUBMITTED"
        is ComplaintStatus.InProgress -> "IN_PROGRESS"
        is ComplaintStatus.Resolved -> "RESOLVED"
        is ComplaintStatus.Closed -> "CLOSED"
    }
}

fun ComplaintJpaEntity.toDomain(): Complaint {
    return Complaint(
        id = ComplaintId(this.id),
        orderId = this.orderId,
        customerId = this.customerId,
        description = this.description,
        status = this.status.toComplaintStatus(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
fun Complaint.toJpaEntity(): ComplaintJpaEntity {
    return ComplaintJpaEntity(
        id = this.id.value,
        orderId = this.orderId,
        customerId = this.customerId,
        description = this.description,
        status = this.status.toStatusString(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

