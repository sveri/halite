#!/bin/bash


gradle shadowJar


rm *.hlt
rm *.log
rm *.lck

halite -t -d "30 30" "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5006,quiet=y -jar build/libs/MyBot.jar" "python3 ../Halite-Python-Starter-Package/MyBot.py"
#halite -d "30 30" "java MyBot" "java FirstBot" "java RandomBot"
