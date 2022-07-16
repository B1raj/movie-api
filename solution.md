# movie-api

I have used springboot with web-flux, trying to make the movie API's as reactive. I have tried to keep the code simple following the general and simple folder structures.

In this solution I have used H2 databases, The database gets initialised with the help of JPA and the seed sql queries.
I have also used spring caching feature to avoid additional omdb api call, considering the omdb responses are not changing in real time. Hence, it is considered safe to be cached at application level.

#Diagram






