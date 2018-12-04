// Utils.cpp
//
// Rev: 181202


#include <string>

#include <iostream>
#include <stdio.h>
#include <ctime>


#include <opencv2/opencv.hpp>

#include "utils.hh"

using namespace cv;
using namespace std;


        void Utils::testMe()
        {
            cout << "This is test msg from Utils." << endl;
        }

        void Utils::initApp()
        {
            cout << "Initializing my little app. " << endl;
        }

        void Utils::saveImageFile(Mat image)
        {
            string filename = "/data/images/img_" + getDateTimeStr() + "_.jpg";

            if(image.empty())
            {
              std::cerr << "Something is wrong with the webcam, could not get frame." << std::endl;
              return;
            }
            // Save the frame into a file
            imwrite(filename, image); // A JPG FILE IS BEING SAVED
        }

        void Utils::saveImageFile(Mat image, string suffix, int eventCnt, string dateTimeStr)
        {
        	char cntStr[16];

        	sprintf( cntStr, "%d", eventCnt);
            string filename = "/data/images/img_" + dateTimeStr + "_"
            		+ cntStr + "_" + suffix + "_.jpg";

            if(image.empty())
            {
              std::cerr << "Something is wrong with the webcam, could not get frame." << std::endl;
              return;
            }
            cout << "Saving image file: " + filename << endl;
            // Save the frame into a file
            imwrite(filename, image); // A JPG FILE IS BEING SAVED
        }


        string Utils::getDateTimeStr()
        {
            time_t rawtime;
            struct tm * timeinfo;
            char buffer[80];

            time (&rawtime);
            timeinfo = localtime(&rawtime);

            //strftime(buffer,sizeof(buffer),"%d-%m-%Y %I:%M:%S",timeinfo);
            strftime(buffer,sizeof(buffer),"%Y%m%d_%H%M%S",timeinfo);
            std::string str(buffer);            
            std::cout << str << endl;

          return str;
        }


