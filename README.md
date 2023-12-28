
# READ ME

##### This service runs on port 2024 and can be accessed locally through localhost:2024
##### Useful links
- [Base url](http://localhost:2024/api)
- [Open in Swagger](http://localhost:2024/swagger-ui/index.html)
#### This is a dynamic service which can be run on any environment without having to change the application.yml values. It can be started easily through your terminal by running the following:

```
export DATASOURCE_URL="your datasource url"
export DATASOURCE_USERNAME="your database username"
export DATASOURCE_PASSWORD="your database password"
export TOKEN_SECRET="your token secret"
export TOKEN_DURATION=1239553
export TOKEN_HEADER="Authorization"
export TOKEN_PREFIX="Bearer "
mvn spring-boot:run
```
