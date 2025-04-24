CREATE TABLE if not exists brands (
       id int AUTO_INCREMENT PRIMARY KEY,
       brand_name VARCHAR(255),
       created_at TIMESTAMP,
       updated_at TIMESTAMP
);

CREATE TABLE if not exists categories (
      id int AUTO_INCREMENT PRIMARY KEY,
      category_name VARCHAR(255),
      cost bigint,
      priority_order int,
      brand_id int,
      created_at TIMESTAMP,
      updated_at TIMESTAMP
);

ALTER TABLE categories
    ADD CONSTRAINT fk_category_brand
        FOREIGN KEY (brand_id)
            REFERENCES brands(id);