def main():
    puzzle_input = readFile()
    # Task 1 Solution
    print(f"Task 1\nOutput: {validGame(puzzle_input)}")
    # Task 2 Solution
    print(f"Task 2\nOutput: {powerOfGame(puzzle_input)}")


def readFile():
    name = "day_two_input.txt"
    l_games_played = []
    with open(name, 'r') as file:
        for line in file.readlines():
            game_data = line.split(":")
            game_id = "".join(game_data[0]).split()[-1]
            game_results = "".join(game_data[-1]).split(";")
            game_results.insert(0, game_id)
            l_games_played.append(game_results)
    return l_games_played


def validGame(puzzle_input):
    sum_of_ideal_games = 0
    for game in puzzle_input:
        id = int(game[0])
        outcomes = game[1:]
        valid_sets = 0
        for s in outcomes:
            num_of_cubes = {"r": 0, "g": 0, "b": 0}
            p_num = 0
            s = s.split()  # reformat
            while p_num + 1 < len(s):
                num_of_cubes[s[p_num + 1][0]] += int(s[p_num])
                p_num += 2
            # Checking if Set is Valid
            if validSet(num_of_cubes):
                valid_sets += 1

        # Checking if Game is Valid (Ideal)
        if valid_sets == len(outcomes):
            sum_of_ideal_games += id
    return sum_of_ideal_games


def powerOfGame(puzzle_input):
    sum_of_power = 0
    for game in puzzle_input:
        fewest_num_cubes = {'r': 0, 'g': 0, 'b': 0}
        game_power = 1
        outcomes = game[1:]
        for s in outcomes:
            s = s.split()  # reformat
            p_num = 0
            while p_num + 1 < len(s):
                if fewest_num_cubes[s[p_num + 1][0]] < int(s[p_num]):
                    fewest_num_cubes[s[p_num + 1][0]] = int(s[p_num])
                p_num += 2
        # Calculating Power for Game
        for value in fewest_num_cubes.values():
            game_power *= value
        sum_of_power += game_power
    return sum_of_power

    # Helper Functions


def validSet(set_data):
    ideal_game = {
        "r": 12,
        "g": 13,
        "b": 14
    }
    for name, value in ideal_game.items():
        if set_data[name] > value:
            return False
    return True


if __name__ == '__main__':
    main()
