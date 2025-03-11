# forecast
Forecast is a system to quickly retrieve some weather data points, such as temperature (current, min, max), location and weather summmary, such as "clear sky".

## Prerequisites
Building and running the system requires the following:
- java: tested with `openjdk 23.0.2`
- gradle: tested with version `8.13`
## Run
- run server with `./gradlew run`
- run client with, e.g. `./gradlew runc --args='-zip 99501 -v'`
## Documentation
Generate java docs with `./gradlew javadoc`, which places results in `app/build/docs/javadoc`.
## Architecture
The system breaks down into three main areas: server-side, client-side, and shared code. Client and server communicate through a REST API. The API lets clients set a ZIP code that they want weather data for, and a 'verbose' flag to control data volume. Request data is submitted as JSON in a POST request, e.g. `{"cmd": "forecast", "zip": "10001", "verbose": true}`. For demonstration purposes, a command line client is provided. Web and mobile clients can easily be added to use the same interface.

    └── dev
        └── forecast
            ├── ForecastRequest.java
            ├── ForecastResponse.java
            ├── Util.java
            ├── client
            │   └── cmdline
            │       └── CommandLineClient.java
            └── server
                ├── ExternalIPService.java
                ├── ForecastServer.java
                ├── ForecastServlet.java
                ├── IPToZIPService.java
                └── WeatherService.java
    + comments
## Known Issues
- only imperial weather units are supported at the moment
