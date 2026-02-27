package com.example.complaint.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ComplaintJpaRepository : JpaRepository<ComplaintJpaEntity, UUID> {
    fun findByCustomerId(customerId: String): List<ComplaintJpaEntity>
}