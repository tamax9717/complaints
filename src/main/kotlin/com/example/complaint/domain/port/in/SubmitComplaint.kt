package com.example.complaint.domain.port.`in`

import com.example.complaint.domain.model.Complaint

interface SubmitComplaint {
    fun submitComplaint(command: SubmitComplaintCommand): Complaint
}
data class SubmitComplaintCommand(
    val orderId: String,
    val customerId: String,
    val description: String
)