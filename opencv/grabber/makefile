#
# This make file will use older version of opencv files; those that are found by pkg-config  (oh-no; grabber wont run right. )

CLFLAGS = `pkg-config --cflags opencv`
LIBS = `pkg-config --libs opencv`

% : %.cpp
	g++  -o $@ $<  $(CFLAGS) $(LIBS)

