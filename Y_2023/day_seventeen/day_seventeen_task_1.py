def main():
    puzzle_input = readFile()
    # Output
    print("Task 1 | Output: ", end="")
    print(minimize_heat_loss(puzzle_input.copy()))


def readFile():
    name = "day_seventeen_input.txt"
    board = []
    with open(name, 'r') as file:
        for line in file.readlines():
            board.append([int(a) for a in line.strip()])
    return board


def minimize_heat_loss(city_map):
    maxrow, maxcol = len(city_map), len(city_map[0])
    path = [(0, ("E", 1), 0, 0)]  # Current Heat Exposure, Direction/Streak, row, col
    visited = set()

    while len(path) > 0:
        heat_val, direct, r, c = min(path)
        path.remove(min(path))
        if r == maxrow - 1 and c == maxcol - 1:
            return heat_val

        if (direct, r, c) in visited:
            continue
        visited.add((direct, r, c))

        next_directions = compute_next(direct)
        for new_direct, x, y in next_directions:
            xr = x + r
            yc = y + c
            if 0 <= xr < maxrow and 0 <= yc < maxcol:
                next_val = city_map[xr][yc]
                path.append((heat_val + next_val, new_direct, xr, yc))


def compute_next(direct):
    label, streak = direct
    possible_directions, possible_vectors = compute_standards(label)
    if streak < 3:
        return organize_directions(possible_directions, possible_vectors, label=label, streak=(streak + 1))
    ind = possible_directions.index(label)
    possible_directions.pop(ind)
    possible_vectors.pop(ind)
    return organize_directions(possible_directions, possible_vectors)


def compute_standards(label_direct):
    standard_directions = ["E", "S", "W", "N"]
    standard_vectors = [(0, 1), (1, 0), (0, -1), (-1, 0)]
    assert label_direct in standard_directions
    if label_direct == "N":
        standard_directions.pop(1)
        standard_vectors.pop(1)
        return standard_directions, standard_vectors
    if label_direct == "W":
        standard_directions.pop(0)
        standard_vectors.pop(0)
        return standard_directions, standard_vectors
    if label_direct == "S":
        standard_directions.pop()
        standard_vectors.pop()
        return standard_directions, standard_vectors
    standard_directions.pop(2)
    standard_vectors.pop(2)
    return standard_directions, standard_vectors


def organize_directions(directions, vectors, label=None, streak=None):
    list_of_instruct = []
    for i in range(len(directions)):
        if label is not None and label == directions[i]:
            list_of_instruct.append(((label, streak), *vectors[i]))
        else:
            list_of_instruct.append(((directions[i], 1), *vectors[i]))
    return list_of_instruct


if __name__ == '__main__':
    main()
