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
//       180916, Added Web cam, changed heartbeat image timeing.
//       181028-31  Cam not displaying image, fixed.
//       181205   Working here now.
//       190325  Added headless feature.
//       190427  Testing. issues with sensor. 
//       190712  Add second IR sensor.


#include <iostream>
#include <stdio.h>
#include <time.h>
#include <unistd.h>  // for sleep
#include <ctime>

#include <opencv2/opencv.hpp>
#include <raspicam/raspicam_cv.h>
#include <pigpio.h>

#include "utils.hh"

using namespace cv;
using namespace std;

int main(int argCnt, char** args)
{

    cout << "Starting my little grabber4 program, using RPi camera and USB camera ... " << endl;
    cout << "rev: 190427, 190503, 190712 \n" << endl;
    cout << "Uses the GPIO to get signal from IR sensor. Root privilege is required to run.\n" << endl;

	Mat frameCam1;        // RPi cam Current frame read.   (1280x960)     OpenCV matrices (for frames)
	Mat frameCam2;        // Web USB camera.
	Mat prevFrame;        // Previous frame.
	Mat saveFrame;        // Frame to be saved to disk.
	Mat frameCam1_bw;     // Monochrome image.
	Mat frameCam1_cor;    // Corrected image; hist eq.
	Mat frameCam2_bw;     // Monochrome image.
	Mat frameCam2_cor;    // Corrected image; hist eq.


	bool readyCam1 = true;
	bool readyCam2 = true;

	bool line13High = false;      //
	bool line19High = false;
	int line13cnt = 0;             // Count of consecutive line 13 highs.
	int line19cnt = 0;             // Count of consecutive line 19 highs.

	Utils util;

	//--- INITIALIZE VIDEOCAPTURE
	//VideoCapture cap1;
	raspicam::RaspiCam_Cv cap;                          // Capture item, RPi on-board camera.
	cv::VideoCapture cap2 =  cv::VideoCapture();        // USB Web camera.

	// open the default camera using default API
	// cap.open(0);  // for openCV
	// OR advance usage: select any API backend
	int deviceID = 0;             // 0 = open default camera
	int apiID = cv::CAP_ANY;      // 0 = autodetect default API

	// open selected camera using selected API

	int deviceID_cam2 = 0;             // 0 = open default camera
	int apiID_cam2 = cv::CAP_ANY;      // 0 = autodetect default API

    // Initialize GPIO and image sources.
    //
	cout << "Initializing GPIO and image sources." << endl;

    try
    {
		if (gpioInitialise() < 0)          // GPIO is used for the IR motion detector (disabled at this time)
		{
			cout << "!! Trouble starting GPIO functions!!" << endl;
		}
		gpioSetMode(18, PI_INPUT);    // Line GPIO18 is for input; pull down.
		gpioSetMode(19, PI_INPUT);    // Line GPIO19 is for input; pull down.

		util.initApp(argCnt, args);

		util.testMe();

		for (int i=0; i < argCnt; i++)
		{
			cout << "arg: " << args[i] << endl;

		}

		cap.set( CV_CAP_PROP_FORMAT, CV_8UC3);  // CV_8UC1, CV_8UC3
		cap2.set( CV_CAP_PROP_FORMAT, CV_8UC3);  // CV_8UC1, CV_8UC3

		if (!cap.open() )  	    // Capture device raspicam
		{
			cout << "!!ERROR! Trouble opening capture device camera #1, RPi camera.\n";
			readyCam1 = false;
		}




		// check if we succeeded
//		if (!cap.isOpened()) {
//			cout << "ERROR! Unable to open camera #1, RPi camera.\n";
//			readyCam1 = false;
//		}

		cap2.open(deviceID_cam2);     // for USB Web camera (a CV item).
		// check if we succeeded
		if (!cap2.isOpened()) {
			cout << "!!ERROR! Unable to open camera #2, USB camera.\n";
			readyCam2 = false;
		}
    }
    catch (cv::Exception& ex)
        {
        	const char* err_msg = ex.what();
        	String  msgStr = ex.msg;
        	cout << "!! Some unexpected Exception during init:" << err_msg << endl;
        	cout << "   msg: " << msgStr << endl;
        }



	// Loop to check IR sensor and grab images.
    try
    {

		//--- GRAB AND WRITE LOOP
		cout << "\n-- Running my little grabber loop ...\n" << endl ;
			 // << "Press any key to terminate" << endl;

		time_t lastImageTime = time(NULL);
		time_t currentTime = time(NULL);


		bool useImageProc = false;
		bool useIRsensor = true;   // sensor is too sensitive at the moment; not using now.


		for (;;)
		{
			currentTime = time(NULL);

			time_t rawtime;
			struct tm * timeinfo;
			char buffer[80];
			time (&currentTime);
			timeinfo = localtime(&currentTime);
			strftime(buffer,sizeof(buffer),"%Y%m%d_%H%M%S",timeinfo);
			std::string timestr(buffer);
		   // std::cout << timestr << ": ";

			bool sendPicPing = false;
			if ( (timeinfo->tm_hour == 12) && ( timeinfo->tm_min == 0 ) && ( timeinfo->tm_sec == 1 ))
			{
				sendPicPing = true;
				std::cout << timestr << ": ";
				cout << " Sending noon ping image.\n";
			}
			if ( (timeinfo->tm_hour == 0) && ( timeinfo->tm_min == 0 ) && ( timeinfo->tm_sec == 1 ))
			{
				sendPicPing = true;
				std::cout << timestr << ": ";
				cout << " Sending midnight ping image.\n";
			}

			if (readyCam1)
			{
				cap.grab();
				cap.retrieve(frameCam1);
			}

			if (readyCam2)
			{
				cap2.grab();
				cap2.retrieve(frameCam2);
			}


			//Size size(1280, 960);
			Size size(640, 480);
			//Size size(1920, 1080);
			if (readyCam1) resize( frameCam1, frameCam1, size );     // Resizes the native frame for transmission.
			if (readyCam2) resize( frameCam2, frameCam2, size );

			// show live and wait for a key with timeout long enough to show images
			if (!frameCam1.empty() )
			{
				// This is good.
			}

			// check if we succeeded
			if (frameCam1.empty())
			{
//				std::cout << timestr << ": ";
//				cout << "ERROR! Blank frame grabbed from Cam1\n";
				//break;
			}

			if( !util.isHeadless())
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

			if (useIRsensor)
			{
				// Apply hist eq on image.
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

				line13High = gpioRead(13);   // GPIO13  IR sensor input line

				if (line13High)
				{
					line13cnt++;
					std::cout << timestr << ": ";
					cout << "-- Line 13 HIGH" << endl;
				}
				if (!line13High)
				{
					line13cnt = 0;
					//std::cout << timestr << ": ";
					//cout << " -- Line 13 low" << endl;
				}

				int line19High = gpioRead(19);   // GPIO19  IR sensor input line, 2nd sensor.

				if (line19High)
				{
					line13cnt++;
					std::cout << timestr << ": ";
					cout << "-- Line 19 HIGH" << endl;
				}
				if (!line19High)
				{
					line19cnt = 0;
					//std::cout << timestr << ": ";
					//cout << " -- Line 19 low" << endl;
				}


				if( ((line13cnt >= 1) && (line13cnt <= 2)) || sendPicPing )   // Take two sets for images
				//if (line18high  || sendPicPing )
				{
					std::cout << timestr << ": ";
					cout << "! IR motion sensor detected activity !" << endl;
					if(readyCam2) if( !util.isHeadless()) imshow(" **Cam2 -- Saved frame", frameCam2 );

					string timeDateStr = util.getDateTimeStr();
					util.saveImageFile( frameCam1, "c1", line13cnt , timeDateStr);

					if(readyCam2) util.saveImageFile( frameCam2, "c2", line13cnt , timeDateStr);
					if(readyCam2) util.saveImageFile( frameCam2_cor, "c2eq", line13cnt , timeDateStr);
				} // End if line 18 cnt
			} // End if use IR sensor.

			//sleep(1);

			waitKey(200);
	//		if (cv::waitKey(500) >= 0)    // 1000  ms delay.
	//			break;
	//		}
		} // End main for.
    }
    catch (cv::Exception& ex)
    {
    	const char* err_msg = ex.what();
    	String  msgStr = ex.msg;
    	cout << "!! Some unexpected Exception:" << err_msg << endl;
    	cout << "   msg: " << msgStr << endl;
    }

    gpioTerminate();
    // the camera will be deinitialized automatically in VideoCapture destructor
    cout << "Little grabber stopped. End of execution.  Enjoy the day!\n";
    return 0;
}
