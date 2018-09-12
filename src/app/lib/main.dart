import 'package:flutter/material.dart';
import 'signInButton.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Demo',
      theme: new ThemeData(
        brightness: Brightness.dark,
        accentColor: Colors.red,
      ),
      home: new LogInScreen(title: 'PubHub'),
    );
  }
}

class LogInScreen extends StatefulWidget {
  LogInScreen({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => new _MyHomePageState();
}

class _MyHomePageState extends State<LogInScreen> {

  void _authenticate(){
    // put sign in code here
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(widget.title),
      ),
      body: new Center(
        child: new Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Text(
              'There is no button to push',
            ),
            RaisedButton(
              child: button('Google', 'images/google.png'),
              onPressed: _authenticate,
              color: Color.fromRGBO(255, 255, 255, 1.0),
              //color: Color.fromRGBO(66, 133, 244, 1.0),
            ),
          ],
        ),
      ),
    );
  }
}
