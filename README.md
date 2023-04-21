
# mobile-device-management

The mobile device management project is broken down into two separate executables; a server and a client.

## Dependencies

java sdk, git, make (optional)

## Server

### Build Option #1

```bash
cd src/server
make clean
make
make run
```

### Build Option #2

```bash
cd src/server
rm -rf *.class (Linux) OR del *.class (Windows)
javac MDMServer.java
java MDMServer
```

### Design Patterns

- TO BE FILLED IN
- TO BE FILLED IN
- TO BE FILLED IN

## Client

### Build Option #1

```bash
cd src/client
make clean
make
make run
```

### Build Option #2

```bash
cd src/client
rm -rf *.class (Linux) OR del *.class (Windows)
javac MDMClient.java
java MDMClient
```

### Design Patterns

- Command Pattern (Refer to Command.java and all other <COMMAND_TYPE>Command.java files)
- Simple Factory (Refer to the CommandFactory.java)
- Singleton (Refer to the CommandFactory.java)

### Team Members

- Abhiram Kothapalli
- Akira Aida
- Daksh Joshi
- Nathan Ingram
- Shriya Satish
- Sihao Shen