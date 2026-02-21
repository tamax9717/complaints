package com.example.complaint.domain.port.out

import com.example.complaint.domain.model.Complaint
import com.example.complaint.domain.model.ComplaintId

interface ComplaintRepository {
    fun save(complaint: Complaint): Complaint
    fun findById(complaintId: ComplaintId): Complaint?
    fun findByCustomer(customerId: String): List<Complaint>
    fun delete(complaintId: ComplaintId)
}