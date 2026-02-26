CREATE TABLE complaints (
                            id UUID PRIMARY KEY,
                            order_id VARCHAR(255) NOT NULL,
                            customer_id VARCHAR(255) NOT NULL,
                            description TEXT NOT NULL,
                            status VARCHAR(50) NOT NULL,
                            created_at TIMESTAMP NOT NULL,
                            updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_complaints_customer_id ON complaints(customer_id);
CREATE INDEX idx_complaints_order_id ON complaints(order_id);