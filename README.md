# Tweet-Repository
  
Sonar Build Command:  
mvn clean verify sonar:sonar -Dsonar.projectKey=TweetAppLocal -Dsonar.host.url=http://localhost:9000 -Dsonar.login=4750b9fc8471246357269fb6aa81ed9e9f9c710d  
  
Maven surefire report commands:  
mvn site  
mvn surefire-report:report  
mvn site -DgenerateReports=false  
  
Prometheus/Grafana Commands:  
docker run -d -p 9090:9090 -v <absolute_path_to_your_prometheus_file>:/etc/prometheus/prometheus.yml prom/prometheus  
example: docker run -d -p 9090:9090 -v /mydisk/monitoring/src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus  

docker run -d -p 3000:3000 grafana/grafana  
