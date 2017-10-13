#include "mytextblock.h"

#include <iostream>
#include <stdio.h>
#include <stdlib.h>



myTextBlock::myTextBlock(QWidget *parent) :
{


}

void myTextBlock::on_MySignal()
{
  std::cout << " In Slot myTextBlock::on_MySignal.  " << std::endl;
}


