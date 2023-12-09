def main():
    puzzle_input = readFile()
    # Task 1
    print(f"Task 1 | Output: {sum_of_extrapolated_values(puzzle_input)}")
    # Task 2
    print(f"Task 2 | Output: {sum_of_extrapolated_values(puzzle_input, preceding=False)}")


def readFile():
    name = "day_nine_input.txt"
    readings = []
    with open(name, 'r') as file:
        for line in file.readlines():
            readings.append(list(map(int, line.split())))
    return readings


def sum_of_extrapolated_values(readings, preceding=True):
    sum_value = 0
    for read in readings:
        extrapolated_value = find_extrapolated_value(read, preceding)
        sum_value += extrapolated_value
    return sum_value


def find_extrapolated_value(read, preceding):
    if len(set(read)) == 1:  # Base Case
        return read[0]

    diff_of_read = []
    for i in range(len(read) - 1):
        diff = read[i + 1] - read[i]
        diff_of_read.append(diff)

    if preceding:
        return find_extrapolated_value(diff_of_read, preceding) + read[-1]
    else:
        return read[0] - find_extrapolated_value(diff_of_read, preceding)


if __name__ == '__main__':
    main()

