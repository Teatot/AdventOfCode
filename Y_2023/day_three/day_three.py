def main():
    puzzle_input = readFile()
    # Task 1
    print(f"Task 1\nOutput: {sumOfPartNumbers(puzzle_input)}")
    # Task 2
    print(f"Task 2\nOutput: {sumOfGearRatios(puzzle_input)}")


def readFile():
    name = "day_three_input.txt"
    board = []
    with open(name, 'r') as file:
        for line in file.readlines():
            row = [a for a in line]
            if row[-1] == "\n":
                row.pop()
            board.append(row)
    return board


def sumOfPartNumbers(grid):
    total_of_parts = 0
    for row in range(len(grid)):
        for col in range(len(grid[row])):
            if symbol(grid[row][col]):
                adj_nums = find_adj_nums(grid, row, col)
                total_of_parts += sum(adj_nums)
    return total_of_parts


def sumOfGearRatios(grid):
    total_of_ratios = 0
    for row in range(len(grid)):
        for col in range(len(grid[row])):
            char_pos = grid[row][col]
            valid_ratios = valid_gearRatio(grid, row, col)
            if char_pos == "*" and valid_ratios is not False:
                total_of_ratios += valid_ratios[0] * valid_ratios[1]

    return total_of_ratios

    # Helper Functions


def symbol(char):
    if char != '.' and char not in map(str, range(10)):
        return True
    return False


def find_adj_nums (board, row, col):
    nums = []
    identified_regions = [(row, col)]
    radius = (-1, 0, 1)
    for r_x in radius:
        for r_y in radius:
            sub_row, sub_col = row + r_x, col + r_y
            if within_range(board, sub_row, sub_col) and num(board[sub_row][sub_col]) and (sub_row, sub_col) not in identified_regions:
                part_num = board[sub_row][sub_col]
                # Check Left-bound
                left_bound = sub_col - 1
                while left_bound >= 0 and num(board[sub_row][left_bound]):
                    part_num = board[sub_row][left_bound] + part_num
                    identified_regions.append((sub_row, left_bound))
                    left_bound -= 1
                # Check Right-bound
                right_bound = sub_col + 1
                while right_bound < len(board[sub_row]) and num(board[sub_row][right_bound]):
                    part_num = part_num + board[sub_row][right_bound]
                    identified_regions.append((sub_row, right_bound))
                    right_bound += 1
                nums.append(int(part_num))
    return nums


def valid_gearRatio(grid, row, col):
    arr = find_adj_nums(grid, row, col)
    if len(arr) == 2:
        return arr
    return False


def num(char):
    if char in map(str, range(10)):
        return True
    return False


def within_range(grid, row, col):
    if row >= len(grid) or row < 0:
        return False
    if col >= len(grid[row]) or col < 0:
        return False
    return True


if __name__ == '__main__':
    main()
