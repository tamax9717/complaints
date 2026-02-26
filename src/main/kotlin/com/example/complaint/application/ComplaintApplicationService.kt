package com.example.complaint.application

import com.example.complaint.domain.model.Complaint
import com.example.complaint.domain.model.ComplaintId
import com.example.complaint.domain.port.`in`.DeleteComplaint
import com.example.complaint.domain.port.`in`.GetComplaint
import com.example.complaint.domain.port.`in`.SubmitComplaint
import com.example.complaint.domain.port.`in`.SubmitComplaintCommand
import com.example.complaint.domain.port.`in`.UpdateComplaint
import com.example.complaint.domain.port.`in`.UpdateComplaintStatus
import com.example.complaint.domain.port.out.ComplaintRepository
import com.example.complaint.domain.port.out.ComplaintEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

    @Service
    @Transactional
    class ComplaintApplicationService(
        private val repository: ComplaintRepository,
        private val eventPublisher: ComplaintEventPublisher
    ): SubmitComplaint, UpdateComplaint, GetComplaint, DeleteComplaint {
        override fun submitComplaint(command: SubmitComplaintCommand): Complaint {
            val complaint = Complaint.create(
                orderId = command.orderId,
                customerId = command.customerId,
                description = command.description,
            )
            val saved = repository.save(complaint)
            eventPublisher.publishComplaintSubmitted(saved)
            return saved
        }

        override fun updateStatus(command: UpdateComplaintStatus): Complaint {
            val complaint = repository.findById(command.complaintId)
                ?: throw ComplaintNotFoundException(command.complaintId)

            val previousStatus = complaint.status.toString()
            val updated = complaint.updateStatus(command.newStatus)

            val saved = repository.save(updated)
            eventPublisher.publishStatusChanged(complaint, previousStatus)

            return saved
        }
        override fun getById(id: ComplaintId): Complaint? {
            return repository.findById(id)
        }

        override fun getAllByCustomer(customerId: String): List<Complaint> {
            return repository.findByCustomer(customerId)
        }

        override fun softDelete(complaintId: ComplaintId) {
            val complaint = repository.findById(complaintId)
                ?: throw ComplaintNotFoundException(complaintId)

            if (complaint.isDeleted()) {
                throw IllegalStateException("Complaint already has been deleted")
            }

            val deleted = complaint.markAsDeleted()
            repository.save(deleted)
            eventPublisher.publishComplaintDeleted(deleted)
        }
        override fun hardDelete(complaintId: ComplaintId) {
            val complaint = repository.findById(complaintId)
                ?: throw ComplaintNotFoundException(complaintId)

            repository.delete(complaintId)
            eventPublisher.publishComplaintPermanentlyDeleted(complaint)
        }
}
class ComplaintNotFoundException(id: ComplaintId) :
    RuntimeException("Complaint not found: $id")