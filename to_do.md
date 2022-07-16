# movie-api

Below are the things that I would have done in this application, if had more time. Having said that it does not impact any functionality of the application.
1. I would have used an end 2 end reactive stack. 
   1. Would like to replace embedded tomcat with netty to get the benefit of event loop.
   2. Would like to use r2dbc drivers to make database layer as reactive as well.
2. I would have used a global code to impose javax validations at Request bean level.
3. Used method level interceptors for logging request and responses along with uuid. 
