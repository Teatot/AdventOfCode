def main():
    puzzle_input = readFile()
    # Output
    print("Task 1 | Output: ", end="")
    print(sum_rating_nums(*puzzle_input))


def readFile():
    name = "day_nineteen_input.txt"
    instruct, products = {}, []
    with open(name, 'r') as file:
        for line in file.readlines():
            if line[0] != "{" and line[0] != "\n":  # Instructions
                label, conditions = line.strip().strip("}").split("{")
                instruct[label] = conditions.split(",")
            elif line[0] != "\n":  # Products
                prod = {}
                for item in line.strip().strip("{").strip("}").split(","):
                    label, val = item.split("=")
                    prod[label] = int(val)
                products.append(prod)
    return instruct, products


def sum_rating_nums(instruct, products):
    rating_total = 0
    for prod in products:
        status = determine_status(instruct, prod)
        if status == 'A':
            for key, item in prod.items():
                rating_total += item
    return rating_total


def determine_status(instruct, cur_prod):
    next_it = 'in'
    while next_it not in ('A', 'R'):
        cur_inspection = instruct[next_it]
        for cond in cur_inspection:
            cond_label, cond_val, cond_symbol, tag = decomp(cond)
            if cond_symbol == ">":
                if cur_prod[cond_label] > cond_val:
                    next_it = tag
                    break
            elif cond_symbol == "<":
                if cur_prod[cond_label] < cond_val:
                    next_it = tag
                    break
            else:
                next_it = tag
    return next_it


def decomp(condition):
    ziped = condition.split(":")
    if len(ziped) <= 1:
        return None, None, None, ziped[0]
    info, tag = ziped
    for sym in info:
        if sym == ">":
            label, val = info.split(">")
            val = int(val)
            return label, val, '>', tag
        elif sym == "<":
            label, val = info.split("<")
            val = int(val)
            return label, val, '<', tag


if __name__ == "__main__":
    main()