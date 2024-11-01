INSERT INTO MPA (name)
VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');


INSERT INTO Film (NAME, DESCRIPTION, RELEASE_DATE, DURATION)
VALUES ('Test1', 'Test1', '2024-07-11', 120);

INSERT INTO Film (NAME, DESCRIPTION, RELEASE_DATE, DURATION)
VALUES ('Test2', 'Test2', '2023-07-11', 90);

INSERT INTO Film (NAME, DESCRIPTION, RELEASE_DATE, DURATION)
VALUES ('Test3', 'Test3', '2023-11-11', 123);

INSERT INTO Film (NAME, DESCRIPTION, RELEASE_DATE, DURATION)
VALUES ('Test4', 'Test4', '2022-09-23', 99);


INSERT INTO film_mpa
VALUES (1, 2), (2, 3), (3, 1), (4, 5);


INSERT INTO "User" (EMAIL, LOGIN, NAME, BIRTHDAY) 
VALUES ('User1@mail.ru', 'User1', 'Jhone', '2004-12-01');

INSERT INTO "User" (EMAIL, LOGIN, NAME, BIRTHDAY) 
VALUES ('User2@mail.ru', 'User2', 'Alex', '2005-07-12');

INSERT INTO "User" (EMAIL, LOGIN, NAME, BIRTHDAY) 
VALUES ('User3@mail.ru', 'User3', 'Anna', '2002-01-13');

INSERT INTO "User" (EMAIL, LOGIN, NAME, BIRTHDAY) 
VALUES ('User4@mail.ru', 'User4', 'Mike', '2001-10-08');


INSERT INTO FILM_LIKES 
VALUES (1, 1), (1, 2), (2, 3), (3, 4);


INSERT INTO GENRE(name) 
VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');


INSERT INTO FILM_GENRE 
VALUES (1, 1), (1, 2), (2, 3), (3, 1), (4, 4);


INSERT INTO USER_FRIENDS 
VALUES (1, 2), (1, 3), (2, 4), (4, 1);