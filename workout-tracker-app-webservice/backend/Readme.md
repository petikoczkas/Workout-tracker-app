## Setup

[IntelliJIdea](https://www.jetbrains.com/idea/) +
[JDK](https://bell-sw.com/pages/downloads/#jdk-17-lts)

[MySQL](https://dev.mysql.com/downloads/installer/) + [Connector/J](https://dev.mysql.com/downloads/connector/j/) (Only if you want to look inside the database)

[Postman](https://app.getpostman.com/join-team?invite_code=2ccff53d8713902cc1607fbb76340f36&target_code=9befbdfa5759f3eef3ad212ad3d995b1) (This link is an invitation to a workspace with existing requests.)

## Running the server
- Open the project in IntelliJ
- Refresh Maven just in case (In project explorer on the project: right click + Maven -> Refresh)
- In terminal to start the server:
```
./mvnw spring-boot:run
```
- To stop it CTRL + C and Y

## Using the server
- Use the already existing request in Postman