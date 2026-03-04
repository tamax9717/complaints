package com.example.complaint.adapter.out.messaging

import com.example.complaint.domain.model.*
import com.example.complaint.domain.port.out.ComplaintEventPublisher
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.CompletableFuture

@Component
class ComplaintKafkaPublisher(
    private val kafkaTemplate: KafkaTemplate<String, ComplaintEvent>
): ComplaintEventPublisher {
    companion object {
        const val TOPIC = "complaint-events"
    }
    override fun publishComplaintSubmitted(complaint: Complaint) {
        val event = ComplaintSubmittedEvent(
            complaintId = complaint.id.toString(),
            orderId = complaint.orderId,
            customerId = complaint.customerId,
            description = complaint.description,
            timestamp = Instant.now()
        )
        sendEvent(event, complaint.id.toString())
    }

    override fun publishStatusChanged(
        complaint: Complaint,
        previousStatus: String
    ) {
        val event = ComplaintStatusChangedEvent(
            complaintId = complaint.id.toString(),
            newStatus = complaint.status.toStatusString(),
            previousStatus = previousStatus,
            timestamp = Instant.now()
        )
        sendEvent(event, complaint.id.toString())
    }

    override fun publishComplaintDeleted(complaint: Complaint) {
        val event = ComplaintDeletedEvent(
            complaintId = complaint.id.toString(),
            orderId = complaint.orderId,
            customerId = complaint.customerId,
            timestamp = Instant.now()
        )
        sendEvent(event, complaint.id.toString())
    }

    override fun publishComplaintPermanentlyDeleted(complaint: Complaint) {
        val event = ComplaintPermanentlyDeletedEvent(
            complaintId = complaint.id.toString(),
            orderId = complaint.orderId,
            customerId = complaint.customerId,
            reason = "ADMIN_MAINTENANCE",
            timestamp = Instant.now()
        )
        sendEvent(event, complaint.id.toString())
    }

    private fun sendEvent(event: ComplaintEvent, key: String) {
        val future: CompletableFuture<SendResult<String, ComplaintEvent>> = kafkaTemplate.send(TOPIC, key, event)

        future.whenComplete { result, ex ->
            if (ex != null) {
                System.err.println("Failed to send event: $event: ${ex.message}")
            } else{
                println("Sent event to partition ${result.recordMetadata.partition()}")
            }
        }
    }

}
