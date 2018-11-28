#include "mainwindow.h"
#include "ui_mainwindow.h"


#include <iostream>
#include <stdio.h>
#include <stdlib.h>

/*
#include "/usr/local/include/opencv2/opencv.hpp"
#include "/usr/local/include/opencv2/core/core.hpp"
#include "/usr/local/include/opencv2/videoio.hpp"
*/

//#include <opencv2/core.hpp>
//#include <opencv2/opencv.hpp>
//#include <opencv2/core/core.hpp>
//#include <opencv2/core/mat.hpp>
//#include <opencv2/videoio.hpp>
//#include <opencv2/imgproc.hpp>
//#include <opencv2/video/video.hpp>


MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_pushButton_1_clicked()
{
//    Mat frame;
//    //--- INITIALIZE VIDEOCAPTURE
//    VideoCapture cap;
//    // open the default camera using default API
//    cap.open(0);
//    // OR advance usage: select any API backend
//    int deviceID = 0;             // 0 = open default camera
//    int apiID = cv::CAP_ANY;      // 0 = autodetect default API
//    // open selected camera using selected API
//    cap.open(deviceID + apiID);
//    // check if we succeeded
//    if (!cap.isOpened()) {
//        cerr << "ERROR! Unable to open camera\n";
//        return -1;
//    }
//    // wait for a new frame from camera and store it into 'frame'
//    cap.read(frame);
//    // check if we succeeded
//    if (frame.empty()) {
//        cerr << "ERROR! blank frame grabbed\n";
//        break;
//    }
//    // show live and wait for a key with timeout long enough to show images
//    imshow("Live", frame);

//   QObject::connect( ui->pushButton_1, SIGNAL(pressed()),ui->Ui_MainWindow, SLOT(on_MySignal() )  );

}

void MainWindow::on_pushButton_2_clicked()
{
    std::cout << "Button2 was pressed\n";

}

void MainWindow::on_pushButton_2_clicked(bool checked)
{
  std::cout << " button 2 clicked: checked: " << checked << std::endl;
}

void MainWindow::on_pushButton_2_released()
{
    std::cout << " button 2 clicked. " << std::endl;
    ui->textBrowser->append("button 2 release");
}

void MainWindow::on_pushButton_3_clicked()
{
    std::cout << " button 3 clicked. " << std::endl;
    ui->textBrowser->append(" button 3 clicked");

}




