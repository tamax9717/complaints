package com.example.complaint.adapter.`in`.rest

import jakarta.validation.constraints.NotBlank

data class SubmitComplaintRequest(
    @field:NotBlank(message = "Order ID is required")
    val orderId: String,

    @field:NotBlank(message = "Customer ID is required")
    val customerId: String,

    @field:NotBlank(message = "Description is required")
    val description: String
)

data class UpdateStatusRequest(
    @field:NotBlank(message = "Status is required")
    val status: String
)