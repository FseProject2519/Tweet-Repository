# Tweet-Repository
## Documents  
Error/Defect Log - https://fseproject2519.atlassian.net/l/c/70FYSXo7  
Project Management - JIRA - https://fseproject2519.atlassian.net/jira/software/projects/TWEET/boards/1/roadmap  
Sonar Report - 
  
## SECTION A - Setting up the service in local:  
1. Clone the project in local using the command : git clone https://github.com/FseProject2519/Tweet-Repository.git   
2. Open STS IDE  
3. File -> Import -> Existing Maven Project -> Select Tweet Repository Folder -> Finish  
4. Clean and Build the project as shown in SECTION B  
  
## SECTION B - Maven Build:  
1. Go to: Run -> Run Configurations -> Maven Build  
2. Enter the properties as shown below for the respective services  
3. Tweet Service:  
![image](https://user-images.githubusercontent.com/104539687/173216312-4901d028-74af-4ac3-a1a0-388b5776b6e1.png)  
  
4. Authorization Service:  
![image](https://user-images.githubusercontent.com/104539687/173216470-4b7f6667-7599-4345-af8e-95bf324db120.png)  
  
5. Right click on the service in the Package Explorer and select: Maven -> Update Project -> Check Update project configuration from pom.xml, Refresh workspace   resources from  local filesystem, Clean projects -> Ok  
6. Right click on the service in the Package Explorer and select: Refresh  
7. Do 5. and 6. for all services  
  
## SECTION C - Running the services in STS without Docker:  
1. Update auth.client.url in the tweet service application.properties  
2. Perform Maven clean and build for all the services as shown in SECTION B  
3. Go to: Window -> Show View -> Boot Dashboard  
4. Start the services through Boot Dashboard in the following order:  
    authorization service -> notification service -> tweet service  
  
## SECTION D - Running the services with Docker:  
1. Update auth.client.url in the tweet service application.properties  
2. Perform Maven clean and build for all the services as shown in SECTION B   
3. Run the following command in the outermost TweetRepository folder where docker-compose.yml is present:  
      a. docker-compose build  
      b. docker compose up  
      c. To Stop: Ctrl + C (and/or) docker compose down  
 [  
 Note: If you get this error: Elasticsearch: Max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]  
 Open Windows Powershell and enter the following commands:  
 a. wsl -d docker-desktop  
 b. sysctl -w vm.max_map_count=262144  
 ]  
  
## SECTION E - Checking Sonar Report:  
1. Go to StartSonar.bat location ( Eg. C:\Program Files\sonarqube-9.4.0.54424\bin\windows-x86-64)  
2. Run StartSonar.bat  
3. Go to tweet service folder and run:  
   mvn clean verify sonar:sonar -Dsonar.projectKey=TweetAppLocal -Dsonar.host.url=http://localhost:9000 -Dsonar.login=4750b9fc8471246357269fb6aa81ed9e9f9c710d  
4. Open http://localhost:9000 and enter username = 'guest' and password = 'guest' to see the Sonar Report  
    
## SECTION F - Generating Maven surefire report:  
1. Go to tweet service folder and perform the following commands:  
    a. mvn site  
    b. mvn surefire-report:report  
    c. mvn site -DgenerateReports=false  
2. Open /tweet-service/target/site/surefire-report.html in a browser  
  
## SECTION G - Running Prometheus/Grafana:  
PROMETHEUS:  
1. Enter <Your IP address>:port in /tweet-service/src/main/resources/prometheus.yml  
2. Start the application in STS - SECTION C or through Docker - SECTION D (preferred)  
3. Open http://localhost:8090/actuator/prometheus in a browser to verify if data is getting generated  
  ![image](https://user-images.githubusercontent.com/104539687/173221594-ab002ef9-c9e0-40a3-9be4-1b1dc756da79.png)  
4. docker run -d -p 9090:9090 -v <complete path to the prometheus.yml file>:/etc/prometheus/prometheus.yml prom/prometheus  
  (Eg.: docker run -d -p 9090:9090 -v D:/mydisk/monitoring/src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus)  
5. Open http://localhost:9090/  
6. Go to: Status -> Targets and check if all statuses are UP  
![image](https://user-images.githubusercontent.com/104539687/173221655-d10a8c40-fca4-4fee-9eac-61417593688a.png)  

GRAFANA:  
1. docker run -d -p 3000:3000 grafana/grafana  
2. Open http://localhost:3000/login  
3. Enter username = 'admin' and password = 'admin'  
4. Go to: Configuration -> Data sources -> Add data source -> Prometheus  
5. Enter URL: http://<Your IP address>:9090 and click 'Save & Test'    
6. Go to: Create -> Import -> Enter 4701 (in Import via grafana.com text box) -> Click Load -> Select 'Prometheus' in the Prometheus drop down list -> Click Import  
7. Wait for around 20 mins and perform some api calls to see the statistics in the Grafana dashboard  
![image](https://user-images.githubusercontent.com/104539687/173221548-0d68c973-bf5e-43d1-912f-9617303149ac.png)  
  
## SECTION H - Setting up ELK:  
1. Start the Docker containers as given in SECTON D
2. Open http://localhost:5601 to view the Kibana console in the browser  
3. Go to: Management -> Index Patterns -> Create Index Pattern  
4. Enter 'logstash-*' in the Index Pattern field and click Next step  
5. Select @timestamp in time filter field name and click Create Index Pattern  
6. Go to: Discover (to see the logs)  
7. On the left hand side under 'Available Fields' add 'message' field to get the logged messages as follows  
  ![image](https://user-images.githubusercontent.com/104539687/173272560-cf882511-3021-4ceb-b829-e959e19b03dc.png)  
