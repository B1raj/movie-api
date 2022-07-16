# movie-api
## Testing the app

The below sequence of api calls can be made to test the application.

0. To perform login use below username and password pair to create the Authorization token:
```
john@doe.com/john
mike@doe.com/mike
will@doe.com/will
```

1. Login API:
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
uuid: <Some random value>
AccessToken: <a valid token>
movie: Titanic
year: 1997
```

3. Rate a movie
```
POST /v1/api/rating HTTP/1.1
Host: localhost:8080
uuid: <Some random value>
AccessToken: <a valid token>
Content-Type: application/json
{
	"movie":"skippy",
	"rating": 1.90,
	"year":1931
}
```

4. Update movie rating
```
PUT /v1/api/rating HTTP/1.1
Host: localhost:8080
uuid: <Some random value>
AccessToken: <a valid token>
Content-Type: application/json
{
	"movie":"skippy",
	"rating": 9.5,
	"year":1931
}
```

5. Get top 10 rated movies order by box office collection
```
GET /v1/api/top10 HTTP/1.1
Host: localhost:8080
uuid: <Some random value>
AccessToken: <a valid token>
```


[solution.md](./solution.md) <br>
[how_to_run.md](./how_to_run.md ) <br>
[how_to_test.md](./how_to_test.md ) <br>
[to_do.md](./to_do.md )<br>
[assumptions.md](./assumptions.md)<br>
[scale.md](./scale.md)<br>