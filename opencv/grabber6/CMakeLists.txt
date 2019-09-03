cmake_minimum_required (VERSION 2.8)

project (grabber6)

set(raspicam_DIR "/home/pi/java_proj/gitHub/raspicam/build")
set(PLATFORM_SPECIFIC_LIBS "-lpthread")

INCLUDE(FindFreetype)
INCLUDE_DIRECTORIES(${FREETYPE_INCLUDE_DIRS})

find_package( raspicam REQUIRED )
find_package( OpenCV REQUIRED )

add_executable(grabber6  grabber.cpp utils.cpp SocketServer.cpp )

#target_link_libraries( grabber6 ${raspicam_CV_LIBS} pigpio pthread ${FREETYPE_LIBRARIES} )
target_link_libraries( grabber6 ${raspicam_CV_LIBS} pigpio pthread  )


