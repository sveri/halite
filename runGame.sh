#!/bin/bash


rm *.log
rm *.hlt

cd firstbot
javac -d ../bin/firstbot FirstBot.java
cd ..

cd mybot
javac -d ../bin/mybot MyBot.java
cd ..


halite -d "30 30" "java -cp .:bin/firstbot/ FirstBot" "java -cp .:bin/mybot/ MyBot"











#Language("Java", BOT +".java", "MyBot.java",
#        "java MyBot",
#        ["*.class", "*.jar"],
#        [(["*.java"], ErrorFilterCompiler(comp_args["Java"][0], filter_stderr="Note:"))]
#    ),