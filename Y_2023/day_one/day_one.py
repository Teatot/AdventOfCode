def main():
    puzzle_input = readFile()
    # Task 1 Solution
    print(f"Task 1\nOutput: {sum_calibrationValues(puzzle_input)}")
    # Task 2 Solution
    print(f"Task 2\nOutput: {updated_sumCalibrationValues(puzzle_input)}")


def readFile():
    name = 'day_one_input.txt'
    with open(name, 'r') as file:
        return "".join(file.readlines()).split("\n")


def sum_calibrationValues(puzzle_input):
    SumCalibrationValues = 0
    for line in puzzle_input:
        p_front = 0
        p_end = len(line) - 1
        while not is_num(line[p_front]) or not is_num(line[p_end]):
            if not is_num(line[p_front]):
                p_front += 1
            if not is_num(line[p_end]):
                p_end -= 1
        SumCalibrationValues += int(line[p_front] + line[p_end])
    return SumCalibrationValues


def updated_sumCalibrationValues(puzzle_input):
    SumCalibrationValues = 0
    num = 1
    for line in puzzle_input:
        front_num, back_num = -1, -1
        p_front = 0
        p_end = len(line) - 1
        p_back_of_front, p_front_of_back = 0, len(line) - 1
        while front_num == -1:
            while p_front - p_back_of_front <= 5:
                if is_word_num(line[p_back_of_front:p_front]):
                    front_num = convert_word_to_num(line[p_back_of_front:p_front])
                    break
                elif is_num(line[p_back_of_front]):
                    front_num = int(line[p_front])
                    break  # Exists the Loop
                p_front += 1
            else:
                p_back_of_front += 1
                p_front = p_back_of_front
        while back_num == -1:
            while p_front_of_back - p_end < 5:
                if is_word_num(line[p_end:p_front_of_back+1]):
                    back_num = convert_word_to_num(line[p_end:p_front_of_back+1])
                    break
                elif is_num(line[p_front_of_back]):
                    back_num = int(line[p_end])
                    break  # Exists the Loop
                p_end -= 1
            else:
                p_front_of_back -= 1
                p_end = p_front_of_back
        # Computes the Calibration of line
        SumCalibrationValues += int(str(front_num) + str(back_num))
        num += 1
    return SumCalibrationValues

    ## Helper Functions


def is_num(char):
    if char in tuple(map(str, range(1, 10))):
        return True
    return False


def is_word_num(word):
    valid_3_letter = ("one", "two", "six")
    valid_4_letter = ("four", "five", "nine")
    valid_5_letter = ("three", "seven", "eight")
    if len(word) > 5:
        return None

    if len(word) == 5:
        if word.lower() in valid_5_letter:
            return True

    elif len(word) == 4:
        if word.lower() in valid_4_letter:
            return True

    elif len(word) == 3:
        if word.lower() in valid_3_letter:
            return True

    return False


def convert_word_to_num(word):
    if word.lower() == "one":
        return 1
    elif word.lower() == "two":
        return 2
    elif word.lower() == "three":
        return 3
    elif word.lower() == "four":
        return 4
    elif word.lower() == "five":
        return 5
    elif word.lower() == "six":
        return 6
    elif word.lower() == "seven":
        return 7
    elif word.lower() == "eight":
        return 8
    elif word.lower() == "nine":
        return 9
    return -1

    ## Output


if __name__ == '__main__':
    main()
