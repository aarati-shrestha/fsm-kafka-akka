It is spring-boot application. It has used the concept of finite state machine, akka and kafka.

The application has two states:
	1. IDLE 2. ACTIVE

Initially the application is in IDLE state where it does nothing. As soon as /sample api is hit with json data, the application goes in active state where it produces the message using akka actor in the kafka topic "tests" which we can see in terminal. After producing, it listens to the kafka topic "consumertopics" where it prints the data whenever it has json string with "requestId" key is presented and it has value of "12345"
