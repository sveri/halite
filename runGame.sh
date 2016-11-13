#!/bin/bash


rm *.log
rm *.hlt

#cd standstillbot
#javac -d ../bin/standstillbot StandstillBot.java
#cd ..

cd sixthbot
javac -d ../bin/sixthbot SixthBot.java
cd ..

cd seventhbot
javac -d ../bin/seventhbot MyBot.java
cd ..

cd mybot
javac -d ../bin/mybot MyBot.java
cd ..


#halite -d "20 20" "java -cp .:bin/mybot/ MyBot" "java -cp .:bin/fourthbot/ FourthBot"
halite -d "30 30" "java -cp .:bin/sixthbot/ SixthBot" "java -cp .:bin/mybot/ MyBot" "java -cp .:bin/seventhbot/ MyBot"











#Language("Java", BOT +".java", "MyBot.java",
#        "java MyBot",
#        ["*.class", "*.jar"],
#        [(["*.java"], ErrorFilterCompiler(comp_args["Java"][0], filter_stderr="Note:"))]
#    ),