#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private slots:
    void on_pushButton_1_clicked();

    void on_pushButton_2_clicked();

    void on_pushButton_2_clicked(bool checked);

    void on_pushButton_2_released();

    void on_pushButton_3_clicked();


signals:


private:
    Ui::MainWindow *ui;
};

#endif // MAINWINDOW_H
