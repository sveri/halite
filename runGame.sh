#!/bin/bash


rm *.log
rm *.hlt

cd firstbot
javac -d ../bin/firstbot FirstBot.java
cd ..

cd fourthbot
javac -d ../bin/fourthbot FourthBot.java
cd ..

cd fithbot
javac -d ../bin/fithbot FithBot.java
cd ..

cd mybot
javac -d ../bin/mybot MyBot.java
cd ..


#halite -d "20 20" "java -cp .:bin/mybot/ MyBot" "java -cp .:bin/fourthbot/ FourthBot"
halite -d "25 25" "java -cp .:bin/firstbot/ FirstBot" "java -cp .:bin/mybot/ MyBot" "java -cp .:bin/fithbot/ FithBot"











#Language("Java", BOT +".java", "MyBot.java",
#        "java MyBot",
#        ["*.class", "*.jar"],
#        [(["*.java"], ErrorFilterCompiler(comp_args["Java"][0], filter_stderr="Note:"))]
#    ),