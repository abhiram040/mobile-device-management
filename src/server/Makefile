GS = -g
JC = javac
JR = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java -Xlint

CLASSES = \
	MDMServer.java \
	AccountManagement.java \
	ServerMessageHandler.java \
	Bundle.java \
	ResponseHandler.java \
	BundleManagement.java \
	MenuOption.java \
	ServiceAccount.java \
	User.java \
	UserManagement.java

default: classes

run:
	$(JR) MDMServer

classes: $(CLASSES:.java=.class)

clean:
	 $(RM) *.class
