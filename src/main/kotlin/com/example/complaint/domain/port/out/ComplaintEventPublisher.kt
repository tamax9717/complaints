package com.example.complaint.domain.port.out

import com.example.complaint.domain.model.Complaint

interface ComplaintEventPublisher {
    fun publishComplaintSubmitted(complaint: Complaint)
    fun publishStatusChanged(complaint: Complaint, previousStatus: String)
}