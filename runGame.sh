#!/bin/bash


rm *.log
rm *.hlt

#cd standstillbot
#javac -d ../bin/standstillbot StandstillBot.java
#cd ..

#cd twelth
#javac -d ../bin/twelth Twelth.java
#cd ..

#cd eleventh
#javac -d ../bin/eleventh Eleventh.java
#cd ..
#
#cd v16
#javac -d ../bin/v16 Sixteenth.java
#cd ..

#cd v17
#javac -d ../bin/v17 Seventeenth.java
#cd ..

#cd thirteenth
#javac -d ../bin/thirteenth -cp .:commons-math3-3.6.1.jar Thirteenth.java
#cd ..

cd mybot
javac -d ../bin/mybot MyBot.java
cd ..


halite -q -d "35 35" "java -cp .:bin/mybot/ MyBot" "java -cp .:bin/eleventh/ Eleventh" "java -cp .:bin/v16/ Sixteenth"











#Language("Java", BOT +".java", "MyBot.java",
#        "java MyBot",
#        ["*.class", "*.jar"],
#        [(["*.java"], ErrorFilterCompiler(comp_args["Java"][0], filter_stderr="Note:"))]
#    ),