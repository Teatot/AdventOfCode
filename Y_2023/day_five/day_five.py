import time
def main():
    puzzle_input_task_1 = readFile_Task1()
    puzzle_input_task_2 = readFile_Task2()
    # Task 1
    print(f"Task 1\nOutput: {findLowestLocation(puzzle_input_task_1)}")
    # Task 2
    print(f"Task 2\nOutput: {find_TrueLowestLocation(puzzle_input_task_2)}")


def readFile_Task1():
    name = "day_five_input.txt"
    seeds = []
    """
    Maps Order (Respectfully): 
    seed_to_soil, 
    soil_to_fertilizer,
    fertilizer_to_water,
    water_to_light,
    light_to_temp,
    temp_to_humid,
    humid_to_location,
    """
    maps = [[], [], [], [], [], [], []]
    with open(name, 'r') as file:
        start = True
        counter = -1
        for line in file.readlines():
            if start:  # Get Seeds Input
                seeds = list(map(int, line.split()[1:]))
                start = False
            # Fetching Maps
            elif is_letter(line[0]):
                counter += 1
            elif line[0].isnumeric():
                maps[counter].append(list(map(int, line.split())))
    return seeds, maps


def readFile_Task2():
    name = "day_five_input.txt"
    seeds = []
    """
    Maps Order (Respectfully): 
    seed_to_soil, 
    soil_to_fertilizer,
    fertilizer_to_water,
    water_to_light,
    light_to_temp,
    temp_to_humid,
    humid_to_location,
    """
    maps = [[], [], [], [], [], [], []]
    with open(name, 'r') as file:
        start = True
        counter = -1
        for line in file.readlines():
            if start:  # Get Seeds Input
                seeds_data = list(map(int, line.split()[1:]))
                p_f = 0
                while p_f + 1 < len(seeds_data):
                    seeds.append((seeds_data[p_f], seeds_data[p_f + 1]))
                    p_f += 2
                start = False

            # Fetching Maps
            elif is_letter(line[0]):
                counter += 1
            elif line[0].isnumeric():
                maps[counter].append(list(map(int, line.split())))
    return seeds, maps


def findLowestLocation(almanac):
    seeds, maps = almanac
    for item_map in maps:
        for i in range(len(seeds)):
            seeds[i] = convert_to_item(seeds[i], item_map)
    return min(seeds)


def find_TrueLowestLocation(almanac):
    seeds, maps = almanac
    for item_map in maps:
        size = len(seeds)
        i = 0
        while i < size:
            print(f"CUR TUPLE: {seeds[i]}.\t|\tCUR LIST: {seeds}")
            seeds[i], size = transform_to_item(seeds, i, item_map, size)
            i += 1
    # Finding the Lowest
    lowest = None
    for seed in seeds:
        if lowest is None:
            lowest = seed[0]
        elif seed[0] < lowest:
            lowest = seed[0]
    return lowest - 1

    # Helper Functions


def is_letter(char):
    if char.lower() in list(map(chr, range(ord('a'), ord('z') + 1))):
        return True
    return False


def convert_to_item(init_unit, conversions):
    for conv in conversions:
        diff = int(init_unit) - int(conv[1])
        if 0 < diff <= int(conv[-1]):
            return int(conv[0]) + diff
    return init_unit


def transform_to_item(seeds_data, ind, conversions, size):
    seed_start, seed_range = seeds_data[ind]
    for conv in conversions:
        diff = seed_start - conv[1]
        if 0 < diff <= conv[-1]:
            spot_remain = conv[-1] - diff
            seeds_left = spot_remain - seed_range
            print(f"Cur Conversion: {conv}\nRemaining: {spot_remain}\tLeft: {seeds_left}\n\n")
            time.sleep(0.5)
            if seeds_left >= 0:
                return (conv[0] + diff, seed_range), size

            seeds_data.append(((seed_start + seed_range + seeds_left + 1), abs(seeds_left)))
            return (conv[0] + diff, spot_remain), size + 1

    return seeds_data[ind], size


if __name__ == '__main__':
    main()
