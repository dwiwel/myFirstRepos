#-------------------------------------------------
#
# Project created by QtCreator 2017-10-03T10:59:15
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = qtProj
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    mytextblock.cpp

HEADERS  += mainwindow.h \
    mytextblock.h

FORMS    += mainwindow.ui



#INCLUDEPATH += -i /usr/include/ -i /usr/local/include -i /usr/include/c++/6/tr1/

#LIBS += -L /usr/lib/local


#CONFIG = `pkg-config --cflags --libs opencv`
