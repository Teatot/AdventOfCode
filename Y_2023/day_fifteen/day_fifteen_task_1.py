def main():
    puzzle_input = readFile()
    print("Task 1 | Output: ", end="")
    print(sum_of_results(puzzle_input.copy()))


def readFile():
    name = "day_fifteen_input.txt"
    with open(name, 'r') as file:
        entry = "".join(file.readlines()).strip().split(",")
        return entry


def sum_of_results(line):
    summed_value = 0
    for seq in line:
        summed_value += HASH_algorithm(seq)
    return summed_value

# Helper Functions


def HASH_algorithm(seq):
    cur_val = 0
    for ch in seq:
        cur_val = ((cur_val + ord(ch)) * 17) % 256
    return cur_val


if __name__ == '__main__':
    main()
