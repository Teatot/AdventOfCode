def main():
    puzzle_input = readFile()
    # Task 1
    print("Task 1 | Output: ", end="")
    print(sum_of_distances(puzzle_input.copy()))
    # Task 2
    print("Task 2 | Output: ", end="")
    print(sum_of_distances(puzzle_input.copy(), 999999))


def readFile():
    list_map = []
    name = "day_eleven_input.txt"
    with open(name, 'r') as file:
        for line in file.readlines():
            list_map.append([it for it in "".join(line.split())])
    return list_map


def sum_of_distances(space_map, skip=1):
    total_distances = 0
    empty_rows, empty_cols = find_empty_rows_cols(space_map)
    galaxies = locate_galaxies(space_map)
    galaxies_pairs = pair_galaxies(galaxies)
    for start, end in galaxies_pairs:
        total_distances += find_shortest_path(start, end, empty_rows, empty_cols, skip)
    return total_distances


def find_shortest_path(start, end, empty_rows, empty_cols, skip):
    start_x, start_y = start
    end_x, end_y = end
    # Finding Blank Space
    row_skip, col_skip = 0, 0
    x_range = range(start_x + 1, end_x) if start_x < end_x else range(end_x + 1, start_x)
    y_range = range(start_y + 1, end_y) if start_y < end_y else range(end_y + 1, start_y)
    for x in x_range:
        if x in empty_rows:
            row_skip += 1
    for y in y_range:
        if y in empty_cols:
            col_skip += 1
    return abs(end_x - start_x) + (skip*row_skip) + abs(end_y - start_y) + (skip*col_skip)


def locate_galaxies(space):
    galaxies_location = set()
    for r in range(len(space)):
        for c in range(len(space[r])):
            if space[r][c] == '#':
                galaxies_location.add((r, c))
    return galaxies_location


def pair_galaxies(galaxies):
    pair_set = set()
    for galaxy in galaxies:
        possible_matching = {friend for friend in galaxies if friend != galaxy}
        for single in possible_matching:
            pair = tuple(sorted((galaxy, single)))
            if pair not in pair_set:
                pair_set.add(pair)
    return pair_set


def find_empty_rows_cols(space):
    empty_rows, empty_cols = set(), set()
    for r in range(len(space)):
        row_count = 0
        for c in range(len(space[r])):
            if space[r][c] == '#':
                row_count += 1
        #
        if row_count == 0:
            empty_rows.add(r)

    for c in range(len(space[0])):
        column_count = 0
        for r in range(len(space)):
            if space[r][c] == '#':
                column_count += 1
        #
        if column_count == 0:
            empty_cols.add(c)
    return sorted(empty_rows), sorted(empty_cols)


if __name__ == '__main__':
    main()
