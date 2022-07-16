# movie-api

Below are the assumptions.

1. User has to first perform login to get access token. Any movie api can be only invoked with a valid access token, Access token validity is configurable and currecntly set to 30 mins
2. OMBD API has high availability of atlest 99.999.
3. OMDB API is the source of truth for all movies. If a movie name don't exist in OMDB then it's an invalid movie.
4. Since Movie name can be duplicate, hence we will be using movie name and year of release as the identifier for any movie API operation.
5. The CSV file shared has lot more details than best-picture oscar award. For this app we will be only importing relevant data to database, but out JSON response is good enough to handle more award category if needed.
6. The CSV file might have invalid data, i.e. duplicate movies with same name and release year, hence in this case at the time of data ingestion, we would have resolved this issue and the SQL constraints are honoured.
7. In the CVS file for year there are many movies with pointed to 2 year (1931/32), In this case we will consider that the movie was release in later part i.e.1932.
8. Since OMDB Api doesn't clearly state if a movie won oscar for best picture, hence this information will be queried the database.
9. Since we want to implement the rating functionality, hence we will use database for storing user rating against the username.
10. OMDB API data don't change in real time, hence it's totally fair to use client side caching to boost performance and avoid additional network call, improving response time and not overshooting the OMDB per day api hit limitation
11. If box office collection doesn't exist in OMBD api or has a value of N/A. Then we will default it to 0.