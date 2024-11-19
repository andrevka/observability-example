# Example observability project

# Requirements
* docker
* docker-compose

## Services
* http://localhost:5601 - opensearch dashboards for searching logs. Username dashboars and password dashboards
* http://localhost:8080/swagger-ui/index.html - spring web application swagger url
* http://localhost:8080/internal/actuator/prometheus - spring web application prometheus metrics
* http://localhost:9090/alerts - prometheus alerts
* http://localhost:9090/query - prometheus query console
* http://localhost:3001/ - rocket.chat, login with user myuser and password myuser
  * Additionally you can login with username admin and password admin and go to http://localhost:3001/admin/integrations to examine how the integration with alert manager works   