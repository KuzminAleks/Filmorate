CREATE TABLE IF NOT EXISTS MPA (
	id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS Film (
	id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(200),
	release_date DATE,
	duration INTEGER,
	MPA INTEGER REFERENCES MPA (id)
);



CREATE TABLE IF NOT EXISTS genre (
	id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS film_genre (
	film_id INTEGER REFERENCES Film (id),
	genre_id INTEGER REFERENCES genre (id)
);



CREATE TABLE IF NOT EXISTS "User" (
	id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	email VARCHAR(255),
	login VARCHAR(255),
	name VARCHAR(255),
	birthday DATE
);

CREATE TABLE IF NOT EXISTS user_friends (
	user_id INTEGER REFERENCES "User" (id),
	friend_id INTEGER REFERENCES "User" (id),
	status varchar(15) DEFAULT 'Pending',
	CHECK (user_id <> friend_id)
);



CREATE TABLE IF NOT EXISTS film_likes (
	film_id INTEGER REFERENCES Film (id),
	user_id INTEGER REFERENCES "User" (id)
);