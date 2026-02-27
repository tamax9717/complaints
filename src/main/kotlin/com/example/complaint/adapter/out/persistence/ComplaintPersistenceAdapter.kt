package com.example.complaint.adapter.out.persistence

import com.example.complaint.domain.model.Complaint
import com.example.complaint.domain.model.ComplaintId
import com.example.complaint.domain.port.out.ComplaintRepository
import org.springframework.stereotype.Component

@Component
class ComplaintPersistenceAdapter(
    private val jpaRepository: ComplaintJpaRepository
): ComplaintRepository {

    override fun save(complaint: Complaint): Complaint {
        val entity = complaint.toJpaEntity()
        val saved = jpaRepository.save(entity)
        return saved.toDomain()
    }

    override fun findById(complaintId: ComplaintId): Complaint? {
        return jpaRepository.findById(complaintId.value)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findByCustomer(customerId: String): List<Complaint> {
        return jpaRepository.findByCustomerId(customerId)
            .map { it.toDomain() }
    }

    override fun delete(complaintId: ComplaintId) {
        jpaRepository.deleteById(complaintId.value)
    }
}