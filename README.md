# mobile-device-management

The server must be running before the client.

## Dependencies

java sdk, make, git

## Server

Handles the client requests and handles the database. In the first terminal...

### Build Option #1

cd src/server

make clean

make

make run

### Build Option #2

cd src/server

rm -rf *.class (Linux) OR del *.class (Windows)

javac MDMServer.java

java MDMServer

## Client

UI client to send requests to the server. In the second terminal...

### Build Option #1

cd src/client

make clean

make

make run

### Build Option #2

cd src/client

rm -rf *.class (Linux) OR del *.class (Windows)

javac MDMClient.java

java MDMClient

### Team Members

Abhiram Kothapalli

Akira Aida

Daksh Joshi

Nathan Ingram

Shriya Satish

Sihao Shen
