# Example observability project

# Requirements
* docker
* docker-compose

## Services
* http://localhost:5601 - opensearch dashboards for searching logs. Username and password dashboards dashboards
* http://localhost:8080/swagger-ui/index.html - spring web application swagger url
* http://localhost:8080/internal/actuator/prometheus - spring web application prometheus metrics
* http://localhost:9090/alerts - prometheus alerts
* http://localhost:9090/query - prometheus query console