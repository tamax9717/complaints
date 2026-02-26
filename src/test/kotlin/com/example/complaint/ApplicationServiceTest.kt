package com.example.complaint

import com.example.complaint.application.ComplaintApplicationService
import com.example.complaint.domain.model.Complaint
import com.example.complaint.domain.model.ComplaintId
import com.example.complaint.domain.model.ComplaintStatus
import com.example.complaint.domain.port.`in`.SubmitComplaintCommand
import com.example.complaint.domain.port.`in`.UpdateComplaintStatus
import com.example.complaint.domain.port.out.ComplaintEventPublisher
import com.example.complaint.domain.port.out.ComplaintRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import kotlin.test.assertEquals

class ApplicationServiceTest {
    private val repository: ComplaintRepository = mockk()
    private val eventPublisher: ComplaintEventPublisher = mockk(relaxed = true)
    private val service = ComplaintApplicationService(repository, eventPublisher)

    @Test
    fun `should submit complaint and publish event`(){
        val command = SubmitComplaintCommand(
            orderId = "TestOrder-123",
            customerId = "TestCustomer-123",
            description = "Test description"
        )

        val complaintSlot = slot<Complaint>()
        every { repository.save(capture(complaintSlot)) } answers {
            complaintSlot.captured
        }

        val result = service.submitComplaint(command)

        assertNotNull(result.id)
        assertEquals(ComplaintStatus.Submitted, result.status)
        verify { repository.save(any()) }
        verify { eventPublisher.publishComplaintSubmitted(any()) }
    }
    @Test
    fun `should update status and publish event`() {
        val complaintId = ComplaintId.generate()
        val existingComplaint = Complaint.create(
            orderId = "TestOrder-123",
            customerId = "TestCustomer-123",
            description = "Test description"
        )

        every { repository.findById(complaintId) } returns existingComplaint
        every { repository.save(any()) } answers {firstArg()}

        val command = UpdateComplaintStatus(
            complaintId = complaintId,
            newStatus = ComplaintStatus.InProgress
        )
        val result = service.updateStatus(command)
        assertEquals(ComplaintStatus.InProgress, result.status)
        verify {
            eventPublisher.publishStatusChanged(any(), "Submitted")
        }
    }
}