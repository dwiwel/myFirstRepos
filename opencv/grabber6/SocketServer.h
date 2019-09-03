/*
 * SocketServer.h
 *
 *  Created on: Sep 2, 2019
 *      Author: root
 */

#ifndef SOCKETSERVER_H_
#define SOCKETSERVER_H_



#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <string>



class SocketServer {
private:
   int     portNumber;
   int     socketfd, clientSocketfd;
   struct  sockaddr_in   serverAddress;
   struct  sockaddr_in   clientAddress;
   bool	   clientConnected;

public:
   SocketServer(int portNumber);
   virtual int listen();
   virtual int send(std::string message);
   virtual std::string receive(int size);
   virtual ~SocketServer();
};




#endif /* SOCKETSERVER_H_ */
