### curl (`topjava_graduation`).

#### RestaurantRest getAll
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/restaurants' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### RestaurantRest getById
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/restaurants/100003' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### RestaurantRest getByName
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/restaurants/names/Прага'
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### RestaurantRest getAll menus ByDate
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/restaurants/menus?date=2020-07-30'`

#### RestaurantRest get menu ByRestaurantId and Date
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/restaurants/100002/menus?date=2020-07-30'`

#### RestaurantRest delete ById
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/restaurants/100002'`

#### RestaurantRest update ById
`curl --location --request PUT 'http://localhost:8080/topjava/rest/admin/restaurants/100003' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --header 'Content-Type: application/json' \
 --data-raw '{
     "id": 100003,
     "name": "Прага_обновленный_2",
     "dishes": null
 }'`

#### RestaurantRest create
`curl --location --request POST 'http://localhost:8080/topjava/rest/admin/restaurants' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --header 'Content-Type: application/json' \
 --data-raw '{
         "id": "",
         "name": "НовыйРесторан",
         "dishes": null
     }'`

#### DishRest getAll
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/dishes' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### DishRest getById
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/dishes/100004?restaurantId=100002' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### DishRest getAll
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/dishes' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### DishRest getAll ByRestaurantId
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/dishes/restaurants/100002' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### DishRest getAll ByRestaurantId and Date
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/dishes/restaurants/100003/date/2020-07-30' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### DishRest delete byId and RestaurantId
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/dishes/100008?restaurantId=100002' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### DishRest delete with countControl 
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/dishes/100014?restaurantId=100003' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### DishRest deleteAll byRestaurantId and Date
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/dishes/restaurants/100002/date/2020-07-30' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### DishRest getAll
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/dishes' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### DishRest update
`curl -L -X PUT 'http://localhost:8080/topjava/rest/admin/dishes/100005?restaurantId=100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
         "id": 100005,
         "name": "Updated Чай",
         "date": "2020-06-29",
         "price": 0.2
     }'`

#### DishRest create
`curl -L -X POST 'http://localhost:8080/topjava/rest/admin/dishes?restaurantId=100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
         "id": "",
         "name": "Новая еда",
         "date": "2020-07-20",
         "price": 0.2
     }'`

#### DishRest createAllDishes of Menu
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
     },
     {
         "id": "",
         "name": "Новая еда 3",
         "date": "2020-07-29",
         "price": 0.3
     }
 ]'`
 
#### VoteRest getAll
`curl --location --request GET 'http://localhost:8080/topjava/rest/profile/votes' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### VoteRest getById
`curl --location --request GET 'http://localhost:8080/topjava/rest/profile/votes/100015' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### VoteRest getByRestaurantId
`curl --location --request GET 'http://localhost:8080/topjava/rest/profile/votes/restaurants/100003' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### VoteRest getByDate for authUser
`curl --location --request GET 'http://localhost:8080/topjava/rest/profile/votes/date/2020-07-30' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### VoteRest getAll for authUser
`curl --location --request GET 'http://localhost:8080/topjava/rest/profile/votes/auth' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`
 
#### VoteRest getBetween 
`curl --location --request GET 'http://localhost:8080/topjava/rest/profile/votes/between?startDate=2020-07-30&endDate=2020-07-30' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
--data-raw ''`

#### VoteRest delete
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/profile/votes/100015' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### VoteRest update 
`curl --location --request PUT 'http://localhost:8080/topjava/rest/profile/votes/100015' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --header 'Content-Type: application/json' \
 --data-raw '{
         "id": 100015,
         "date": "2020-06-28",
         "restaurantId": 100003,
         "userId": 100000
     }'`

#### VoteRest create
`curl --location --request POST 'http://localhost:8080/topjava/rest/profile/votes/restaurants/100002' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### FreeUserRest getAll (menus)
`curl --location --request GET 'http://localhost:8080/topjava/rest/free'`

#### FreeUserRest getAll ByRestaurantId (menu)
`curl --location --request GET 'http://localhost:8080/topjava/rest/free/restaurants/100002'`

#### ProfileRest get
`curl --location --request GET 'http://localhost:8080/topjava/rest/profile/100000' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### ProfileRest update
`curl --location --request PUT 'http://localhost:8080/topjava/rest/profile/100001' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --header 'Content-Type: application/json' \
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

#### ProfileRest delete
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/profile/100001' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### AdminRestUser getAll
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/users'`

#### AdminRestUser get
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/users/100000'`

#### AdminRestUser enabled
`curl -L -X PATCH 'http://localhost:8080/topjava/rest/admin/users/100022?enabled=false'`

#### AdminRestUser create
`curl -L -X POST 'http://localhost:8080/topjava/rest/admin/users' \
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

#### AdminRestUser update
`curl -L -X PUT 'http://localhost:8080/topjava/rest/admin/users/100000' \
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

#### AdminRestUser getByEmail
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/users/by?email=admin@gmail.com' \
--data-raw ''`

#### AdminRestUser delete
`curl -L -X DELETE 'http://localhost:8080/topjava/rest/admin/users/100000'`

#### MenuRest getAll today
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/menus' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### MenuRest getAll today ByRestaurantId
`curl --location --request GET 'http://localhost:8080/topjava/rest/profile/votes/100015' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### MenuRest getAll ByDate
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/menus/date/2020-07-30' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### MenuRest delete byRestaurantId AndDate
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/menus/restaurants/100002/date/2020-07-30' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`

#### MenuRest createMenu withNewRestaurants
`curl --location --request POST 'http://localhost:8080/topjava/rest/admin/menus/restaurant/НовыйРесторан4' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --header 'Content-Type: application/json' \
 --data-raw '[{
         "id": "",
         "name": "Новая еда 4",
         "date": "2020-07-25",
         "price": 0.1
     },
     {
         "id": "",
         "name": "Новая еда 44",
         "date": "2020-07-25",
         "price": 0.2
     },
     {
         "id": "",
         "name": "Новая еда 444",
         "date": "2020-07-25",
         "price": 0.3
     }
 ]'`
 
#### MenuRest createMenu withRestaurantId 
`curl --location --request POST 'http://localhost:8080/topjava/rest/admin/menus/restaurant/100002' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --header 'Content-Type: application/json' \
 --data-raw '[{
         "id": "",
         "name": "Новая еда 7",
         "date": "2020-07-30",
         "price": 0.1
     },
     {
         "id": "",
         "name": "Новая еда 8",
         "date": "2020-07-30",
         "price": 0.2
     },
     {
         "id": "",
         "name": "Новая еда 9",
         "date": "2020-07-30",
         "price": 0.3
     }
 ]'`
