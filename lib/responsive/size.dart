

import 'package:flutter/widgets.dart';



class Size {
    static double height;
    static double width;
    static double size;


    void init(BuildContext context) {
    height = MediaQuery.of(context).size.height;
    width = MediaQuery.of(context).size.width;
    size = MediaQuery.of(context).orientation == Orientation.portrait
    ?height
    :width;
    }

    static double textSize(double r){
      return size * r ;
    }

    static double titleSize(double r){
      return size * r ; 
    }

    static double heightSize(double r){
      return height * r;
    }

    static double widthSize(double r){
      return width * r;
    }
}

