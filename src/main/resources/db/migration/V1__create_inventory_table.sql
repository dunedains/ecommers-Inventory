CREATE TABLE inventory (
    id         NUMBER(19)  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id NUMBER(19)  NOT NULL,
    quantity   NUMBER(10)  DEFAULT 0 NOT NULL,
    CONSTRAINT uq_inventory_product_id UNIQUE (product_id)
);