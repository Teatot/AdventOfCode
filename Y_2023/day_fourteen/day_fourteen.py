def main():
    puzzle_input = readFile()
    # Task 1
    task1_input = tilt_rocks(transpose(puzzle_input.copy()), "NORTH")
    print("Task 1 | Output: ", end="")
    print(calculate_load(task1_input))
    # Task 2
    print("Task 2 | Output: ", end="")
    print(calculate_load_cycle(puzzle_input.copy()))


def readFile():
    name = "day_fourteen_input.txt"
    board = []
    with open(name, 'r') as file:
        for line in file.readlines():
            board.append([a for a in line.strip()])
    return board


def calculate_load(dish):
    total_load = 0
    maxrow, maxcol = len(dish), len(dish[0])

    for i in range(maxrow):
        for j in range(maxcol):
            if dish[i][j] == "O":
                total_load += maxcol - j
    return total_load


def calculate_load_cycle(dish, cycles=1000000000):
    count_of_positions = []
    positions_history = []
    for c in range(cycles):
        dish = cycle(dish)
        if dish not in positions_history:
            positions_history.append(dish)
            count_of_positions.append(1)
        else:
            ind = positions_history.index(dish)
            count_of_positions[ind] += 1

        if len(positions_history) < sum(count_of_positions):
            centre_point = find_loop(count_of_positions)
            cycles -= len(count_of_positions[:centre_point])  # Updating Cycle to fit with the Loop
            # Updating New Arrays
            count_of_positions = count_of_positions[centre_point:]
            positions_history = positions_history[centre_point:]
            break
    remainder = cycles % len(count_of_positions)
    if remainder == 0:
        return calculate_load(transpose(positions_history[-1]))
    return calculate_load(transpose(positions_history[remainder - 1]))

# Helper Functions


def cycle(board):
    directions = ("NORTH", "WEST", "SOUTH", "EAST")
    for d in directions:
        board = transpose(board)
        board = tilt_rocks(board, d)
    return board


def transpose(matrix):
    return list(map(list, list(zip(*matrix))))


def tilt_rocks(board, direction):
    assert direction in ("NORTH", "SOUTH", "WEST", "EAST")
    maxrow, maxcol = len(board), len(board[0])
    for row in range(maxrow):
        location_of_rounded = location_rounded_rocks(board[row], direction)

        for loc_rounded in location_of_rounded:
            if direction in ("NORTH", "WEST"):
                move_rock_north_west(board[row], loc_rounded)
            elif direction in ("SOUTH", "EAST"):
                move_rock_south_east(board[row], loc_rounded)
    return board


def move_rock_north_west(row, cur_pos):
    row[cur_pos] = "."
    for i in range(cur_pos - 1, -1, -1):
        if row[i] == "#" or row[i] == "O":
            row[i + 1] = "O"
            return
    row[0] = "O"
    return


def move_rock_south_east(row, cur_pos):
    row[cur_pos] = "."
    for j in range(cur_pos + 1, len(row)):
        if row[j] == "#" or row[j] == "O":
            row[j - 1] = "O"
            return
    row[len(row) - 1] = "O"
    return


def location_rounded_rocks(row, direction):
    locations = []
    search_range = range(len(row))
    if direction in ("SOUTH", "EAST"):
        search_range = range(len(row) - 1, -1, -1)
    for i in search_range:
        if row[i] == "O":
            locations.append(i)
    return locations


def find_loop(l):
    for i in range(len(l)):
        if l[i] != 1:
            return i
    return -1


if __name__ == '__main__':
    main()