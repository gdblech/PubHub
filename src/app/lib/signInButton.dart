import 'package:flutter/material.dart';

Widget button(title, uri, [ color = const Color.fromRGBO(68, 68, 76, .8) ]) {
  return Container(
    width: 230.0,
    height: 40.0,
    child: Center(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          Image.asset(
            uri,
            width: 30.0,
          ),
          Padding(
            child: Text(
            "Sign in with $title",
            style:  TextStyle(
              fontFamily: 'Roboto',
              color: color,
              fontSize: 20.0,
            ),
          ),
            padding: new EdgeInsets.only(left: 16.0),
          ),
        ],
      ),
    ),
  );
}