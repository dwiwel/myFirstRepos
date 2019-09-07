/*
 * SocketServer.cpp
 *
 *  Created on: Sep 2, 2019
 *      Author: root
 *  Rev: 20190902  Taken from Exploring RPi, chapter 12, clientServer
 */


#include "SocketServer.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
using namespace std;


SocketServer::SocketServer(int portNumber) {
	this->socketfd = -1;
	this->clientSocketfd = -1;
	this->portNumber = portNumber;
	this->clientConnected = false;
}

int SocketServer::listen(){
    this->socketfd = socket(AF_INET, SOCK_STREAM, 0);
    if (this->socketfd < 0){
    	perror("Socket Server: error opening socket.\n");
    	return -1;
    }
    bzero((char *) &serverAddress, sizeof(serverAddress));
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_addr.s_addr = INADDR_ANY;
    serverAddress.sin_port = htons(this->portNumber);
    if (bind(socketfd, (struct sockaddr *) &serverAddress, sizeof(serverAddress)) < 0){
    	perror("Socket Server: error on binding the socket.\n");
    	return -1;
    }
    ::listen(this->socketfd, 5);
    socklen_t clientLength = sizeof(this->clientAddress);
    this->clientSocketfd = accept(this->socketfd,
    		(struct sockaddr *) &this->clientAddress,
    		&clientLength);
    if (this->clientSocketfd < 0){
    	perror("Socket Server: Failed to bind the client socket properly.\n");
    	return -1;
    }
    return 0;
}

int SocketServer::send(std::string message){

	//string msg = message.c_str();  // Will make a null terminated string.
	char *writeBuffer =  message.data();

	int length = message.length();
    int n = write(this->clientSocketfd, writeBuffer, length+1);
    if (n < 0){
       perror("Socket Server: error writing to server socket.");
       return n;
    }
    return n;
}



string SocketServer::receive(int size=1024)
{
    char readBuffer[size];
    memset (readBuffer, 0, size);

    int n = read(this->clientSocketfd, readBuffer, size);

    if (n < 0){
       perror("Socket Server: error reading from server socket.");
       return ("ERR");
    }
    else if (n == 0){
	  return ("NULL");
	}
    return string(readBuffer);
}


// may not work.  added later.
int SocketServer::receive(char *readBuffer, int size=1024)
{
	memset (readBuffer, 0, size);
    int n = read(this->clientSocketfd, readBuffer, sizeof(readBuffer));

    if (n < 0){
       perror("SocketServer::receive: error reading from server socket.");
       return -1;
    }
    else if (n == 0){
	  return 0;
	}
    return n;
}



SocketServer::~SocketServer() {
	close(this->socketfd);
	close(this->clientSocketfd);
}


