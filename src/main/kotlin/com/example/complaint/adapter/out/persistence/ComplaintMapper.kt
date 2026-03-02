package com.example.complaint.adapter.out.persistence

import com.example.complaint.domain.model.Complaint
import com.example.complaint.domain.model.ComplaintId
import com.example.complaint.domain.model.toComplaintStatus
import com.example.complaint.domain.model.toStatusString


fun ComplaintJpaEntity.toDomain(): Complaint {
    return Complaint(
        id = ComplaintId(this.id),
        orderId = this.orderId,
        customerId = this.customerId,
        description = this.description,
        status = this.status.toComplaintStatus(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
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
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}

