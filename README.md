Java Enterprise Project TopJava_graduation
==========================================

Specification
==========================================
Design and implement a REST API using 
Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote on which restaurant they want to have lunch at
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we asume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides new menu each day.


Технологии /инструменты / фреймворки Java Enterprise:Maven/ Spring/ Security/ JPA(Hibernate)/ REST(Jackson).
==========================================================================================================
- основные классы приложения: [Dish, Restaurant, User, Vote]
- все данные хранятся в базе данных [HSQLDB]
- для обслуживания запросов анонимных пользователей (anonymous) и авторизованных (profile) 
  формирования список меню ресторанов в классе ТО [Menu] 
- бизнес-логика:
   в MenuRestController - проверка количества блюд в меню (от 2 до 5). Разрешается добавлять блюда только списком,
   или по одному блюду при условии, что в DB уже хранятся не меньше двух блюд на этот день. Удалять разрешается 
   только весь список за этот день, либо по одному блюду, если DB хранятся больше двух блюд на этот день.
   Не разрешается дублирование блюд в этот день.
   в VoteRestController - проверка голосований. Разрешается голосовать один раз в день. До 11:00 голос можно изменить.
- репозиторий [Spring Data JPA]
- Транзакционность
- Пул коннектов [Tomcat]
- список ресторанов (с едой сегодняшнего дня) кешируется [Spring cache], 
  это позволяет не обращаться к DB при стандартном запросе актуального списка меню
- базовая авторизация [SpringSecurity], доступ к ресурсам по ролям:
              `/rest/admin/**'`  - 'ADMIN'
              `/rest/profile/**` - 'USER'
              `/anonymous/**`    - свободный доступ просмотра актуальных меню
- тестирование репозитория [Junit4] 
- тестирование REST контроллеров [Junit5] через матчеры 
- логирование
- `curl` - команды тестирования голосований:

#### curl (`topjava_graduation`).

#### anonymous getAllMenusThisDay
`curl -L -X GET 'http://localhost:8080/topjava/anonymous' \
 -H 'Authorization: Basic Og=='`

#### anonymous getMenuByRestaurantIdThisDay
`curl -L -X GET 'http://localhost:8080/topjava/anonymous/restaurants/100003' \
 -H 'Authorization: Basic Og=='`


#### profile votes get (by voteId)
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/votes/100016' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### profile votes getAllForAuth
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/votes' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### profile votes getByRestaurantAuth
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/votes/restaurants/100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### profile votes getByDateForAuth for authUser
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/votes/date?date=2020-06-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu`

#### profile votes getBetween 
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/votes/between?startDate=2020-06-30&endDate=2020-07-29' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu`

#### profile votes update 
`curl -L -X PUT 'http://localhost:8080/topjava/rest/profile/votes/100016' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
     "id": 100016,
     "date": "2020-06-29",
     "restaurantId": 100003,
     "userId": 100000
 }'`

#### profile votes create
`curl -L -X POST 'http://localhost:8080/topjava/rest/profile/votes?restaurantId=100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### profile votes delete
`curl -L -X DELETE 'http://localhost:8080/topjava/rest/profile/votes/100016' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`


#### profile menus getAll
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/menus' \
  -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`
 
#### profile menus getAllToday
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/menus/today' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`
 
#### profile menus getByRestaurantToday
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/menus/restaurants/100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### profile menus getByRestaurantIdAndDate
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/menus/menu/100002?date=2020-07-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### profile menus getByRestaurantNameAndDate
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/menus/menu?restaurantName=%D0%9F%D1%80%D0%B0%D0%B3%D0%B0&date=2020-07-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### profile menus getAllByDate
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/menus/date?date=2020-07-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`


#### admin votes getAll
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin votes getById
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/100015' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin votes getAllForRestaurant
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/restaurants/100003' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin votes getAllByDate
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/date?date=2020-07-29' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin votes getByDateForUser
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/date?date=2020-07-29' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`
 
#### admin votes getAllForUser
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/users?id=100000' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`
 
#### admin votes getBetweenForUser 
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/votes/between?startDate=2020-07-30&endDate=2020-07-30&userId=100000' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin votes update 
`curl -L -X PUT 'http://localhost:8080/topjava/rest/admin/votes/100015' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
          "id": 100015,
          "date": "2020-06-28",
          "restaurantId": 100003,
          "userId": 100000
      }'`

#### admin votes create
`curl -L -X POST 'http://localhost:8080/topjava/rest/admin/votes?restaurantId=100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin votes delete
`curl -L -X DELETE 'http://localhost:8080/topjava/rest/admin/votes/100015' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`


#### admin restaurants getAll
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/restaurants' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin restaurants getById
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/restaurants/100003' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin restaurants getByName
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/restaurants/names?restaurantName=%D0%92%D0%B5%D0%BD%D0%B5%D1%86%D0%B8%D1%8F' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu`

#### admin restaurants getByIdWithDishesOfDate
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/restaurants/100003/date?date=2020-07-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin restaurants getAllWithDishes
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/restaurants/dishes' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`
 
#### admin restaurants getAllWithDishesOfDate
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/restaurants/menus?date=2020-07-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`
 
#### admin restaurants create
`curl -L -X POST 'http://localhost:8080/topjava/rest/admin/restaurants' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
         "id": "",
         "name": "НовыйРесторан",
         "dishes": null
     }'`

#### admin restaurants update
`curl -L -X PUT 'http://localhost:8080/topjava/rest/admin/restaurants/100003' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
     "id": 100003,
     "name": "Прага_обновленный_2",
     "dishes": null
 }'`

#### admin restaurants delete
`curl -L -X DELETE 'http://localhost:8080/topjava/rest/admin/restaurants/100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`


#### admin dishes getAll
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/dishes' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin dishes getById
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/dishes/100010/restaurants/100003' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin dishes getAllByRestaurantId
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/dishes/restaurants/100003' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin dishes getAllByRestaurantIdAndDate
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/dishes/menus?restaurantId=100003&date=2020-07-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin dishes createListOfMenu
`curl -L -X POST 'http://localhost:8080/topjava/rest/admin/dishes/restaurants/100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '[{
         "id": "",
         "name": "Новая еда 1",
         "date": "2020-07-29",
         "price": 0.1
     },
     {
         "id": "",
         "name": "Новая еда 2",
         "date": "2020-07-29",
         "price": 0.2
     }
 ]'`

#### admin dishes createLimit
`curl -L -X POST 'http://localhost:8080/topjava/rest/admin/dishes?restaurantId=100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
         "id": "",
         "name": "Новая еда",
         "date": "2020-07-29",
         "price": 0.2
     }'`

#### admin dishes update
`curl -L -X PUT 'http://localhost:8080/topjava/rest/admin/dishes/100005?restaurantId=100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
         "id": 100005,
         "name": "Updated Чай",
         "date": "2020-06-29",
         "price": 0.2
     }'`
 
#### admin dishes delete
`curl -L -X DELETE 'http://localhost:8080/topjava/rest/admin/dishes/100014/restaurants/100003?date=2020-07-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin dishes deleteListOfMenu 
`curl -L -X DELETE 'http://localhost:8080/topjava/rest/admin/dishes/restaurants/100002?date=2020-07-30' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`


#### profile users get
`curl -L -X GET 'http://localhost:8080/topjava/rest/profile/users/100001' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### profile users update
`curl -L -X PUT 'http://localhost:8080/topjava/rest/profile/users/100001' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
     "id": 100001,
     "name": "updatedUser",
     "email": "updatedEmail@mail.com",
     "password": "updated",
     "registered": "2020-07-30T06:00:00.000+00:00",
     "roles": [
         "USER"
     ]
 }'`

#### profile users delete
`curl -L -X DELETE 'http://localhost:8080/topjava/rest/profile/users/100001' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`


#### admin users getAll
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/users' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin users get
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/users/100000' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin users getByEmail
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/users/by?email=admin@gmail.com' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin users enabled
`curl -L -X PATCH 'http://localhost:8080/topjava/rest/admin/users/100001?enabled=false' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### admin users create
`curl -L -X POST 'http://localhost:8080/topjava/rest/admin/users' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
     "id": "",
     "name": "NewUser",
     "email": "newAddress@gmail.com",
     "password": "newPassword",
     "registered": "2020-01-30T06:00:00.000+00:00",
     "roles": [
         "USER"
     ]
 }'`

#### admin users update
`curl -L -X PUT 'http://localhost:8080/topjava/rest/admin/users/100000' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
      "id": "100000",
      "name": "UpdateUser",
      "email": "updateAddress@gmail.com",
      "password": "updatePassword",
      "registered": "2020-01-30T06:00:00.000+00:00",
      "roles": [
          "USER"
      ]
 }'`

#### admin users delete
`curl -L -X DELETE 'http://localhost:8080/topjava/rest/admin/users/100001' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`
