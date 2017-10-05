/**
  @file grabber.cpp  (taken from videocapture_basic.cpp
  @brief A very basic sample for using VideoCapture and VideoWriter
  @author PkLab.net
  @date Aug 24, 2016
*/
//
// Don Wiwel, 05OCT2017
// Rev:

#include <iostream>
#include <stdio.h>
#include <opencv2/opencv.hpp>

#include "utils.hh"

using namespace cv;
using namespace std;

int main(int, char**)
{
    cout << "Starting grabber test program ...\n" << endl;

    Utils meUtils;

    meUtils.initApp();

    Utils::testMe();

    Mat frame;            // Current (last) frame read.
    Mat prevFrame;
    Mat saveFrame;        // Frame to be saved to disk.

    //--- INITIALIZE VIDEOCAPTURE
    VideoCapture cap;
    // open the default camera using default API
    //cap.open(0);
    // OR advance usage: select any API backend
    int deviceID = 0;             // 0 = open default camera
    int apiID = cv::CAP_ANY;      // 0 = autodetect default API
    // open selected camera using selected API
    cap.open(deviceID + apiID);
    // check if we succeeded
    if (!cap.isOpened()) {
        cerr << "ERROR! Unable to open camera\n";      
        return -1;
    }

    //--- GRAB AND WRITE LOOP
    cout << "Start my little grabber ..." 
         << "Press any key to terminate" << endl;
    for (;;)
    {
        // wait for a new frame from camera and store it into 'frame'
        cap.read(frame);

        // check if we succeeded
        if (frame.empty()) {
            cout << "ERROR! blank frame grabbed\n";
            break;
        }
        // show live and wait for a key with timeout long enough to show images
        if (!frame.empty() ) imshow("Live", frame);

        if (!prevFrame.empty() ) imshow("PrevFrame", prevFrame);
    
        Scalar prevStdDev, currentStdDev;
        cv::meanStdDev(prevFrame, Scalar(), prevStdDev);
        cv::meanStdDev(frame, Scalar(), currentStdDev);

        cout << "prevStdDev: " << prevStdDev << "   currentStdDev: " << currentStdDev << endl;    

        
        if ( cv::abs(currentStdDev[1] - prevStdDev[1]) > 4 )
        {
            cout << "! Activity detected !" << endl;
            saveFrame = frame.clone();
            if (!saveFrame.empty() )
            { 
                imshow("saveFrame", saveFrame);
                Utils::saveImageFile( saveFrame );
            }
        } 
        

        if (cv::waitKey(100) >= 0) 
        {
            break;
        }
        
        prevFrame = frame.clone();
    }
    // the camera will be deinitialized automatically in VideoCapture destructor
    cout << "Little grabber stopped.  Enjoy the day!\n";
    return 0;
}
