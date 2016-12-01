#!/bin/bash


gradle shadowJar


rm *.hlt
rm *.log
rm *.lck

halite -t -d "40 40" "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5006,quiet=y -jar build/libs/MyBot.jar" "java -jar MyBot22.jar" "java -jar MyBot25.jar"
#halite -d "30 30" "java MyBot" "java FirstBot" "java RandomBot"
