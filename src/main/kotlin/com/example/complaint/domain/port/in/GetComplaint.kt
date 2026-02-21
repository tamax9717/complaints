package com.example.complaint.domain.port.`in`

import com.example.complaint.domain.model.Complaint
import com.example.complaint.domain.model.ComplaintId

interface GetComplaint {
    fun getById(id: ComplaintId): Complaint?
    fun getAllByCustomer(customerId: String): List<Complaint>
}