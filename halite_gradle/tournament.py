import os
import random
from pprint import pprint
from threading import Thread




map_sizes = ["\"20 20\"", "\"30 30\"", "\"40 40\"", "\"50 50\""]

results_first = {"1":0, "2":0, "3":0}
results_second = {"1":0, "2":0, "3":0}
results_third = {"1":0, "2":0, "3":0}


def update_results(results_map, result):
    count = results_map[result]
    results_map[result] = count + 1

def run_halite_game():
    for i in range(20):
        range_str = random.choice(map_sizes)
        print("selecting size: " + range_str)
        stream = os.popen("halite -q -d " + range_str +  " \"java -jar build/libs/MyBot.jar\" \"java -jar MyBot31.jar\" \"java -jar MyBot32.jar\"")

        bash_results = stream.read()
        bash_results = bash_results[-16:]
        first_result = bash_results[3:4]
        second_result = bash_results[7:8]
        third_result = bash_results[11:12]

        update_results(results_first, first_result)
        update_results(results_second, second_result)
        update_results(results_third, third_result)

        pprint(results_first)
        print("\n")

os.popen("gradle shadowJar")

thread = Thread(target = run_halite_game, args = ( ))
thread.start()
thread2 = Thread(target = run_halite_game, args = ( ))
thread2.start()
thread3 = Thread(target = run_halite_game, args = ( ))
thread3.start()
thread4 = Thread(target = run_halite_game, args = ( ))
thread4.start()
thread5 = Thread(target = run_halite_game, args = ( ))
thread5.start()
thread6 = Thread(target = run_halite_game, args = ( ))
thread6.start()


thread.join()
thread2.join()
thread3.join()
thread4.join()
thread5.join()
thread6.join()

print("\n\n")
pprint(results_first)
pprint(results_second)
pprint(results_third)



os.popen("rm *.hlt")
os.popen("rm *.log")

# halite -q -d "20 20" "java -jar build/libs/MyBot.jar" "java -jar MyBot22.jar" "java -jar MyBot25.jar"