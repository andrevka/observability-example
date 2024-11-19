# Example observability project

# Requirements
* docker
* docker-compose

# Project
This is a project for demonstrating how monitoring can be setup in a tech stack with spring-boot, prometheus, opersearch, logstash, rocket.chat, and alertmanager.

## Startup
`docker-compose up -d`

## Services
* http://localhost:5601 - opensearch dashboards for searching logs. Username dashboars and password dashboards
* http://localhost:8080/swagger-ui/index.html - spring web application swagger url
* http://localhost:8080/internal/actuator/prometheus - spring web application prometheus metrics
* http://localhost:9090/alerts - prometheus alerts
* http://localhost:9090/query - prometheus query console
* http://localhost:3001/ - rocket.chat, login with user myuser and password myuser
  * Additionally you can login with username admin and password admin and go to http://localhost:3001/admin/integrations to examine how the integration with alert manager works

## Scenarios

1. verify alerts reach your preferred channel (rocket.chat #Monitoring)
   1. Open http://localhost:3001/ (rocketchat) and login (myuser:myuser)
   2. Open application swagger-ui http://localhost:8080/swagger-ui/index.html
      1. Use the `/setActiveUsers/{activeUsers}` method and set the count to `500`
   3. Open http://localhost:9090/alerts and refresh until you see `Not enough threads to support the amount of users` go active
   4. Open application swagger-ui http://localhost:8080/swagger-ui/index.html
     1. Use the `/setActiveUsers/{activeUsers}` method and set the count to `0`
   5. Open http://localhost:9090/alerts and refresh until you see `Not enough threads to support the amount of users` go inactive
   6. In rocket chat you should have received two messages `firing: Not enough threads to support the amount of users` and `resolved: Not enough threads to support the amount of users`
      1. It may take a little time when the alert fires in prometheus and the message is sent to rocket chat
2. verify alerts reach your preferred channel (rocket.chat #Monitoring)
   1. Open http://localhost:3001/ (rocketchat) and login (myuser:myuser)
   2. Open application swagger-ui http://localhost:8080/swagger-ui/index.html
      1. Use the `/observed/{status}` and call with status FAIL
         1. During the last minute more than one ExceptionWhileObserved has been thrown should be in prometheus and rocket chat in some time
         2. About a minute later the resolved message should appear in rocket chat

## Teardown
`docker-compose down -v`