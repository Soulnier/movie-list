## О проекте
Веб-приложение для ведения личной коллекции фильмов с возможностью:
- Просмотра списка всех фильмов
- Добавления новых фильмов с выбором жанров и статуса просмотра
- Редактирования информации о фильмах
- Удаления фильмов

## Технологии
- **Java 17**
- **Maven**
- **PostgreSQL**
- **Servlets**
- **Tomcat**

## Запуск проекта локально

### Требования
- JDK 17 или выше
- Maven
- PostgreSQL
- Git

### Инструкция по запуску

#### 1. Клонировать репозиторий
```bash
https://github.com/Soulnier/movie-list.git
cd movie_list
```

#### 2. Создать базу данных в postgresql
```sql
CREATE DATABASE movie_list;
```

#### 3. В файле В файле src/main/resources/application.properties укажите свои данные:
```
db.url=jdbc:postgresql://localhost:5432/movie_list
db.username=postgres
db.password=postgres
```

#### 4. Собрать проект
```bash
mvn clean package
```

#### 5. Запустить
```bash
mvn tomcat7:run
```

#### 6. Открыть в браузере
```
http://localhost:8080/movielist/movies
```
