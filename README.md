
# Spring-Reactive-Microservices

Microservices with Spring Reactive (WebFlux).

Components in this project.

* Api-Gateway (Spring Cloud gateway & OAuth2 Resource server)
* Auth Libraries (Common OAuth2 configuration)
* Auth Service (OAuth2 Authorization server)
* Common Service (Common spring configuration)
* Eureka Server (Service Registry)
* Mqtt Service (MQTT related operations)
* Redisson Worker Service (Redisson worker node for distributed processing with Redis)
* User Management Service (User registration & information)
* Zuul Server (Netflix zuul & OAuth2 Resource server)

----------
#### Explanation for each and every service

* ###### Api-Gateway
    1. Acts as gateway.
    2. OAuth2 resource server for Spring WebFlux.
    3. Supports WebSocket.
    4. Supports social login requests.

* ###### Auth Libraries
    1. Contains common OAuth2 spring configurations for both Authorization and Resource server.

* ###### Auth Service
    1. Acts as OAuth2 authorization server.
    2. Stores OAuth token in Mongo DB (Custom TokenStore).
    3. Social login with google and facebook configuration.
    4. Spring Feign Client and Feign Client FallbackFactory configuration.    

* ###### Common Service
    1. Contains shared spring configurations for all services. 

* ###### Eureka Server
    1. Acts as Service Registry. 

* ###### Mqtt Service
    1. Communicates with MQTT.
    2. Subscribes to toics in MQTT.
    3. Publishes data with [Netty Socket IO](https://github.com/mrniko/netty-socketio)  
    4. [Redisson](https://github.com/redisson/redisson) config for Job scheduling and Distibuted processing.

* ###### Redisson Worker Service
    1. This application is a worker node for running the jobs scheduled in Redis.
    2. Contains [Redisson Node](https://github.com/redisson/redisson/wiki/2.-Configuration) configuration.

* ###### User Management Service
    1. Contains User details and information related stuff.
    2. Gmail configuration for mail communication.

* ###### Zuul Server
    1. Acts as gateway.
    2. OAuth2 resource server.
    3. Doesn't support WebSocket.



