#ifndef MYTEXTBLOCK_H
#define MYTEXTBLOCK_H

#include <QObject>
#include <QWidget>
#include <QTextBrowser>

namespace Ui {
class myTextBlock;
}

class myTextBlock : public QTextBrowser
{
    Q_OBJECT

    public:
        myTextBlock(QWidget *parent);
         ~myTextBlock();

    signals:
        void on_MySignal();
};

#endif // MYTEXTBLOCK_H
