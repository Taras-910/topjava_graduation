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
`votes/between?startDate=2020-06-30&endDate=2020-07-29' \
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


#### user anonymous getAll (menus)
`curl --location --request GET 'http://localhost:8080/topjava/rest/anonymous'`

#### user anonymous getAll ByRestaurantId (menu)
`curl --location --request GET 'http://localhost:8080/topjava/rest/anonymous/restaurants/100002'`


#### user profile get
`curl --location --request GET 'http://localhost:8080/topjava/rest/profile/100000' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### user profile update
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

#### user profile delete
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/profile/100001' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --data-raw ''`


#### user admin getAll
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/users'`

#### user admin get
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/users/100000'`

#### user admin enabled
`curl -L -X PATCH 'http://localhost:8080/topjava/rest/admin/users/100022?enabled=false'`

#### user admin create
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

#### user admin update
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

#### user admin getByEmail
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/users/by?email=admin@gmail.com' \
--data-raw ''`

#### user admin delete
`curl -L -X DELETE 'http://localhost:8080/topjava/rest/admin/users/100000'`


#### restaurant admin getAll
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/restaurants' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### restaurant admin getById
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/restaurants/100003' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### restaurant admin getByName
`curl -L -X GET 'http://localhost:8080/topjava/rest/admin/restaurants/names/Прага'
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### restaurant admin getAll menus ByDate
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/restaurants/menus?date=2020-07-30'`

#### restaurant admin getMenu ByRestaurantId and Date
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/restaurants/100002/menus?date=2020-07-30'`

#### restaurant admin deleteById
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/restaurants/100002'`

#### restaurant admin updateById
`curl --location --request PUT 'http://localhost:8080/topjava/rest/admin/restaurants/100003' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --header 'Content-Type: application/json' \
 --data-raw '{
     "id": 100003,
     "name": "Прага_обновленный_2",
     "dishes": null
 }'`

#### restaurant admin create
`curl --location --request POST 'http://localhost:8080/topjava/rest/admin/restaurants' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 --header 'Content-Type: application/json' \
 --data-raw '{
         "id": "",
         "name": "НовыйРесторан",
         "dishes": null
     }'`


#### dish admin getAll
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/dishes' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### dish admin getById
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/dishes/100004?restaurantId=100002' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### dish admin getAll
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/dishes' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### dish admin getAllByRestaurantId
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/dishes/restaurants/100002' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### dish admin getAllByRestaurantId and Date
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/dishes/restaurants/100003/date/2020-07-30' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### dish admin deleteById and RestaurantId
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/dishes/100008?restaurantId=100002' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### dish admin delete with countControl 
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/dishes/100014?restaurantId=100003' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### dish admin deleteAllByRestaurantId and Date
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/dishes/restaurants/100002/date/2020-07-30' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### dish admin update
`curl -L -X PUT 'http://localhost:8080/topjava/rest/admin/dishes/100005?restaurantId=100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
         "id": 100005,
         "name": "Updated Чай",
         "date": "2020-06-29",
         "price": 0.2
     }'`

#### dish admin create
`curl -L -X POST 'http://localhost:8080/topjava/rest/admin/dishes?restaurantId=100002' \
 -H 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu' \
 -H 'Content-Type: application/json' \
 --data-raw '{
         "id": "",
         "name": "Новая еда",
         "date": "2020-07-20",
         "price": 0.2
     }'`

#### dish admin createAllDishes of Menu
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


#### menu admin getAllToday
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/menus' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### menu admin getAllToday ByRestaurantId
`curl --location --request GET 'http://localhost:8080/topjava/rest/profile/votes/100015' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### menu admin getAllByDate
`curl --location --request GET 'http://localhost:8080/topjava/rest/admin/menus/date/2020-07-30' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### menu admin deleteByRestaurantId AndDate
`curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/menus/restaurants/100002/date/2020-07-30' \
 --header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### menu admin createMenuWithNewRestaurants
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
 
#### menu admin createMenuWithRestaurantId 
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
