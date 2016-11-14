#!/bin/bash


rm *.log
rm *.hlt

#cd standstillbot
#javac -d ../bin/standstillbot StandstillBot.java
#cd ..

cd twelth
javac -d ../bin/twelth Twelth.java
cd ..

cd eleventh
javac -d ../bin/eleventh Eleventh.java
cd ..

cd mybot
javac -d ../bin/mybot MyBot.java
cd ..


halite -d "30 30" "java -cp .:bin/eleventh/ Eleventh" "java -cp .:bin/mybot/ MyBot" "java -cp .:bin/twelth/ Twelth"











#Language("Java", BOT +".java", "MyBot.java",
#        "java MyBot",
#        ["*.class", "*.jar"],
#        [(["*.java"], ErrorFilterCompiler(comp_args["Java"][0], filter_stderr="Note:"))]
#    ),