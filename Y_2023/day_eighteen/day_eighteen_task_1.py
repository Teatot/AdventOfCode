def main():
    puzzle_input = readFile()
    # Output
    print("Task 1 | Output: ", end="")
    print(compute_area(puzzle_input.copy()))


def readFile():  # NOTE: Hexadecimal Part not Required for Task 1
    name = "day_eighteen_input.txt"
    instruct = []
    with open(name, 'r') as file:
        for line in file.readlines():
            instruct.append(line.strip().split()[:-1])
    return instruct


def compute_area(instruct):
    init_x, init_y = compute_start_position(instruct)
    cave_map = create_board(*approximate_max_dimensions(instruct, init_x, init_y))
    boarder_length = compute_boarders(cave_map, instruct, init_x, init_y)
    return flood(cave_map, *inside_lagoon(cave_map)) + boarder_length


def flood(cave_map, initial_x, initial_y):
    maxx, maxy = len(cave_map), len(cave_map[0])
    path = [(initial_x, initial_y)]
    visited = set()

    while len(path) > 0:
        x, y = path.pop(0)
        if (x, y) in visited:
            continue
        visited.add((x, y))
        for r, c in ((1, 0), (-1, 0), (0, 1), (0, -1)):
            rx = r + x
            cy = c + y
            if 0 <= rx < maxx and 0 <= cy < maxy and cave_map[rx][cy] == ".":
                cave_map[rx][cy] = "#"
                path.append((rx, cy))
    return len(visited)


def inside_lagoon(cave_map):
    maxx, maxy = len(cave_map), len(cave_map[0])
    for r in range(maxx):
        if pole_edges(cave_map[r]):
            continue
        for c in range(maxy):
            if cave_map[r][c] == "#":
                return r, c + 1


def compute_boarders(board, instruct, cur_x, cur_y):
    boarder_length = 0
    maxx, maxy = len(board), len(board[0])
    for direct, val in instruct:
        if direct == "D":
            for i in range(int(val)):
                board[cur_x][cur_y] = "#"
                cur_x += 1
        elif direct == "U":
            for i in range(int(val)):
                board[cur_x][cur_y] = "#"
                cur_x -= 1
        elif direct == "R":
            for i in range(int(val)):
                board[cur_x][cur_y] = "#"
                cur_y += 1
        elif direct == "L":
            for i in range(int(val)):
                board[cur_x][cur_y] = "#"
                cur_y -= 1
        boarder_length += int(val)
    return boarder_length


def approximate_max_dimensions(instruct, init_x=0, init_y=0):
    maxx, maxy = 0, 0
    for label, val in instruct:
        if label == "R":
            maxy += int(val)
        if label == "D":
            maxx += int(val)
    return maxx + init_x + 1, maxy + init_y + 1


def pole_edges(row):
    label = 0
    for unit in row:
        if unit == "#":
            label += 1
        if label > 2:
            return True
    return False


def compute_start_position(instruct):
    start_x, start_y = 0, 0
    for direct, val in instruct:
        if direct == "L":
            start_y += int(val)
        elif direct == "U":
            start_x += int(val)
    return start_x, start_y


def create_board(maxx, maxy):
    return [["." for _ in range(maxy)] for i in range(maxx)]


if __name__ == '__main__':
    main()
