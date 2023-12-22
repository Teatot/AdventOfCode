def main():
    puzzle_input = readFile()
    print("Task 2 | Output: ", end="")
    print(compute_focusing_power(puzzle_input.copy()))


def readFile():
    name = "day_fifteen_input.txt"
    with open(name, 'r') as file:
        entry = "".join(file.readlines()).strip().split(",")
        return entry


def compute_focusing_power(configurations):
    box_storage = {}
    for order in configurations:
        if task_type(order) == "-":
            cur_box_num = HASH_algorithm(order[:-1])
            if box_storage.get(cur_box_num, -1) != -1 and item_inside(box_storage[cur_box_num], order[:-1]):
                remove_item(box_storage[cur_box_num], order[:-1])
                if len(box_storage[cur_box_num]) < 1:
                    box_storage.pop(cur_box_num)
        else:
            label, focal_len = order.split('=')
            cur_box_num = HASH_algorithm(label)
            if box_storage.get(cur_box_num, -1) != -1 and item_inside(box_storage[cur_box_num], label):  # Box num DNE
                update_item(box_storage[cur_box_num], label, focal_len)
            else:
                insert_element(box_storage, cur_box_num, [label, focal_len])
    focal_power = calculate_focal_power(box_storage)
    return focal_power


def calculate_focal_power(dict_type):
    total_power = 0
    for key, items in dict_type.items():
        for i in range(len(items)):
            total_power += (key + 1) * (i + 1) * int(items[i][1])
    return total_power


def insert_element(dictionary, key, item):
    if dictionary.get(key, -1) == -1:
        dictionary[key] = [item]
        return
    new_item = dictionary.get(key)
    new_item.append(item)
    dictionary[key] = new_item
    return


def remove_item(box, element):
    for i in range(len(box)):
        if box[i][0] == element:
            box.remove(box[i])
            return


def update_item(box, element, data):
    for j in range(len(box)):
        if box[j][0] == element:
            box[j][1] = data
            return


def HASH_algorithm(seq):
    cur_val = 0
    for ch in seq:
        cur_val = ((cur_val + ord(ch)) * 17) % 256
    return cur_val


def item_inside(box, element):
    for entry in box:
        if entry[0] == element:
            return True
    return False


def task_type(sequence):
    assert '-' in sequence or '=' in sequence
    if "-" in sequence:
        return '-'
    if '=' in sequence:
        return '='


if __name__ == '__main__':
    main()
