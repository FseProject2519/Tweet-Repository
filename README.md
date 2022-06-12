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

Docker Steps:  
1. Perform Maven clean and build all the services  
2. Run the following commands in the respective service folders:  
      a. docker build --tag=tweet-service .  
      b. docker build --tag=authorization .  
3. Run the following command in the outermost TweetRepository folder where docker-compose.yml is present:  
      a. docker compose up  
      b. To Stop: Ctrl + C (and/or) docker compose down  
