def main():
    puzzle_input = readFile()
    # Task 1
    print("Task 1 | Output: ", end="")
    print(find_farthest_point(puzzle_input))



def readFile():
    name = 'day_ten_input.txt'
    loop_map = []
    with open(name, 'r') as file:
        for line in file.readlines():
            loop_map.append([pipe for pipe in "".join(line.split())])
    return loop_map


def find_farthest_point(loop_map):
    start = find_start_position(loop_map)
    loop_length = find_length_main_loop(start, loop_map)
    """
    The Farthest Point from the start will be the midpoint of the length of the Loop
    """
    return loop_length / 2


def find_length_main_loop(start, loop_map, return_pos=False):  # Breadth Search Flood
    start_x, start_y = start
    path = [(0, start_x, start_y)]
    visited = set()

    while len(path) > 0:
        length, r, c = max(path)
        path.remove(max(path))

        if (r, c) == start and len(visited) > 0:
            if return_pos:
                return length, visited
            return length

        if (r, c) in visited:
            continue
        visited.add((r, c))

        node = loop_map[r][c]
        if node == ".":
            continue

        movements = check_possible_next_movements(node, loop_map, r, c)
        for x, y in movements:
            xr = r + x
            yc = c + y
            if 0 <= xr < len(loop_map) and 0 <= yc < len(loop_map[0]):
                path.append((length + 1, xr, yc))


def find_start_position(loop_map):
    for r in range(len(loop_map)):
        for c in range(len(loop_map[r])):
            if loop_map[r][c] == "S":
                return r, c
    return None


def check_possible_next_movements(pipe_type, loop_map=None, x_pos=None, y_pos=None):
    if pipe_type == "S":
        return check_start_next_movements(loop_map, x_pos, y_pos)
    elif pipe_type == '|':  # Vertical Pipe - Connecting North and South
        return [(1, 0), (-1, 0)]
    elif pipe_type == "-":  # Horizontal Pipe - Connecting East and West
        return [(0, 1), (0, -1)]
    elif pipe_type == "L":  # 90-Deg - Connecting North and East
        return [(-1, 0), (0, 1)]
    elif pipe_type == "J":  # 90-Deg - Connecting North and West
        return [(-1, 0), (0, -1)]
    elif pipe_type == "7":  # 90-Deg - Connecting South and West
        return [(1, 0), (0, -1)]
    elif pipe_type == "F":  # 90-Deg - Connecting South and East
        return [(1, 0), (0, 1)]


def check_start_next_movements(loop_map, x_pos, y_pos):
    possible_areas = []
    if loop_map[x_pos - 1][y_pos] not in ("-", 'J', 'L', '.'):
        possible_areas.append((-1, 0))
    if loop_map[x_pos + 1][y_pos] not in ("-", '7', 'F', '.'):
        possible_areas.append((1, 0))
    if loop_map[x_pos][y_pos - 1] not in ("|", 'J', '7', '.'):
        possible_areas.append((0, -1))
    if loop_map[x_pos][y_pos + 1] not in ("|", 'L', 'F', '.'):
        possible_areas.append((0, 1))

    return possible_areas


if __name__ == '__main__':
    main()
