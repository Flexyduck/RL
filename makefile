JFLAGS =	-g
JC =	javac
.SUFFIXES:	.java	.class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Main.java\
	ValueIterationAgent.java\
	ValueIterGUI.java\
	State.java\
	QLearning.java\
	QLearningGUI.java\
	Grid.java\
	ExitState.java\
	Boulder.java\

default:	classes

classes:	$(CLASSES:.java=.class)

clean:
	$(RM) *.class