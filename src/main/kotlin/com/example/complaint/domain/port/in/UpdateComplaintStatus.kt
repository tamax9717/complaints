package com.example.complaint.domain.port.`in`

import com.example.complaint.domain.model.Complaint
import com.example.complaint.domain.model.ComplaintId
import com.example.complaint.domain.model.ComplaintStatus

interface UpdateComplaint {
    fun updateStatus(command: UpdateComplaintStatus): Complaint
}

data class UpdateComplaintStatus(
    val complaintId: ComplaintId,
    val newStatus: ComplaintStatus
)