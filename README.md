bank
====

Bank project for vesys (distributed systems) module

The goal of this project is to establish a connection between a client and a server using various techniques:

- [x] [Sockets](https://github.com/RTiK/bank/tree/master/src/bank/socket)
- [ ] HTTP
- [x] [SOAP](https://www.github.com/RTiK/bank/tree/master/src/bank/soap)
- [x] [REST](https://github.com/RTiK/bank/tree/master/src/bank/rest)
- [x] [RMI](https://github.com/RTiK/bank/tree/master/src/bank/rmi)
- [x] [JMS](https://github.com/RTiK/bank/tree/master/src/bank/jms)
- [x] [WebSockets](https://github.com/RTiK/bank/tree/master/src/bank/websockets)


To start the connection the server must be started first. The client class must be then configured to use the coresponding driver by setting the path in the delpoyment arguments.

Note to future classes: As you can see not all exercies have been made and the implementations are not perfect but they can be used as an inspiration and push you in the right direction.
