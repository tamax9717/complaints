package com.example.complaint.domain.port.`in`

import com.example.complaint.domain.model.ComplaintId

interface DeleteComplaint {
    fun softDelete(complaintId: ComplaintId)
    fun hardDelete(complaintId: ComplaintId)
}