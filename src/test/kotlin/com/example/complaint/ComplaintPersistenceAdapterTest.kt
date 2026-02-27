package com.example.complaint

import com.example.complaint.adapter.out.persistence.ComplaintPersistenceAdapter
import com.example.complaint.domain.model.Complaint
import com.example.complaint.domain.model.ComplaintId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ComplaintPersistenceAdapter::class)
class ComplaintPersistenceAdapterTest {

    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:15-alpine")
            .apply {
                withDatabaseName("complaints")
                withUsername("test")
                withPassword("test")
            }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @Autowired
    private lateinit var adapter: ComplaintPersistenceAdapter

    @Test
    fun `should save and retrieve complaint`() {
        val complaint = Complaint.create(
            orderId = "TestOrder-123",
            customerId = "TestCustomer-123",
            description = "Test description"
        )

        val saved = adapter.save(complaint)
        val retrieved = adapter.findById(saved.id)

        assertNotNull(retrieved)
        assertEquals(complaint.orderId, retrieved.orderId)
        assertEquals(complaint.description, retrieved.description)
    }

    @Test
    fun `should return null when complaint not found`() {
        val result = adapter.findById(ComplaintId.generate())
        assertNull(result)
    }

    @Test
    fun `should find all complaints by customer`() {
        val customerId = "TestCustomer-123"

        adapter.save(Complaint.create(
            orderId = "TestOrder-123",
            customerId = customerId,
            description = "First complaint"
        ))

        adapter.save(Complaint.create(
            orderId = "TestOrder-456",
            customerId = customerId,
            description = "Second complaint"
        ))

        val complaints = adapter.findByCustomer(customerId)
        assertEquals(2, complaints.size)
    }
}