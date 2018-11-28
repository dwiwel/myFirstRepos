// myTextblock.h
//


#ifndef MYTEXTBLOCK_H
#define MYTEXTBLOCK_H

#include <QObject>
#include <QWidget>
#include <QTextBrowser>

namespace Ui {
class myTextBlock;
}

class myTextBlock : public QWidget
{
    Q_OBJECT

    public:
        myTextBlock(QWidget *parent);
         ~myTextBlock();

    signals:

};

#endif // MYTEXTBLOCK_H
