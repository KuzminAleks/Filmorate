# Диаграмма БД
**Ссылка на диаграмму:** [Описание БД](https://dbdiagram.io/d/670ce97a97a66db9a3e42692)
# Примеры запросов
*Запрос всех пользователей* 
```sql
SELECT u.email,
  u.login,
  u.name,
  u.birthday
  uf.friend_id AS friends
FROM User AS u
LEFT JOIN user_friends AS uf ON u.id = uf.user_id;
```

*Запрос всех фильмов*
```sql
SELECT f.name,
  f.description,
  f.release_date,
  f.duration,
  fl.user_id AS likes,
  g.name AS genre,
  mpa.name AS MPA
FROM Film AS f
LEFT JOIN film_likes AS fl ON f.id = fl.film_id
LEFT JOIN genre AS g ON f.genre = g.id
LEFT JOIN MPA AS mpa ON f.MPA = mpa.id
```

*Топ N популярных фильмов*
```sql
SELECT f.name
FROM Film AS f
LEFT JOIN film_likes AS fl ON f.id = fl.film_id
GROUP BY f.name
ORDER BY COUNT(fl.user_id) DESC
LIMIT N
```

*Запрос общих друзей*
```sql
SELECT u.email,
  u.login,
  u.name
FROM User AS u
WHERE (SELECT friend_id
      FROM user_friends
      WHERE user_id = 1) AS friends1 IN (SELECT friend_id
                                        FROM user_friends
                                        WHERE user_id = 2) AS friends2
```
