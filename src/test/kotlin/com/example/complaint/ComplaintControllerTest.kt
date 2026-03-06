package com.example.complaint

import com.example.complaint.adapter.`in`.rest.ComplaintController
import com.example.complaint.adapter.`in`.rest.SubmitComplaintRequest
import com.example.complaint.domain.model.*
import com.example.complaint.domain.port.`in`.*
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import tools.jackson.databind.ObjectMapper

@WebMvcTest(ComplaintController::class)
class ComplaintControllerTest {
    @TestConfiguration
    class TestConfig {
        @Bean
        fun submitUseCase() = mockk<SubmitComplaint>()
        @Bean fun updateUseCase() = mockk<UpdateComplaint>()
        @Bean fun getUseCase() = mockk<GetComplaint>()
        @Bean fun deleteUseCase() = mockk<DeleteComplaint>()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var submitUseCase: SubmitComplaint
    @Autowired private lateinit var getUseCase: GetComplaint
    @Autowired private lateinit var deleteUseCase: DeleteComplaint
    @Autowired private lateinit var updateUseCase: UpdateComplaint
    @Autowired private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `should submit complaint`() {
        val request = SubmitComplaintRequest("ORD-123", "CUST-456", "Damaged")
        val complaint = Complaint.create("ORD-123", "CUST-456", "Damaged")

        every { submitUseCase.submitComplaint(any()) } returns complaint

        mockMvc.perform(
            post("/api/complaints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.orderId").value("ORD-123"))
    }

    @Test
    fun `should reject invalid request`() {
        val request = SubmitComplaintRequest("", "CUST-456", "Test")

        mockMvc.perform(
            post("/api/complaints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors.orderId").exists())
            .andExpect(jsonPath("$.status").value(400))  // ⭐ Check status field
    }

    @Test
    fun `should return 404 when not found`() {
        val id = ComplaintId.generate()
        every { getUseCase.getById(id) } returns null

        mockMvc.perform(get("/api/complaints/${id}"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should delete complaint`() {
        val id = ComplaintId.generate()
        every { deleteUseCase.softDelete(id) } just Runs

        mockMvc.perform(delete("/api/complaints/${id}"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `should reject invalid UUID`() {
        mockMvc.perform(get("/api/complaints/not-a-uuid"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.timestamp").exists())
    }
}