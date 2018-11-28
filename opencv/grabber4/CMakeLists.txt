cmake_minimum_required (VERSION 2.8)

project (grabber4)

set(raspicam_DIR "/home/pi/java_proj/gitHub/raspicam/build")

find_package( raspicam REQUIRED )
find_package( OpenCV REQUIRED )

add_executable(grabber4 grabber.cpp  utils.cpp)

target_link_libraries( grabber4 ${raspicam_CV_LIBS} pigpio )


