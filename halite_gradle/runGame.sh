#!/bin/bash


gradle shadowJar

rm *.hlt
rm *.log
rm *.lck
rm *.zip

halite -d "20 20" "java -jar build/libs/MyBot.jar" "java -jar MyBot22.jar"
#halite -d "30 30" "java MyBot" "java FirstBot" "java RandomBot"

zip MyBot.zip build/libs/MyBot.jar






#Language("Java", BOT +".java", "MyBot.java",
#        "java MyBot",
#        ["*.class", "*.jar"],
#        [(["*.java"], ErrorFilterCompiler(comp_args["Java"][0], filter_stderr="Note:"))]
#    ),