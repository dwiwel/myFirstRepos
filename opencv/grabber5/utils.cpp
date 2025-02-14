// Utils.cpp
//
// Rev: 181202
//      190326
//      190716


#include <string>

#include <iostream>
#include <stdio.h>
#include <ctime>


#include <opencv2/opencv.hpp>

#include "utils.hh"

using namespace cv;
using namespace std;

		Utils::Utils()
		{
			argCnt = 0;
		    headless = false;
		}

        void Utils::testMe()
        {
            cout << "This is test msg from Utils." << endl;
        }

        void Utils::initApp()
        {	headless = false;       // default
            cout << "Utils: Initializing my little app. " << endl;
        }

        void Utils::initApp( int argCnt, char** args)
        {
        	headless = false;       // default
            cout << "Utils: Initializing my little app, with arguments. " << endl;

            for (int i=0; i < argCnt; i++)
             {
             	cout << "Utils: arg: " << args[i] << endl;

             	String argStr(args[i]);

             	cout << ">>> Arg is " + argStr << endl;

             	if (argStr == "headless")
                {
                	headless = true;
                	cout << " Headless -- Operating without a monitor." << endl;
                }

             }
        }

        bool Utils::isHeadless()
        {
        	return headless;

        }

        void Utils::saveImageFile(Mat image)
        {
            string filename = "/data/images/img_" + getDateTimeStr() + "_.jpg";

            if(image.empty())
            {
              std::cout << "WARNING: Trying to save image, but frame is empty." << std::endl;
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
              std::cout << "!SaveImageFile: Something is wrong with image; image emtpy: " << filename << std::endl;
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


