#!/bin/bash


rm *.log
rm *.hlt

#cd standstillbot
#javac -d ../bin/standstillbot StandstillBot.java
#cd ..

cd ninethbot
javac -d ../bin/ninethbot NinethBot.java
cd ..

cd tenth
javac -d ../bin/tenth Tenth.java
cd ..

cd mybot
javac -d ../bin/mybot MyBot.java
cd ..


halite -d "30 30" "java -cp .:bin/ninethbot/ NinethBot" "java -cp .:bin/mybot/ MyBot" "java -cp .:bin/tenth/ Tenth"











#Language("Java", BOT +".java", "MyBot.java",
#        "java MyBot",
#        ["*.class", "*.jar"],
#        [(["*.java"], ErrorFilterCompiler(comp_args["Java"][0], filter_stderr="Note:"))]
#    ),