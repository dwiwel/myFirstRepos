/**
  @file grabber.cpp for grabber4 (original taken from videocapture_basic.cpp
  @brief A very basic sample for using VideoCapture and VideoWriter
  @author PkLab.net  (Thank You!)
  @date Aug 24, 2016
*/
//
// Don Wiwel, 06OCT2017
// Rev:  171104. mods for raspberry pi ops.
//       171201. grabber4; using Raspberry Pi NoIR camera V2
//       171231, Working here, clean up. Adding GPIO for PIR sensor.
//       180905. Minor modes, timing, image size.
//       180916, Added Web cam, changed heartbeat image timing.
//       181028-31  Cam not displaying image, fixed.
//       181205   Working here now.
//       190325  Added headless feature.
//       190427  Testing. issues with sensor. 
//       190712  Add second IR sensor.
//       190828  motion app was causing an issue; only one a time.
//       190907  grabber6; interprocess messaging.
//       190909  Fixes if one or both cameras are disconnected.
//       190913  minor.
//       191008  Changed to using GPIO line 13 IR motion sensor.
//       191010  Trying GPIO class from book.
//       191016  Mod to AND both IO lines
//       191024  RFI issue testing.

#include <iostream>
#include <stdio.h>
#include <time.h>
#include <unistd.h>  // for sleep
#include <ctime>

#include <pthread.h>
#include <csignal>

#include <opencv2/opencv.hpp>         // opencv lib for image processing
#include <raspicam/raspicam_cv.h>     // lib for accessing raspberry pi onboard camera.
//#include <pigpio.h>                   // lib for accessing raspberry pi GPIO lines.

#include "utils.hh"
#include "SocketServer.h"


#include<iostream>
#include<unistd.h>  // for the usleep() function
#include"GPIO.h"
using namespace exploringRPi;
using namespace std;


#define SIGHUP  1   /* Hangup the process */
#define SIGINT  2   /* Interrupt the process */
#define SIGQUIT 3   /* Quit the process */
#define SIGILL  4   /* Illegal instruction. */
#define SIGTRAP 5   /* Trace trap. */
#define SIGABRT 6   /* Abort. */

using namespace cv;
using namespace std;



struct args
{
	int threadNum;
	bool run;
	String commandStr;

};





void* getCmdThread(void *passedArg) {

   struct args *threadArgs = (struct args*)(passedArg);

   int tid = threadArgs->threadNum;

   int cmdCnt = 0;
   bool serverRunning = false;



   while (threadArgs->run)
   {
	   SocketServer server(8210);      // This is for imageSend client to connect in.

	   cout << "Listening for a new control client to connect in ..." << endl;
	   int retVal = server.listen();
	   if (retVal == -1)
	   {
		   cout << "!Trouble listening server at given listed port." << endl;
		   serverRunning = false;
	   }
	   else
	   {
		   serverRunning = true;
		   cout << "Client connected." << endl;
	   }

	   cmdCnt = 0;
	   char readBuffer[1024] = "";
	   while ( (threadArgs->run) && serverRunning )
	   {
		   try
		   {
			   string rcvStr = server.receive(1024);
			   //rcvStr = rcvStr.toLowerCase();

//			   server.receive(readBuffer, 1024);
//			   rcvStr = string(readBuffer);

				//string msg = rcvStr.c_str();        // Will make a null terminate string.
				//char *writeBuffer =   msg.data();

			   if (rcvStr == "err" || rcvStr == "eof")
			   {
				   cout << "!Error while rcv msg from client, or connection closed by client." << endl;
				   break;
			   }

			   cmdCnt++;
			   //cout << "-->> Client cmd count:  " << cmdCnt << endl;
			   //cout << "Received from the client [" << rcvStr << "]" << endl;

			   string message("Ack cmd: " + rcvStr);

			   //cout << "Echo back: [" << message << "]" << endl;
			   int cnt = server.send(message);
			   if (cnt < 0)  break;                    // Some error; reset listen.

			   if (rcvStr.compare("takeimage") == 0 )  threadArgs->commandStr = "takeimage";
			   else if (rcvStr.compare("err") == 0 )   threadArgs->commandStr = "err";
			   else  threadArgs->commandStr = "\ngetCmdThread: unk command from client: " + rcvStr;


		   }
		   catch (exception *ex)
		   {
			   cout << "! Exception while reading from client. Resetting server." << endl;
			   sleep(2);
			   break;
		   }
		   sleep(1);
	   }
	   sleep(2);
   }

   cout << "getCmdThread Stopped. " << endl;
   // pthread_exit(NULL);

   int *x;  // (to avoid warning)
   return (void*)(x);

}


bool run = true;   // Main app run flag.
void raiseFlag(int signum)
{
	printf("Caught signal %d, coming out...\n", signum);
	cout << "---Stop app requested; received signal: " << signum << endl;
    run = false;
    exit(0);
}




//----------------------------------------------------------------------

int main(int argCnt, char** args)
{

    cout << "Starting my little **grabber6** program, using RPi camera and USB camera ... " << endl;
    cout << "rev: 191024+. \n" << endl;
    cout << "Uses the GPIO lines to get signal from IR sensors. Root privilege is required to run.\n" << endl;

    // opencv objects.
    //
	Mat frameCam1;        // RPi cam Current frame read; IR camera   (1280x960)     OpenCV matrices (for frames)
	Mat frameCam2;        // Web USB camera.
	Mat prevFrame;        // Previous frame.
	Mat saveFrame;        // Frame to be saved to disk.
	Mat frameCam1_bw;     // Monochrome image.
	Mat frameCam1_cor;    // Corrected image; hist eq.
	Mat frameCam2_bw;     // Monochrome image.
	Mat frameCam2_cor;    // Corrected image; hist eq.

	bool readyCam1 = false;
	bool readyCam2 = false;

	bool line13High = false;       //
	bool line19High = false;
	bool lineBothHigh =false;
	int line13cnt = 0;             // Count of consecutive line 13 highs.
	int line19cnt = 0;             // Count of consecutive line 19 highs.
	int lineBothCnt = 0;

	Utils util;

	//--- INITIALIZE VIDEOCAPTURE
	//VideoCapture cap1;
	raspicam::RaspiCam_Cv cap1;                          // Capture item, RPi on-board camera, use raspicam lib
	cv::VideoCapture cap2 =  cv::VideoCapture();         // USB Web camera.

	// open the default camera using default API
	// cap.open(0);  // for openCV
	// OR advance usage: select any API backend
	int deviceID_cam1 = 0;             // 0 = open default camera  (raspicam)
	int apiID_cam1 = cv::CAP_ANY;      // 0 = autodetect default API

	// open selected camera using selected API

	int deviceID_cam2 = 1;             // 1 = (will be USB Cam)
	int apiID_cam2 = cv::CAP_ANY;      // 0 = autodetect default API

	cout << "Starting thread to accept command messages for a client." << endl;

    pthread_t processMsgThreadId;
    struct args *threadArgs = (struct args *)( malloc(sizeof(struct args)) );

    threadArgs->threadNum = 999;
    threadArgs->run = true;          // Flag used to shutdown
    threadArgs->commandStr = "null";

    int rc = pthread_create( &processMsgThreadId, NULL, getCmdThread, (void *)(threadArgs) );

    // Initialize GPIO and image sources.
    //
	cout << "Initializing GPIO and image sources." << endl;



	GPIO inGPIO_1(13);    // pin 11 and pin 13        //#######################
	GPIO inGPIO_2(19);
    try
    {
//		if (gpioInitialise() < 0)          // #### GPIO is used for the IR motion detector (disabled at this time)
//		{
//			cout << "!! Trouble starting GPIO functions!!" << endl;
//		}
//		gpioSetMode(18, PI_INPUT);    // Line GPIO18 is for input; pull down.
//		gpioSetMode(19, PI_INPUT);    // Line GPIO19 is for input; pull down.
//
//


		inGPIO_1.setDirection(INPUT);      // basic input example
		inGPIO_2.setDirection(INPUT);      // basic input example
		cout << "The input state of line1 is: "<< inGPIO_1.getValue() << endl;
		cout << "The input state of line2 is: "<< inGPIO_2.getValue() << endl;



		util.initApp(argCnt, args);

		util.testMe();

		for (int i=0; i < argCnt; i++)
		{
			cout << "arg: " << args[i] << endl;
		}

		cap1.set( CV_CAP_PROP_FORMAT, CV_8UC3);  // CV_8UC1, CV_8UC3
		cap2.set( CV_CAP_PROP_FORMAT, CV_8UC3);  // CV_8UC1, CV_8UC3

		if (!cap1.open())  	    // Capture device raspicam
		{
			cout << "!!ERROR! Trouble opening capture device camera #1, RPi camera.\n";
			readyCam1 = false;
		}
		else readyCam1 = true;

		// check if we succeeded
		if (!cap1.isOpened()) {
			cout << "!!ERROR! Unable to open camera #1, RPi camera.\n";
			readyCam1 = false;
		}
		else readyCam1 = true;

		cap2.open(deviceID_cam2);     // for USB Web camera (a CV item).
		// check if we succeeded
		if (!cap2.isOpened()) {
			cout << "!!ERROR! Unable to open camera #2, USB camera.\n";
			readyCam2 = false;
		}
		else readyCam2 = true;

    }
    catch (cv::Exception& ex)
	{
		const char* err_msg = ex.what();
		String  msgStr = ex.msg;
		cout << "!! Some unexpected Exception during init:" << err_msg << endl;
		cout << "   msg: " << msgStr << endl;
	}

    //signal(15, raiseFlag);
    //signal(SIGINT, raiseFlag);
    //signal(2, raiseFlag);

	// Main Loop to check IR sensor and grab images.
    try
    {
		//--- GRAB AND WRITE LOOP
		cout << "\n-- Running my little grabber loop ...\n" << endl ;
			 // << "Press any key to terminate" << endl;

		time_t lastImageTime = time(NULL);
		time_t currentTime = time(NULL);

		bool useImageProc = false; // Use openCV image processing to detect movement.
		bool useIRsensor = true;   // Use IR sensor to detect movement.

		// Commands, internal or from external client.
		//
		bool sendPicPing = false;
		bool takeImageCmd = false;

		while (run)
		{
			currentTime = time(NULL);

			time_t rawtime;
			struct tm * timeinfo;
			char buffer[80];
			time (&currentTime);
			timeinfo = localtime(&currentTime);
			strftime(buffer,sizeof(buffer),"%Y%m%d_%H%M%S",timeinfo);
			std::string timestr(buffer);           // Event date-time stamp.


			// Check for incoming commands (from socket reading thread above).
			//
	        if (threadArgs->commandStr == "takeimage")
	        {
	        	takeImageCmd = true;         // Cmd rcvd to take an image.
	        	threadArgs->commandStr = ">ack takeimage";
	        	cout << " > External Command received to take image. \n";
	        }

	        if ( (timeinfo->tm_hour == 12) && ( timeinfo->tm_min == 0 ) && ( timeinfo->tm_sec == 1 ))
			{
				sendPicPing = true;
				std::cout << timestr << ": ";
				cout << " S--------------------------------- Sending noon ping image.\n";
			}
			if ( (timeinfo->tm_hour == 0) && ( timeinfo->tm_min == 0 ) && ( timeinfo->tm_sec == 1 ))
			{
				sendPicPing = true;
				std::cout << timestr << ": ";
				cout << " --------------------------------- Sending midnight ping image.\n";
			}
//
//			if (  (( timeinfo->tm_min == 0 ) && ( timeinfo->tm_sec == 0 )) || (( timeinfo->tm_min == 0 ) && ( timeinfo->tm_sec == 1 )) )
//			{
//				sendPicPing = true;
//				std::cout << timestr << ": ";
//				cout << " ---------------------- Sending hourly ping image ... \n";
//			}

			string timeDateStr_image = util.getDateTimeStr();          // Image Date Time Stamp

			try
			{
				if (readyCam1)
				{
					cap1.grab();
					cap1.retrieve(frameCam1);
				}

				if (readyCam2)
				{
					cap2.grab();
					cap2.retrieve(frameCam2);
				}
			}
			catch (cv::Exception& ex)
			{
				cout << "!! Some Exception occurred while grabbing frames ..." << endl;
				const char* err_msg = ex.what();
				String  msgStr = ex.msg;
				cout << "!! Some exception:" << err_msg << endl;
				cout << "   msg: " << msgStr << endl;
			}

			//Size size(1280, 960);
			Size size(640, 480);
			//Size size(1920, 1080);

			try
			{
				if (readyCam1) resize( frameCam1, frameCam1, size );     // ###Resizes the native frame for transmission.
			}
			catch (cv::Exception& ex)
			{
				cout << "! Exception during resize of image1. ex: " << ex.what() << endl;
				readyCam1 = false;
			}

			try
			{
				if (readyCam2) resize( frameCam2, frameCam2, size );    // ### Resizes the native frame for transmission.
			}
			catch (cv::Exception& ex )
			{
//				cout << "! Exception during resize of image2. ex: " << ex.what() << endl;
//				readyCam2 = false;
			}

			if (frameCam1.empty() )
			{
				std::cout << timestr << ": ";
				cout << "WARNING! Blank frame grabbed from Cam1; raspicam\n";
			}

			// check if we succeeded
			if (frameCam2.empty())
			{
				//std::cout << timestr << ": ";
				//cout << "WARNING! Blank frame grabbed from Cam2; Webcam. \n";
			}

			if( !util.isHeadless())                           // Only show image if monitor to be present.
			{
				if (readyCam1) imshow("Cam1 -- RPi Live", frameCam1 );
				if (readyCam2) imshow("Cam2 -- USB Live", frameCam2 );
			}

			if (useImageProc)  // Use openCV
			{
				Scalar prevStdDev, currentStdDev;
				cv::meanStdDev(prevFrame, Scalar(), prevStdDev);
				cv::meanStdDev(frameCam1, Scalar(), currentStdDev);

				std::cout << timestr << ": ";
				cout << "prevStdDev: " << prevStdDev << "   currentStdDev: " << currentStdDev << endl;

				if ( (cv::abs(currentStdDev[0] - prevStdDev[0]) > 2.5 )           // was 1.2
					|| ( sendPicPing == true ) )                        // Send image at noon.
				{
					std::cout << timestr << ": ";
					cout << "! Camera movement activity detected !" << endl;   // Save the frame.
					saveFrame = frameCam1.clone();
					if (!saveFrame.empty() )
					{
						if( !util.isHeadless()) imshow("Cam 1 -- Saved Image", saveFrame);
						util.saveImageFile( saveFrame );
						lastImageTime = time(NULL);
					}
				}
				prevFrame = frameCam1.clone();
			}

			if (useIRsensor)                  //#######
			{
				// Apply hist eq on image to cam1 image; IR Cam.
				//
				if (readyCam1)
				{
					cvtColor(frameCam1, frameCam1_bw, CV_RGB2GRAY);

					//Ptr<CLAHE> clahe = cv::createCLAHE(double clipLimit = 40.0, Size tileGridSize = Size(8, 8));
					Ptr<CLAHE> clahe = cv::createCLAHE(10.0, Size(8, 8));
					//clahe->setClipLimit( 1.5 );
					clahe->apply(frameCam1_bw, frameCam1_cor);
					//equalizeHist(frameCam1_bw, frameCam1_cor);
					if( !util.isHeadless()) imshow("Cam1 -- BW ", frameCam1_bw);
					if( !util.isHeadless()) imshow("Cam1 -- Contrast Equalization", frameCam1_cor);
				}

				// Apply hist eq on image to cam2 image; Web Cam.
				//
				if (readyCam2)
				{
					cvtColor(frameCam2, frameCam2_bw, CV_RGB2GRAY);

					//Ptr<CLAHE> clahe = cv::createCLAHE(double clipLimit = 40.0, Size tileGridSize = Size(8, 8));
					Ptr<CLAHE> clahe = cv::createCLAHE(10.0, Size(8, 8));
					//clahe->setClipLimit( 1.5 );
					clahe->apply(frameCam2_bw, frameCam2_cor);
					//equalizeHist(frameCam2_bw, frameCam2_cor);
					if( !util.isHeadless()) imshow("Cam2 -- BW ", frameCam2_bw);
					if( !util.isHeadless()) imshow("Cam2 -- Contrast Equalization", frameCam2_cor);
				}
//                                 #############################
//				line13High = gpioRead(13);   // GPIO13  IR sensor input line.  (used to trigger image save)
//				waitKey(2);
//				line19High = gpioRead(19);   // GPIO19  IR sensor input line, 2nd sensor. (currently used.)

				line13High = inGPIO_1.getValue();
				//waitKey(2);
				line19High = inGPIO_2.getValue();
				lineBothHigh = line13High && line19High;

				if(lineBothHigh)
				{
					lineBothCnt++;
					std::cout << timestr << ": ";
					cout << "-- **** Line BOTH HIGH;" << " count=" << lineBothCnt << endl;
				}
				else lineBothCnt = 0;

				if (line13High)
				{
					line13cnt++;
					std::cout << timestr << ": ";
					cout << "-- Line 13 HIGH;" << " count=" << line13cnt << endl;
				}
				if (!line13High)              // Going low resets counter.
				{
					line13cnt = 0;
					//std::cout << timestr << ": ";
					//cout << " -- Line 13 low" << endl;
				}

				if (line19High)
				{
					line19cnt++;
					std::cout << timestr << ": ";
					cout << "-- Line 19 HIGH;" << " count=" << line19cnt << endl;
				}
				if (!line19High)
				{
					line19cnt = 0;
					//std::cout << timestr << ": ";
					//cout << " -- Line 19 low" << endl;
				}

				// Take two sets for images after GPIO line13 (and/or line19) goes high.
				// On count 1 and 2, take/save images.
				//
//				if( (   (line19cnt >= 1) && (line19cnt <= 2)
//				      && (line13cnt >= 1) && (line13cnt <= 2)
//					)
				if ( ((line13cnt >= 1) && (line13cnt <= 2))
						|| sendPicPing
						|| takeImageCmd
				   )
				{
					std::cout << timestr << ": ";
					cout << "----------***************** IR motion sensor detected activity; GPIO line13 high, or commanded to take ------" << endl;
					//cout << "---------- ******** IR motion sensor detected activity or commanded to save image  ------" << endl;

					if(readyCam1) if( !util.isHeadless()) imshow(" **Cam1 -- Saved frame", frameCam1 );
					if(readyCam2) if( !util.isHeadless()) imshow(" **Cam2 -- Saved frame", frameCam2 );

					if (readyCam1)
					{
						util.saveImageFile( frameCam1, "c1", line13cnt , timeDateStr_image);
						util.saveImageFile( frameCam1_cor, "c1eq", line13cnt , timeDateStr_image);
					}
					if (readyCam2)
					{
						util.saveImageFile( frameCam2, "c2", line13cnt , timeDateStr_image);
						util.saveImageFile( frameCam2_cor, "c2eq", line13cnt , timeDateStr_image);
					}

					takeImageCmd = false;      // Internal commands has been executed; clear flags.
					sendPicPing = false;       //

				} // End if line 19 (18) cnt
			} // End if use IR sensor.

			//sleep(1);

			waitKey(300);
//			if (cv::waitKey(200) >= 0)    // 200  ms delay.  Check and take image this often.
//			{
//				break;
//			}

		}
		// End main for.
    }
    catch (cv::Exception& ex)
    {
    	const char* err_msg = ex.what();
    	String  msgStr = ex.msg;
    	cout << "!!! Some very unexpected Exception:" << err_msg << endl;
    	cout << "   msg: " << ex.msg << endl;
    	cout << "   file: " << ex.file << endl;
    	cout << "   function: " << ex.func << endl ;
    	cout << "   what: " << ex.what() << endl ;
    }

    // Shutdown
    //
    cout << "Shutting down." << endl;
    run = false;
	threadArgs->run = false;                  // Stop running threads gracefully
	pthread_join(processMsgThreadId, NULL);   // Wait here until thread stops.


   //  gpioTerminate();
    // the camera will be deinitialized automatically in VideoCapture destructor
    cout << "Little grabber stopped. End of execution.  Enjoy the day!\n";
    return 0;
}
