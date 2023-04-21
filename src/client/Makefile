GS = -g
JC = javac
JR = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java -Xlint

CLASSES = \
	MDMClient.java \
	AddAccountV1Command.java \
	CommandFactory.java \
	AddAccountV2Command.java \
	AddPacBundleV1Command.java \
	AddPacBundleV2Command.java \
	AddPacBundleV3Command.java \
	AddPreBundleCommand.java \
	AddUserCommand.java \
	AddUsersCommand.java \
	ClientMessageHandler.java \
	Command.java \
	DeleteAccountCommand.java \
	MessageContainer.java \
	DeleteUserCommand.java \
	DeleteUsersCommand.java \
	ListAccountCommand.java \
	ListAccountsCommand.java \
	ListAllPacBundlesCommand.java \
	ListAllPreBundlesCommand.java \
	ListAllUsersCommand.java \
	ListBundleDetailsCommand.java \
	ListMonthlyFeesAllCommand.java \
	ListMonthlyFeesCommand.java \
	ListUserDetailsCommand.java \
	MenuOption.java \
	StubCommand.java \
	UI.java \
	UpdateAccountCommand.java \
	UpdateUserCommand.java

default: classes

run:
	$(JR) MDMClient

classes: $(CLASSES:.java=.class)

clean:
	 $(RM) *.class
