# movie-api

The application uses h2 in-memory database, So once the application is booted up the console can be accessed at
```
http://localhost:8080/h2-console 
un: sa
pwd: password
```

The application has spring doc open-api library to expose swagger documention out of the box, which can be accessed at
```
http://localhost:8080/swagger-ui/index.html 
```


This application serves the below functionality:

* Authentication of end user is done by calling /v1/api/login and provides JWT token (Access token) in return.
* Access token needs to be provided in header for accessing all other movie API.
* All APIs take UUID (used in request logging, for tracing purpose)
* After successful login, end user needs to perform the following using exposed rest API's
* Also attached here the postman bundle movie-api.postman_collection.json
* Exposes API to Check if a movie won oscar or not, by providing the movie name and year of release as input
* Exposes API to store/update user movie rating .
* Provides a list of 10 top-rated movies ordered by box office value.

```
To perform login use below username and password pair to create the Authorization token:
john@doe.com/john
mike@doe.com/mike
will@doe.com/will
```

Login API:
```
POST /v1/api/login HTTP/1.1
Host: localhost:8080
Authorization: Basic c2NvdEB5YWhvby5jb206dGlnZXI=
uuid: <Some random value>
```

2. check if movie won oscar or not
```
GET /v1/api/movie HTTP/1.1
Host: localhost:8080
uuid: 23232
AccessToken: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTc4MjI5OTMsImlzcyI6Imh0dHBzOi8vYmlyYWouYXV0aC5zZXJ2aWNlLmNvbSIsImlhdCI6MTY1NzgyMTE5MywiYXVkIjoiaHR0cHM6Ly9iaXJhai5tb3ZpZS5hcGkuY29tL2FjY2VzcyIsInVzZXJJZCI6Im1pa2VAZG9lLmNvbSJ9.4H3SPTr8rHtUzAKowPrk_YwWhHpDCrM50FY4RRsLW4E
movie: Titanic
year: 1997
```

3. Rate a movie
```
POST /v1/api/rating HTTP/1.1
Host: localhost:8080
uuid: 23232
AccessToken: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTc4MjE5MjcsImlzcyI6Imh0dHBzOi8vYmlyYWouYXV0aC5zZXJ2aWNlLmNvbSIsImlhdCI6MTY1NzgyMDEyNywiYXVkIjoiaHR0cHM6Ly9iaXJhai5tb3ZpZS5hcGkuY29tL2FjY2VzcyIsInVzZXJJZCI6Im1pa2VAZG9lLmNvbSJ9.C7JKGnWWvnF5p9KJ3Js9Rp_Vqt51Q7wBh0gmQ40OUYw
Content-Type: application/json
{
	"movie":"skippy",
	"rating": 9.90,
	"year":1931
}
```

4. Update movie rating
```
PUT /v1/api/rating HTTP/1.1
Host: localhost:8080
uuid: 23232
AccessToken: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTc4MjE5MjcsImlzcyI6Imh0dHBzOi8vYmlyYWouYXV0aC5zZXJ2aWNlLmNvbSIsImlhdCI6MTY1NzgyMDEyNywiYXVkIjoiaHR0cHM6Ly9iaXJhai5tb3ZpZS5hcGkuY29tL2FjY2VzcyIsInVzZXJJZCI6Im1pa2VAZG9lLmNvbSJ9.C7JKGnWWvnF5p9KJ3Js9Rp_Vqt51Q7wBh0gmQ40OUYw
Content-Type: application/json
{
	"movie":"skippy",
	"rating": 9.90,
	"year":1931
}
```

5. Get top 10 rated movies order by box office collection
```
GET /v1/api/top10 HTTP/1.1
Host: localhost:8080
uuid: 23232
AccessToken: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTc4MjE5MjcsImlzcyI6Imh0dHBzOi8vYmlyYWouYXV0aC5zZXJ2aWNlLmNvbSIsImlhdCI6MTY1NzgyMDEyNywiYXVkIjoiaHR0cHM6Ly9iaXJhai5tb3ZpZS5hcGkuY29tL2FjY2VzcyIsInVzZXJJZCI6Im1pa2VAZG9lLmNvbSJ9.C7JKGnWWvnF5p9KJ3Js9Rp_Vqt51Q7wBh0gmQ40OUYw
Content-Type: application/json

```