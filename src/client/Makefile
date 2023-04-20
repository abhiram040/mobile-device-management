GS = -g
JC = javac
JR = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java -Xlint

# This uses the line continuation character (\) for readability
# You can list these all on a single line, separated by a space instead.
# If your version of make can't handle the leading tabs on each
# line, just remove them (these are also just added for readability).
CLASSES = \
	MDMClient.java \
	AddAccountV1Command.java \
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
