
rm *.log
rm *.hlt

cd twelth
javac -d ..\bin\twelth Twelth.java
cd ..

cd eleventh
javac -d ..\bin\eleventh Eleventh.java
cd ..

cd mybot
javac -d ..\bin\mybot MyBot.java
cd ..


.\halite.exe -d "40 40" "java -cp bin/mybot MyBot" "java -cp bin/eleventh Eleventh" "java -cp bin/twelth Twelth"



