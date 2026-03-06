package com.example.complaint.adapter.`in`.rest

import com.example.complaint.domain.model.ComplaintId
import com.example.complaint.domain.model.toComplaintStatus
import com.example.complaint.domain.port.`in`.DeleteComplaint
import com.example.complaint.domain.port.`in`.GetComplaint
import com.example.complaint.domain.port.`in`.SubmitComplaint
import com.example.complaint.domain.port.`in`.SubmitComplaintCommand
import com.example.complaint.domain.port.`in`.UpdateComplaint
import com.example.complaint.domain.port.`in`.UpdateComplaintStatus
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/complaints")
class ComplaintController(
    private val submitUseCase: SubmitComplaint,
    private val updateUseCase: UpdateComplaint,
    private val getUseCase: GetComplaint,
    private val deleteUseCase: DeleteComplaint
) {

    @PostMapping
    fun submitComplaint(@Valid @RequestBody request: SubmitComplaintRequest): ResponseEntity<ComplaintResponse> {
        val command = SubmitComplaintCommand(
            request.orderId,
            request.customerId,
            request.description,
        )
        val complaint = submitUseCase.submitComplaint(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(complaint.toResponse())
    }

    @GetMapping("/{id}")
    fun getComplaint(@PathVariable id: String): ResponseEntity<ComplaintResponse> {
        val complaintId = ComplaintId.fromString(id)
        val complaint = getUseCase.getById(complaintId)?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(complaint.toResponse())
    }

    @GetMapping
    fun getComplaintsByCustomer(@RequestParam customerId: String): ResponseEntity<List<ComplaintResponse>> {
        val complaints = getUseCase.getAllByCustomer(customerId)
        return ResponseEntity.ok(complaints.map {it.toResponse()})
    }

    @PutMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: String,
        @Valid @RequestBody request: UpdateStatusRequest
    ): ResponseEntity<ComplaintResponse>{
        val complaintId = ComplaintId.fromString(id)
        val newStatus = request.status.toComplaintStatus()

        val command = UpdateComplaintStatus(complaintId, newStatus)
        val complain = updateUseCase.updateStatus(command)
        return ResponseEntity.ok(complain.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteComplaint(@PathVariable id: String): ResponseEntity<Void> {
        val complaintId = ComplaintId.fromString(id)
        deleteUseCase.softDelete(complaintId)
        return ResponseEntity.noContent().build()
    }
}