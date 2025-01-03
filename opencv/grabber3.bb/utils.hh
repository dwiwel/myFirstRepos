// utils.hh   Header file for Utils class. 
//
// Don Wiwel, 05OCT2017
// Rev:



#ifndef __UTIL_HH
#define __UTIL_HH

#include <iostream>
#include <stdio.h>
#include <opencv2/opencv.hpp>


using namespace cv;
using namespace std;


class Utils
{
    public:
        static void initApp();
        static void saveImageFile( Mat image );
        static string getDateTimeStr();
        static void testMe();
};

#endif  // __UTIL_HH
