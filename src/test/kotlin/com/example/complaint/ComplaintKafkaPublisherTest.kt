package com.example.complaint

import com.example.complaint.adapter.out.messaging.ComplaintDeletedEvent
import com.example.complaint.adapter.out.messaging.ComplaintEvent
import com.example.complaint.adapter.out.messaging.ComplaintKafkaPublisher
import com.example.complaint.adapter.out.messaging.ComplaintSubmittedEvent
import com.example.complaint.domain.model.Complaint
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration


@SpringBootTest(properties = [
    "spring.jpa.hibernate.ddl-auto=none",
    "spring.flyway.enabled=false"
])
@Testcontainers
class ComplaintKafkaPublisherTest {

    companion object {
        @Container
        @JvmStatic
        val kafka = KafkaContainer(DockerImageName.parse("apache/kafka"))

        @Container
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:15-alpine")
            .apply {
                withDatabaseName("complaints")
                withUsername("test")
                withPassword("test")
            }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry){
            registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers)
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)

        }
    }

    @Autowired
    private lateinit var publisher: ComplaintKafkaPublisher

    private var consumer: org.apache.kafka.clients.consumer.Consumer<String, ComplaintEvent>? = null

    @AfterEach
    fun cleanup(){
        consumer?.close()
    }


    @Test
    fun `should publish complaint submitted event`() {
        consumer = createConsumer()

        val complaint = Complaint.create(
            orderId = "TestOrder-123",
            customerId = "TestCustomer-123",
            description = "Test description"
        )
        publisher.publishComplaintSubmitted(complaint)

        val records = KafkaTestUtils.getRecords(consumer!!, Duration.ofSeconds(10))
        assertTrue(records.count() > 0)

        val event = records.records(ComplaintKafkaPublisher.TOPIC).first().value()
                as ComplaintSubmittedEvent
        assertEquals(complaint.orderId, event.orderId)
    }

    @Test
    fun `should publish delete event`(){
        consumer = createConsumer()

        val complaint = Complaint.create(
            orderId = "TestOrder-123",
            customerId = "TestCustomer-123",
            description = "Test description"
        ).markAsDeleted()
        publisher.publishComplaintDeleted(complaint)

        val records = KafkaTestUtils.getRecords(consumer!!, Duration.ofSeconds(10))
        assertTrue(records.count() > 0)

        val event = records.records(ComplaintKafkaPublisher.TOPIC).first().value()
            as ComplaintDeletedEvent
        assertEquals(complaint.orderId, event.orderId)

    }
    private fun createConsumer(): org.apache.kafka.clients.consumer.Consumer<String, ComplaintEvent> {
        val props = mutableMapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafka.bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to "test-group",
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "true",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JacksonJsonDeserializer::class.java,
            JacksonJsonDeserializer.TRUSTED_PACKAGES to "*",
            JacksonJsonDeserializer.VALUE_DEFAULT_TYPE to ComplaintEvent::class.java.name
        )

        val factory = DefaultKafkaConsumerFactory<String, ComplaintEvent>(props)
        val consumer = factory.createConsumer()
        consumer.subscribe(listOf(ComplaintKafkaPublisher.TOPIC))
        return consumer
    }
}