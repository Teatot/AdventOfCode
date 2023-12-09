import math
def main():
    instructions, nodes = readFile()  # Ins - String | Nod - Dictionary
    # Task 1
    print(f"Task 1\nOutput: {num_required_steps(instructions, nodes)}")
    # Task 2
    print(f"Task 2\nOutput: {true_num_required_steps(instructions, nodes)}")


def readFile():
    name = "day_eight_input.txt"
    instructions = ""
    nodes = {}
    with open(name, 'r') as file:
        first_line = True
        for line in file.readlines():
            if first_line:
                instructions = "".join(line.split())
                first_line = False
            elif line[0] != '\n':
                temp = ("".join(line.split())).split('=')
                key = temp[0]
                pair = "".join([r if r != '(' and r != ')' else "" for r in temp[1]]).split(',')
                nodes[key] = nodes.get(key, pair)
    return instructions, nodes


def num_required_steps(instructions, nodes):
    total_steps = 0
    cur_node = 'AAA'
    while True:
        for direction in instructions:
            if direction == 'L':
                cur_node = nodes[cur_node][0]
            else:
                cur_node = nodes[cur_node][1]
            total_steps += 1
            if cur_node == 'ZZZ':
                return total_steps


def true_num_required_steps(instructions, nodes):
    starting_nodes = find_starting_nodes(nodes)
    total_steps = [find_num_steps(instructions, nodes, node) for node in starting_nodes]
    return math.lcm(*total_steps)  # Unpack


def find_num_steps(instructions, nodes, start_node):
    total_steps = 0
    cur_node = start_node
    while True:
        for direction in instructions:
            if direction == "L":
                cur_node = nodes[cur_node][0]
            else:
                cur_node = nodes[cur_node][1]
            total_steps += 1
            if cur_node[-1] == "Z":
                return total_steps


def find_starting_nodes(nodes):
    start_nodes = []
    for node in nodes.keys():
        if node[-1] == 'A':
            start_nodes.append(node)
    return start_nodes


if __name__ == '__main__':
    main()
