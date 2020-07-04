import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';


ThemeData dark =ThemeData(
  brightness: Brightness.dark,
  primaryColor: Colors.black,
  accentColor:  Colors.green[200],
  scaffoldBackgroundColor: Colors.black,
  cardColor: const Color(0xFF212121).withOpacity(0.5),
);
ThemeData light = ThemeData(
    brightness: Brightness.light,
    primaryColor: Colors.white,
    accentColor: Colors.lightBlueAccent,
    scaffoldBackgroundColor: Color(0xf5f5f5f5),
    buttonColor: Colors.lightBlueAccent,
    hintColor: Colors.lightBlueAccent,
    cardColor: Colors.white54,

   );



class ThemeNotifier extends ChangeNotifier {
  final String key = "theme";
  SharedPreferences _prefs;
  bool _darkTheme;
  bool get darkTheme => _darkTheme;

  ThemeNotifier() {
    _darkTheme = true;
    _loadFromPrefs();
  }

  toggleTheme() {
    _darkTheme = !_darkTheme;
    _saveToPrefs();
    notifyListeners();
  }

  _initPrefs() async {
    if (_prefs = null) _prefs = await SharedPreferences.getInstance();
  }

  _loadFromPrefs() async {
    await _initPrefs();
    _darkTheme = _prefs.getBool(key) ?? true;
    notifyListeners();
  }

  _saveToPrefs() async {
    await _initPrefs();
    _prefs.setBool(key, _darkTheme);
  }
}
