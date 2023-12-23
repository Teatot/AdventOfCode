# Using Shoelace Theorem to Solve for the Area
def main():
    puzzle_input = readFile()
    # Output
    print("Task 2 | Output: ", end="")
    print(compute_area(puzzle_input))


def readFile():
    name = "day_eighteen_input.txt"
    instruct = []
    with open(name, 'r') as file:
        for line in file.readlines():
            instruct.append(line.strip().split()[-1].strip("(").strip(")").strip("#"))
    return instruct


def compute_area(hexadecimals):
    instruct = compute_instructions(hexadecimals)
    vertices = compute_vertices(instruct)
    return shoelace_formula(instruct, vertices)


def shoelace_formula(instruct, vertices):
    sumd = 0
    for i in range(1, len(vertices)):
        x, y = vertices[i - 1], vertices[i]
        sumd += (x[0] * y[1] - x[1] * y[0])
    return (abs(sumd) + compute_boarder_length(instruct)) / 2 + 1


def compute_vertices(instruct):  # Left and Up - Negative (Coordinate System)
    cur_x, cur_y = 0, 0
    vertices = []
    for direct, val in instruct:
        if direct == "R":
            cur_y += val
        elif direct == "L":
            cur_y -= val
        elif direct == "U":
            cur_x += val
        elif direct == "D":
            cur_x -= val
        vertices.append((cur_x, cur_y))
    return vertices


def compute_boarder_length(instruct):
    boarder_length = 0
    for direct, val in instruct:
        boarder_length += val
    return boarder_length


def compute_instructions(hexa):
    instructions = []
    for num in hexa:
        direction, value = compute_direction(int(num[-1])), hexa_to_decimal(num[:-1])
        instructions.append((direction, value))
    return instructions


def compute_direction(num):
    assert num in range(4)
    if num == 0:
        return "R"
    if num == 1:
        return "D"
    if num == 2:
        return "L"
    return "U"


def hexa_to_decimal(string):
    return int(string, 16)


if __name__ == '__main__':
    main()
