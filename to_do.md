# movie-api


Below are the things that can be done in this application. Having said that it does not impact any functionality of the application and can be used as a production grade application
1. end 2 end reactive stack could be used. 
    * Replace embedded tomcat with netty to get the benefit of event loop.
    * Use r2dbc drivers to make database layer as reactive as well.
2. Use a global code to impose javax validations at Request bean level.
3. Use method level interceptors for logging request and responses along with uuid. 


[solution.md](./solution.md) <br>
[how_to_run.md](./how_to_run.md ) <br>
[how_to_test.md](./how_to_test.md ) <br>
[to_do.md](./to_do.md )<br>
[assumptions.md](./assumptions.md)<br>
[scale.md](./scale.md)<br>