package com.example.complaint.adapter.out.persistence

import jakarta.persistence.Entity
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "complaints")
class ComplaintJpaEntity(

    @Id
    val id: UUID,

    @Column(name = "order_id", nullable = false)
    val orderId: String,

    @Column(name = "customer_id", nullable = false)
    val customerId: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(nullable = false, length = 50)
    val status: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant
)