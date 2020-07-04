import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:privacee/SizeConfig.dart';
import 'package:privacee/UI/Call/calllog.dart';
import 'package:privacee/Services/themes.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

Map<PermissionGroup, PermissionStatus> permissions;

void getPermission() async {
  permissions = await PermissionHandler().requestPermissions([
    PermissionGroup.phone,
    PermissionGroup.storage,
    PermissionGroup.microphone,
    PermissionGroup.contacts,
    PermissionGroup.sms,
  ]);
}

void main() async {
  runApp(
    //DevicePreview(builder: (context) =>
    MyApp(),
  );
}

class SplashScreen extends StatefulWidget {
  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  startTime() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    bool firstTime = prefs.getBool('first_time');

    if (firstTime != null && !firstTime) {
      // Not first time
      return navigationPageHome();
    } else {
      // First time
      prefs.setBool('first_time', false);
      return navigationPageWel();
    }
  }

  void navigationPageHome() {
    Navigator.of(context).pushReplacementNamed('/AppLockPage');
  }

  void navigationPageWel() {
    Navigator.of(context).pushReplacementNamed('/SignUpPage');
  }

  @override
  void initState() {
    super.initState();
    getPermission();
    startTime();
  }

  @override
  Widget build(BuildContext context) {
    return Container();
  }
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  //  @override
  // bool get wantKeepAlive => true;
  Map<PermissionGroup, PermissionStatus> permissions;
  @override
  void initState() {
    super.initState();
    getPermission();
  }

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(builder: (context, constraints) {
      return OrientationBuilder(builder: (context, orientation) {
        SizeConfig().state(constraints, orientation);
        return ChangeNotifierProvider(
            create: (_) => ThemeNotifier(),
            child: Consumer<ThemeNotifier>(
                builder: (context, ThemeNotifier notifier, child) {
              return MaterialApp(
                title: '',
                theme: notifier.darkTheme ? dark : light,
                home: SplashScreen(),
                routes: <String, WidgetBuilder>{
                  '/AppLockPage': (BuildContext context) => new CallLogs(),
                  '/SignUpPage': (BuildContext context) => new CallLogs(),
                },
              );
            }));
      });
    });
  }
}
