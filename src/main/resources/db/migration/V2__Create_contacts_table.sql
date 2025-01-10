CREATE TABLE contacts (
	id VARCHAR(100) not NULL primary key,
	username VARCHAR(100) not null,
	first_name VARCHAR(100) not null,
	last_name VARCHAR(100) not null,
	phone VARCHAR(100),
	email VARCHAR(100),
	constraint fk_users_contacts foreign key (username) references users(username)
)