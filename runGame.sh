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

cd thirteenth
javac -d ../bin/thirteenth -cp .:commons-math3-3.6.1.jar Thirteenth.java
cd ..

cd mybot
javac -d ../bin/mybot  -cp .:commons-math3-3.6.1.jar MyBot.java
cd ..


halite -q -d "40 40" "java -cp .:bin/mybot/:mybot/commons-math3-3.6.1.jar MyBot" "java -cp .:bin/thirteenth/:thirteenth/commons-math3-3.6.1.jar Thirteenth" "java -cp .:bin/eleventh/ Eleventh" "java -cp .:bin/twelth/ Twelth"











#Language("Java", BOT +".java", "MyBot.java",
#        "java MyBot",
#        ["*.class", "*.jar"],
#        [(["*.java"], ErrorFilterCompiler(comp_args["Java"][0], filter_stderr="Note:"))]
#    ),