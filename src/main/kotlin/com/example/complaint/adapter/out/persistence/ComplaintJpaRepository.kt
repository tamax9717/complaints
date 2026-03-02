package com.example.complaint.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface ComplaintJpaRepository : JpaRepository<ComplaintJpaEntity, UUID> {

    @Query("SELECT c FROM ComplaintJpaEntity c WHERE c.customerId = :customerId AND c.deletedAt IS NULL")
    fun findActiveByCustomerId(customerId: String): List<ComplaintJpaEntity>

    @Query("SELECT c FROM ComplaintJpaEntity c WHERE c.id = :id AND c.deletedAt IS NULL")
    fun findActiveById(id: UUID): ComplaintJpaEntity?

    @Query("SELECT c FROM ComplaintJpaEntity c WHERE c.deletedAt IS NULL")
    fun  findAllActive(): List<ComplaintJpaEntity>

    @Query("SELECT c FROM ComplaintJpaEntity c WHERE c.id = :id")
    fun findByIdIncludingDeleted(id: UUID): ComplaintJpaEntity?
}