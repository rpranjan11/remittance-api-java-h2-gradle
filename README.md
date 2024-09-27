 
### Installation
In order to run/execute/deploy this project, following software/tools/packages needs to be installed:
* Java - version=<17 [I used version 21]
* Eclipse IDE - Latest version [preferred]

### Project setup in Eclipse IDE
* Install Buildship Gradle Integration form the Eclipse marketplace
* Import the project in Eclipse as a gradle project
* Set the appropriate project properties like Java version [if multiple version installation on the pc]
* Right click on Project from Project Explorer and Select Gradle>Refresh Gradle Project
* Now, the project can be Gradle Build and Gradle Run


### Implementation/Development Details
All the Assignment constraints and guidelines have been fulfilled as per my best understanding to them.
* The project is properly structured into standard layers that SpringBoot project follows
* All the 5 APIs are implemented as per the document provided and followed the specific API constraints provided in the document
* H2 Database is used which can accessed on http://localhost:8080/h2-console after running the app
* Only JSON has been used as Request and Response
* Authentication Token is implemented for the verification of API requests
* Logger is implemented for tracking the logs of code execution
* Error and Exception management is used on high-level due to less information provided about it
* Used Utility classes to separate the Utility functionality from the service/business logics
* JUnit Test cases are implemented for all the APIs in the test package
* There can be still a scope of code clean, implement any missed logic, introducing inner layers, proper error and exception management, unique Authentication & Authorization process, etc. to make the project production-level. Due to time constraint, I have focused to fulfill the most necessary constraints and deliver the assignment.


### Also, I have uploaded a video in the same drive folder of project running in my PC. The video contains calling APIs from Postman and verifying execution from logs and API responses, checking success, invalid, and error scenarios of APIs.

