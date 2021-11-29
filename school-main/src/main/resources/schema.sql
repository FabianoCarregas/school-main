
DROP TABLE IF EXISTS Course;

CREATE TABLE Course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(500)
);

DROP TABLE IF EXISTS User;

CREATE TABLE User (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL
   
);

DROP TABLE IF EXISTS user_course;

create table user_course (
	user_id bigint not null, 
	course_id bigint not null, 
	primary key (user_id, course_id)
	
);


