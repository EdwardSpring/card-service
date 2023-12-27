Service can be started easily through your terminal by running the following:

export DATASOURCE_URL="your datasource url"
export DATASOURCE_USERNAME="your database username"
export DATASOURCE_PASSWORD="your database password"

export TOKEN_SECRET="your token secret"
export TOKEN_DURATION=1239553 # This is 20 minutes
export TOKEN_HEADER="Authorization"
export TOKEN_PREFIX="Bearer "

mvn spring-boot:run

