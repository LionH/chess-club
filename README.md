#  [![Build Status][travis-image]][travis-url] [![Coverage Status](https://coveralls.io/repos/ChessCorp/chess-club/badge.svg?branch=master&service=github)](https://coveralls.io/github/ChessCorp/chess-club?branch=master)


> Chess Server that will provide restful APIs and a complete Web UI.


## Build and Run

```sh
$ mvn clean package
```

This builds an executable jar file in the target folder.
This jar file can now be started as:

```sh
$ java -jar chess-club-1.0.0.jar
```

The server is then listening on port 8080. This port can be overridden:

```sh
$ java -Dserver.port=8888 -jar chess-club-1.0.0.jar
```
 

## License

MIT © [Yannick Kirschhoffer](http://www.alcibiade.org/)

[travis-image]: https://travis-ci.org/ChessCorp/chess-club.svg?branch=master
[travis-url]: https://travis-ci.org/ChessCorp/chess-club
