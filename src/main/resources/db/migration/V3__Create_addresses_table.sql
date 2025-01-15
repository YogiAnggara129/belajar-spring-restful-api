CREATE TABLE addresses (
    id VARCHAR(100) not NULL primary key,
    contact_id VARCHAR(100) not NULL,
    street VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    province VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    postal_code VARCHAR(10) NOT NULL,
    constraint fk_contacts_addresses foreign key (contact_id) references contacts(id)
)