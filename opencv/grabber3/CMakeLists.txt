cmake_minimum_required (VERSION 2.6)

project (grabber3)
find_package( OpenCV REQUIRED )

add_executable(grabber3 grabber.cpp  utils.cpp)

TARGET_LINK_LIBRARIES( grabber3  ${OpenCV_LIBS} )


