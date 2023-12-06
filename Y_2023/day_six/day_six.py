import math
def main():
    puzzle_input = readFile()
    # Task 1
    print(f"Task 1\nOutput: {margin_of_error(puzzle_input)}")
    # Task 2
    print(f"Task 2\nOutput: {num_of_combinations(puzzle_input)}")


def readFile():
    name = "day_six_input.txt"
    races = []
    times, distances = [], []
    with open(name, 'r') as file:
        for line in file.readlines():
            if line[0].lower() == 't':  # Time
                times = line.split()[1:]
            else: # Distance
                distances = line.split()[1:]
    for i in range(len(times)):
        races.append((times[i], distances[i]))
    return races

def margin_of_error(races):
    product_of_records = 1
    for game in races:
        product_of_records *= calculate_recordRaces(game)
    return product_of_records


def num_of_combinations(race):
    race_time = int("".join([r[0] for r in race]))
    race_distance = int("".join([r[1] for r in race]))
    return calculate_recordRaces((race_time, race_distance))

    # Helper Functions

def calculate_recordRaces(game):
    record_games = 0
    race_time, race_distance = game
    mid = math.ceil(int(race_time)/2)
    n_mid = mid
    while n_mid <= int(race_time):
        if (int(race_time) - n_mid) * n_mid > int(race_distance):
            record_games += 1
        else:
            break
        n_mid += 1
    if int(race_time) % 2 == 0:  # EVEN NUM
        return (record_games - 1) * 2 + 1  # Not include the mid when doubling; add after
    return record_games * 2  # ODD NUM


if __name__ == '__main__':
    main()