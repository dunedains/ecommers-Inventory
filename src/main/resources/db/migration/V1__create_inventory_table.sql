CREATE TABLE inventory (
    id         BIGINT  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id BIGINT  NOT NULL,
    quantity   INTEGER DEFAULT 0 NOT NULL,
    CONSTRAINT uq_inventory_product_id UNIQUE (product_id)
);
