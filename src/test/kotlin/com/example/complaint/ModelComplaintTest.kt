package com.example.complaint

import com.example.complaint.domain.model.Complaint
import com.example.complaint.domain.model.ComplaintStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ModelComplaintTest {
    @Test
    fun `should create complaint with submitted status`(){
        val complaint = Complaint.create(
            orderId = "TestOrder-123",
            customerId = "TestCustomer-123",
            description = "Test Description.':,"
        )
        assertNotNull(complaint.id)
        assertEquals(ComplaintStatus.Submitted, complaint.status)
        assertEquals("TestOrder-123", complaint.orderId)
    }

    @Test
    fun `should reject blank description`(){
        assertThrows<IllegalArgumentException> {
            val complaint = Complaint.create(
                orderId = "TestOrder-123",
                customerId = "TestCustomer-123",
                description = " "
            )
        }

    }

    @Test
    fun `should allow status transition from submitted to in progress`(){
        val complaint = Complaint.create(
            orderId = "TestOrder-123",
            customerId = "TestCustomer-123",
            description = "Got some issue, help"
        )
        val updatedStatus = complaint.updateStatus(ComplaintStatus.InProgress)
        assertEquals(ComplaintStatus.InProgress, updatedStatus.status)
    }

    @Test
    fun `should reject invalid status transition`(){
        val complaint = Complaint.create(
            orderId = "TestOrder-123",
            customerId = "TestCustomer-123",
            description = "Help please"
        )
        assertThrows<IllegalArgumentException> {
            complaint.updateStatus(ComplaintStatus.Resolved)
        }
    }

}