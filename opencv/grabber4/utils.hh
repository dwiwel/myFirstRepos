// utils.hh   Header file for Utils class. 
//
// Don Wiwel, 05OCT2017
// Rev:



#ifndef __UTIL_HH
#define __UTIL_HH

#include <iostream>
#include <stdio.h>
#include <string>
#include <opencv2/opencv.hpp>


using namespace cv;
using namespace std;


class Utils
{
    public:

	    Utils();
        void initApp();
        void initApp( int argCnt, char** args);
        void saveImageFile( Mat image );
        void saveImageFile(Mat image, string suffix, int eventCnt, string dateTimeStr);
        string getDateTimeStr();
        void testMe();
        bool isHeadless();

    private:
		int argCnt;
	    bool headless;


};

#endif  // __UTIL_HH
