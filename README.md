# Tweet-Repository  
Github Link -  [https://github.com/FseProject2519/Tweet-Repository ](https://github.com/FseProject2519/Tweet-Repository/tree/cloud-dee-dev) 
## Documents  
1. Screenshots - [https://github.com/FseProject2519/Tweet-Repository/tree/dee-dev/Project_Documents/Screenshots ](https://github.com/FseProject2519/Tweet-Repository/blob/cloud-dee-dev/Project_Documents/Screenshots/AWS_SCREENSHOTS.pdf) 
  
## AWS ARCHITECTURE:  
 ![image](https://user-images.githubusercontent.com/104539687/192669339-f6cc8ca9-d947-41f9-8b2f-39e9d273949a.png)

## SECTION A - Setting up the service in local:  
1. Clone the project in local using the command : git clone -b cloud-dee-dev https://github.com/FseProject2519/Tweet-Repository.git   
2. Open STS IDE  
3. File -> Import -> Existing Maven Project -> Select Tweet Repository Folder -> Finish  
4. Clean and Build the project as shown in SECTION B  
  
## SECTION B - Maven Build:  
###### Note: Tweet service is hosted in AWS hence it is not required to set it up in local.
1. Go to: Run -> Run Configurations -> Maven Build  
2. Enter the properties as shown below for the respective services  
3. E.g. Authorization Service:  
![image](https://user-images.githubusercontent.com/104539687/173216470-4b7f6667-7599-4345-af8e-95bf324db120.png)  
  
5. Right click on the service in the Package Explorer and select: Maven -> Update Project -> Check Update project configuration from pom.xml, Refresh workspace   resources from  local filesystem, Clean projects -> Ok  
6. Right click on the service in the Package Explorer and select: Refresh  
7. Do 5. and 6. for all services  
  
## SECTION C - Running the services in STS without Docker:   
1. Perform Maven clean and build for all the services as shown in SECTION B  
2. Go to: Window -> Show View -> Boot Dashboard  
3. Start the services through Boot Dashboard in the following order:  
    authorization service -> notification service  
