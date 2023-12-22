def main():
    puzzle_input = readFile()
    # Output
    print("Task 2 | Output: ", end="")
    print(compute_maximize_tiles(puzzle_input.copy()))


def readFile():
    name = "day_sixteen_input.txt"
    board = []
    with open(name, 'r') as file:
        for line in file.readlines():
            board.append([a for a in line.strip()])
    return board


def compute_maximize_tiles(cave_map):
    energized_tiles = set()
    # Coasts
    for k in range(1, len(cave_map[0]) - 1):
        west_path = [("W", k, 0)]  # East Side to West Side
        east_path = [("E", k, len(cave_map) - 1)]  # West Side to East Side
        energized_tiles.add(compute_energize_tiles(cave_map.copy(), west_path))
        energized_tiles.add(compute_energize_tiles(cave_map.copy(), east_path))
    # Poles
    for j in range(1, len(cave_map) - 1):
        north_path = [("N", len(cave_map) - 1, j)]  # South Side to North Side
        south_path = [("S", 0, j)]  # North Side to South Side
        energized_tiles.add(compute_energize_tiles(cave_map.copy(), north_path))
        energized_tiles.add(compute_energize_tiles(cave_map.copy(), south_path))
    return max(energized_tiles)


def compute_energize_tiles(cave_map, paths):  # Somewhat of a BFS
    visited = set()

    while len(paths) > 0:
        direction, r, c = paths.pop(0)  # Enqueuing
        if (direction, r, c) in visited:
            continue
        visited.add((direction, r, c))

        next_paths = compute_next_paths(cave_map, direction, r, c)
        if next_paths is None:
            continue
        for new_direct, x, y in next_paths:
            rx = x + r
            yc = y + c
            if 0 <= rx < len(cave_map) and 0 <= yc < len(cave_map[0]):
                paths.append([new_direct, rx, yc])

    return num_of_energized(visited)


def compute_next_paths(board, direct, x, y):
    standard_direct = ("N", "W", "S", "E")
    standard_vectors = ((-1, 0), (0, -1), (1, 0), (0, 1))
    assert direct in standard_direct
    if board[x][y] == ".":  # Empty Space
        ind = standard_direct.index(direct)
        new_x, new_y = standard_vectors[ind]
        return ((direct, new_x, new_y),)
    if board[x][y] == "|":
        if direct in ("N", "S"):  # Treat as Normal Empty Space
            ind = standard_direct.index(direct)
            new_x, new_y = standard_vectors[ind]
            return ((direct, new_x, new_y),)
        # Special Case - Split Into Two
        return ("N", -1, 0), ("S", 1, 0)

    if board[x][y] == "-":
        if direct in ("W", "E"):  # Treat as Normal Empty Space
            ind = standard_direct.index(direct)
            new_x, new_y = standard_vectors[ind]
            return ((direct, new_x, new_y),)
        # Special Case - Split Into Two
        return ("W", 0, -1), ("E", 0, 1)
    if board[x][y] == "\\":
        if direct == "N":
            return (("W", 0, -1),)
        if direct == "E":
            return (("S", 1, 0),)
        if direct == "W":
            return (("N", -1, 0),)
        if direct == "S":
            return (("E", 0, 1),)
    if board[x][y] == "/":
        if direct == "N":
            return (("E", 0, 1),)
        if direct == "W":
            return (("S", 1, 0),)
        if direct == "E":
            return (("N", -1, 0),)
        if direct == "S":
            return (("W", 0, -1),)


def num_of_energized(visited):
    unique_positions = set()
    for direct, x, y in visited:
        unique_positions.add((x, y))
    return len(unique_positions)


if __name__ == '__main__':
    main()