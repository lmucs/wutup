A Java-based (Spring, Jersey, etc.) webservice for wutup. 

Sample endpoints include

    GET http://example.com/wutup/events/1
    GET http://example.com/wutup/events?page=3&pageSize=20
    GET http://example.com/wutup/events?categories=party&page=0&pageSize=10
    PUT http://example.com/wutup/events/1
    POST http://example.com/wutup/events
    DELETE http://example.com/wutup/events/1

## Production Build

To do a simple build (with unit tests), producing a war configured to use the JNDI
production data source:

    mvn clean package

## Full Build with Integration Tests

To run with integration tests:

    mvn clean verify

Running integration tests will automatically use an embdedded webserver and embedded
database.  NOTE: The integration test suite inside the application does not replace
the large regression suite to be built by QA.

## Development and Manual Testing

To run the webservice without deploying to your own container (useful for manual testing):

    mvn -DPROPERTIES_PATH=dirContainingThePropertiesFile clean tomcat:run

The path must contain a file called `wutup-webservice.properties` and contain application
secrets for JDBC properties.  A sample properties file is:

    jdbc.driverClassName=org.h2.Driver
    jdbc.url=jdbc:h2:/apps/local/wutup/db
    jdbc.username=wutup
    jdbc.password=IncorrectPonyFuelcellBrad

Now you can use poster or curl, for example:

    curl -i -X GET -H 'Accept:application/json' 'http://localhost:8080/wutup/events/1'
