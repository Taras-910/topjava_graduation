Java Enterprise Project TopJava_graduation
==========================================

Технологии /инструменты / фреймворки Java Enterprise:
Maven/ Spring/ Security/ JPA(Hibernate)/ REST(Jackson).

- основные классы приложения: [Dish, Restaurant, User, Vote]
- все данные хранятся в базе данных [HSQLDB]
- для формирования меню ресторанов используется класс [Menu] (Transport Object) 
- бизнес-логика выполняется в классах:
             в MenuRestController - проверка количества блюд в меню (от 2 до 5)
             в VoteRestController - проверка голосований в соответствии с ТЗ
- репозиторий [Spring Data JPA]
- Транзакции
- Пул коннектов [Tomcat]
- список ресторанов (с едой) кешируется [Spring cache]
- базовая авторизация [SpringSecurity], доступ к ресурсам по ролям:
              `/rest/admin/**'`  - 'ADMIN'
              `/rest/profile/**` - 'USER'
              `/anonymous/**`    - свободный доступ просмотра актуальных меню
- тестирование репозитория [Junit4] 
- тестирование REST контроллеров [Junit5] через матчеры 
- логирование тестов
- `curl` тестирования голосований с
(полный список команд в config.curl.md) :

#### curl (`topjava_graduation`).

#### vote admin getAll
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote admin getById
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/100015' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote admin getByRestaurantId
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/restaurants/100003' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote admin getByDate
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/date/2020-07-29' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote admin getAllForUser
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/users/100001' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`
 
#### vote admin isExistForUserByDate
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/users/100000/date/2020-07-30' \
-H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`
 
#### vote admin getBetween 
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/between/users/100000/?startDate=2020-07-30&endDate=2020-07-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote admin delete
`curl -L -X DELETE 'http://localhost:8080/topjava/rest/admin/votes/100015' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote admin update 
`curl -L -X PUT 'http://localhost:8080/topjava/rest/admin/votes/100015' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
         "id": 100015,
         "date": "2020-06-28",
         "restaurantId": 100003,
         "userId": 100000
     }'`

#### vote admin create
`curl -L -X POST 'http://localhost:8080/topjava/rest/admin/votes/restaurants/100002/users/100001' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`


#### vote profile get byVoteId
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/votes/100016' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote profile getAllForAuth
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/votes/auth' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote profile getByRestaurantIdForAuth
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/votes/restaurants/100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote profile getByDateForAuth for authUser
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/votes/date/2020-06-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote profile isExistVote
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/votes/exist/date/2020-06-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`
 
#### vote profile getBetween 
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/votes/between?startDate=2020-06-30&endDate=2020-07-29' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote profile delete
`curl -L -X DELETE 'http://localhost:8080/topjava/rest/profile/votes/100016' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### vote profile update 
`curl -L -X PUT 'http://localhost:8080/topjava/rest/profile/votes/100016' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
     "id": 100016,
     "date": "2020-06-29",
     "restaurantId": 100003,
     "userId": 100000
 }'`

#### vote profile create
`curl -L -X POST 'http://localhost:8080/topjava/rest/profile/votes/restaurants/100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`
