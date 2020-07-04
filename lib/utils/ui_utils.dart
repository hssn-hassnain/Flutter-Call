import "package:flutter/widgets.dart";
import 'package:privacee/enums/device_screen_type.dart';

DeviceScreenType getDeviceType(MediaQueryData mediaQuery) {
  double deviceWidget = mediaQuery.size.shortestSide;
  if (deviceWidget > 950) {
    return DeviceScreenType.Desktop;
  }
  if (deviceWidget > 600) {
    return DeviceScreenType.Tablet;
  }
  return DeviceScreenType.Mobile;
}
