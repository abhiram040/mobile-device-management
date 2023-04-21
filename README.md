# mobile-device-management

Clone the repository locally. 'make' is the preferred build method but instructions are provided if you do not have 'make' installed.

## Dependencies

java sdk, git, make (optional)

## Server

Handles the client requests and handles the storage of information. In the first terminal...

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

### Design Patterns

TO BE FILLED IN

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

### Design Patterns

Command Pattern (Refer to Command.java and all other <COMMAND_TYPE>Command.java files)

Simple Factory (Refer to the CommandFactory.java)

Singleton (Refer to the CommandFactory.java)

### Team Members

Abhiram Kothapalli

Akira Aida

Daksh Joshi

Nathan Ingram

Shriya Satish

Sihao Shen
