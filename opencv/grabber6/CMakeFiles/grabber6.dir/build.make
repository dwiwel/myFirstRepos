# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.6

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6

# Include any dependencies generated for this target.
include CMakeFiles/grabber6.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/grabber6.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/grabber6.dir/flags.make

CMakeFiles/grabber6.dir/grabber.cpp.o: CMakeFiles/grabber6.dir/flags.make
CMakeFiles/grabber6.dir/grabber.cpp.o: grabber.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/grabber6.dir/grabber.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/grabber6.dir/grabber.cpp.o -c /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/grabber.cpp

CMakeFiles/grabber6.dir/grabber.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/grabber6.dir/grabber.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/grabber.cpp > CMakeFiles/grabber6.dir/grabber.cpp.i

CMakeFiles/grabber6.dir/grabber.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/grabber6.dir/grabber.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/grabber.cpp -o CMakeFiles/grabber6.dir/grabber.cpp.s

CMakeFiles/grabber6.dir/grabber.cpp.o.requires:

.PHONY : CMakeFiles/grabber6.dir/grabber.cpp.o.requires

CMakeFiles/grabber6.dir/grabber.cpp.o.provides: CMakeFiles/grabber6.dir/grabber.cpp.o.requires
	$(MAKE) -f CMakeFiles/grabber6.dir/build.make CMakeFiles/grabber6.dir/grabber.cpp.o.provides.build
.PHONY : CMakeFiles/grabber6.dir/grabber.cpp.o.provides

CMakeFiles/grabber6.dir/grabber.cpp.o.provides.build: CMakeFiles/grabber6.dir/grabber.cpp.o


CMakeFiles/grabber6.dir/utils.cpp.o: CMakeFiles/grabber6.dir/flags.make
CMakeFiles/grabber6.dir/utils.cpp.o: utils.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building CXX object CMakeFiles/grabber6.dir/utils.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/grabber6.dir/utils.cpp.o -c /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/utils.cpp

CMakeFiles/grabber6.dir/utils.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/grabber6.dir/utils.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/utils.cpp > CMakeFiles/grabber6.dir/utils.cpp.i

CMakeFiles/grabber6.dir/utils.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/grabber6.dir/utils.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/utils.cpp -o CMakeFiles/grabber6.dir/utils.cpp.s

CMakeFiles/grabber6.dir/utils.cpp.o.requires:

.PHONY : CMakeFiles/grabber6.dir/utils.cpp.o.requires

CMakeFiles/grabber6.dir/utils.cpp.o.provides: CMakeFiles/grabber6.dir/utils.cpp.o.requires
	$(MAKE) -f CMakeFiles/grabber6.dir/build.make CMakeFiles/grabber6.dir/utils.cpp.o.provides.build
.PHONY : CMakeFiles/grabber6.dir/utils.cpp.o.provides

CMakeFiles/grabber6.dir/utils.cpp.o.provides.build: CMakeFiles/grabber6.dir/utils.cpp.o


CMakeFiles/grabber6.dir/SocketServer.cpp.o: CMakeFiles/grabber6.dir/flags.make
CMakeFiles/grabber6.dir/SocketServer.cpp.o: SocketServer.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building CXX object CMakeFiles/grabber6.dir/SocketServer.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/grabber6.dir/SocketServer.cpp.o -c /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/SocketServer.cpp

CMakeFiles/grabber6.dir/SocketServer.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/grabber6.dir/SocketServer.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/SocketServer.cpp > CMakeFiles/grabber6.dir/SocketServer.cpp.i

CMakeFiles/grabber6.dir/SocketServer.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/grabber6.dir/SocketServer.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/SocketServer.cpp -o CMakeFiles/grabber6.dir/SocketServer.cpp.s

CMakeFiles/grabber6.dir/SocketServer.cpp.o.requires:

.PHONY : CMakeFiles/grabber6.dir/SocketServer.cpp.o.requires

CMakeFiles/grabber6.dir/SocketServer.cpp.o.provides: CMakeFiles/grabber6.dir/SocketServer.cpp.o.requires
	$(MAKE) -f CMakeFiles/grabber6.dir/build.make CMakeFiles/grabber6.dir/SocketServer.cpp.o.provides.build
.PHONY : CMakeFiles/grabber6.dir/SocketServer.cpp.o.provides

CMakeFiles/grabber6.dir/SocketServer.cpp.o.provides.build: CMakeFiles/grabber6.dir/SocketServer.cpp.o


CMakeFiles/grabber6.dir/GPIO.cpp.o: CMakeFiles/grabber6.dir/flags.make
CMakeFiles/grabber6.dir/GPIO.cpp.o: GPIO.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Building CXX object CMakeFiles/grabber6.dir/GPIO.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/grabber6.dir/GPIO.cpp.o -c /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/GPIO.cpp

CMakeFiles/grabber6.dir/GPIO.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/grabber6.dir/GPIO.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/GPIO.cpp > CMakeFiles/grabber6.dir/GPIO.cpp.i

CMakeFiles/grabber6.dir/GPIO.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/grabber6.dir/GPIO.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/GPIO.cpp -o CMakeFiles/grabber6.dir/GPIO.cpp.s

CMakeFiles/grabber6.dir/GPIO.cpp.o.requires:

.PHONY : CMakeFiles/grabber6.dir/GPIO.cpp.o.requires

CMakeFiles/grabber6.dir/GPIO.cpp.o.provides: CMakeFiles/grabber6.dir/GPIO.cpp.o.requires
	$(MAKE) -f CMakeFiles/grabber6.dir/build.make CMakeFiles/grabber6.dir/GPIO.cpp.o.provides.build
.PHONY : CMakeFiles/grabber6.dir/GPIO.cpp.o.provides

CMakeFiles/grabber6.dir/GPIO.cpp.o.provides.build: CMakeFiles/grabber6.dir/GPIO.cpp.o


# Object files for target grabber6
grabber6_OBJECTS = \
"CMakeFiles/grabber6.dir/grabber.cpp.o" \
"CMakeFiles/grabber6.dir/utils.cpp.o" \
"CMakeFiles/grabber6.dir/SocketServer.cpp.o" \
"CMakeFiles/grabber6.dir/GPIO.cpp.o"

# External object files for target grabber6
grabber6_EXTERNAL_OBJECTS =

grabber6: CMakeFiles/grabber6.dir/grabber.cpp.o
grabber6: CMakeFiles/grabber6.dir/utils.cpp.o
grabber6: CMakeFiles/grabber6.dir/SocketServer.cpp.o
grabber6: CMakeFiles/grabber6.dir/GPIO.cpp.o
grabber6: CMakeFiles/grabber6.dir/build.make
grabber6: /opt/vc/lib/libmmal_core.so
grabber6: /opt/vc/lib/libmmal_util.so
grabber6: /opt/vc/lib/libmmal.so
grabber6: /usr/local/lib/libopencv_stitching.so.3.3.0
grabber6: /usr/local/lib/libopencv_superres.so.3.3.0
grabber6: /usr/local/lib/libopencv_videostab.so.3.3.0
grabber6: /usr/local/lib/libopencv_aruco.so.3.3.0
grabber6: /usr/local/lib/libopencv_bgsegm.so.3.3.0
grabber6: /usr/local/lib/libopencv_bioinspired.so.3.3.0
grabber6: /usr/local/lib/libopencv_ccalib.so.3.3.0
grabber6: /usr/local/lib/libopencv_dpm.so.3.3.0
grabber6: /usr/local/lib/libopencv_face.so.3.3.0
grabber6: /usr/local/lib/libopencv_freetype.so.3.3.0
grabber6: /usr/local/lib/libopencv_fuzzy.so.3.3.0
grabber6: /usr/local/lib/libopencv_img_hash.so.3.3.0
grabber6: /usr/local/lib/libopencv_line_descriptor.so.3.3.0
grabber6: /usr/local/lib/libopencv_optflow.so.3.3.0
grabber6: /usr/local/lib/libopencv_reg.so.3.3.0
grabber6: /usr/local/lib/libopencv_rgbd.so.3.3.0
grabber6: /usr/local/lib/libopencv_saliency.so.3.3.0
grabber6: /usr/local/lib/libopencv_stereo.so.3.3.0
grabber6: /usr/local/lib/libopencv_structured_light.so.3.3.0
grabber6: /usr/local/lib/libopencv_surface_matching.so.3.3.0
grabber6: /usr/local/lib/libopencv_tracking.so.3.3.0
grabber6: /usr/local/lib/libopencv_xfeatures2d.so.3.3.0
grabber6: /usr/local/lib/libopencv_ximgproc.so.3.3.0
grabber6: /usr/local/lib/libopencv_xobjdetect.so.3.3.0
grabber6: /usr/local/lib/libopencv_xphoto.so.3.3.0
grabber6: /usr/local/lib/libopencv_shape.so.3.3.0
grabber6: /usr/local/lib/libopencv_photo.so.3.3.0
grabber6: /usr/local/lib/libopencv_calib3d.so.3.3.0
grabber6: /usr/local/lib/libopencv_phase_unwrapping.so.3.3.0
grabber6: /usr/local/lib/libopencv_dnn.so.3.3.0
grabber6: /usr/local/lib/libopencv_video.so.3.3.0
grabber6: /usr/local/lib/libopencv_datasets.so.3.3.0
grabber6: /usr/local/lib/libopencv_plot.so.3.3.0
grabber6: /usr/local/lib/libopencv_text.so.3.3.0
grabber6: /usr/local/lib/libopencv_features2d.so.3.3.0
grabber6: /usr/local/lib/libopencv_flann.so.3.3.0
grabber6: /usr/local/lib/libopencv_highgui.so.3.3.0
grabber6: /usr/local/lib/libopencv_ml.so.3.3.0
grabber6: /usr/local/lib/libopencv_videoio.so.3.3.0
grabber6: /usr/local/lib/libopencv_imgcodecs.so.3.3.0
grabber6: /usr/local/lib/libopencv_objdetect.so.3.3.0
grabber6: /usr/local/lib/libopencv_imgproc.so.3.3.0
grabber6: /usr/local/lib/libopencv_core.so.3.3.0
grabber6: CMakeFiles/grabber6.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/CMakeFiles --progress-num=$(CMAKE_PROGRESS_5) "Linking CXX executable grabber6"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/grabber6.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/grabber6.dir/build: grabber6

.PHONY : CMakeFiles/grabber6.dir/build

CMakeFiles/grabber6.dir/requires: CMakeFiles/grabber6.dir/grabber.cpp.o.requires
CMakeFiles/grabber6.dir/requires: CMakeFiles/grabber6.dir/utils.cpp.o.requires
CMakeFiles/grabber6.dir/requires: CMakeFiles/grabber6.dir/SocketServer.cpp.o.requires
CMakeFiles/grabber6.dir/requires: CMakeFiles/grabber6.dir/GPIO.cpp.o.requires

.PHONY : CMakeFiles/grabber6.dir/requires

CMakeFiles/grabber6.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/grabber6.dir/cmake_clean.cmake
.PHONY : CMakeFiles/grabber6.dir/clean

CMakeFiles/grabber6.dir/depend:
	cd /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6 && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6 /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6 /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6 /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6 /home/pi/java_proj/gitHub/myFirstRepos/opencv/grabber6/CMakeFiles/grabber6.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/grabber6.dir/depend

