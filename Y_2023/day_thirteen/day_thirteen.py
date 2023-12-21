def main():
    puzzle_input = readFile()
    # Task 1:
    print("Task 1 | Output: ", end="")
    total_notes, past_vertical_lines, past_horizontal_lines = summary_notes(puzzle_input.copy())
    print(total_notes)
    # Task 2
    print("Task 2 | Output: ", end="")
    print(new_summary_notes(puzzle_input.copy(), past_vertical_lines, past_horizontal_lines))


def readFile():
    name = "day_thirteen_input.txt"
    clusters = []
    with open(name, 'r') as file:
        sector = []
        for section in file.readlines():
            if section != "\n":
                sector.append([x for x in section.strip()])
            else:
                clusters.append(sector)
                sector = []
        clusters.append(sector)
    return clusters


def summary_notes(clusters):
    notes = 0
    cur_vertical_lines, cur_horizontal_lines = [], []
    for sector in clusters:
        maxrow, maxcol = len(sector), len(sector[0])
        # Horizontal
        horizontal_symmetry = -1
        for i in range(maxrow - 1):
            if horizontal_sym(sector.copy(), i):
                horizontal_symmetry = i + 1
                cur_horizontal_lines.append(i + 1)
                cur_vertical_lines.append(0)
                break
        # Vertical
        transpose_sector = transpose(sector)
        vertical_symmetry = -1
        for j in range(maxcol - 1):
            if horizontal_sym(transpose_sector.copy(), j):
                vertical_symmetry = j + 1
                cur_horizontal_lines.append(0)
                cur_vertical_lines.append(j + 1)
                break

        notes += horizontal_symmetry * 100 if horizontal_symmetry != -1 else vertical_symmetry
    return notes, cur_vertical_lines, cur_horizontal_lines


def new_summary_notes(clusters, past_vert_lines, past_hori_lines):
    notes = 0
    for sector in clusters:
        maxrow, maxcol = len(sector), len(sector[0])
        # Horizontal
        horizontal_symmetry = -1
        past_h_line = past_hori_lines.pop(0)
        for i in range(maxrow - 1):
            if dynamic_horizontal_sym(sector.copy(), i) and i + 1 != past_h_line:
                horizontal_symmetry = i + 1
                break
        # Vertical
        transpose_sector = transpose(sector)
        past_v_line = past_vert_lines.pop(0)
        vertical_symmetry = -1
        for j in range(maxcol - 1):
            if dynamic_horizontal_sym(transpose_sector.copy(), j) and j + 1 != past_v_line:
                vertical_symmetry = j + 1
                break

        notes += (horizontal_symmetry * 100) if horizontal_symmetry != -1 else vertical_symmetry
    return notes

    # Helper Functions


def transpose(matrix):
    return list(map(list, list(zip(*matrix))))


def horizontal_sym(sector, half_range):
    maxrow, maxcol = len(sector), len(sector[0])
    full_range = 2 * half_range
    for r1 in range(maxrow):
        mirrored_r1 = full_range + 1 - r1
        if mirrored_r1 >= maxrow or mirrored_r1 < 0:
            continue
        if sector[r1] != sector[mirrored_r1]:
            return False
    return True


def dynamic_horizontal_sym(sector, half_range):
    clear_smudge = False
    maxrow, maxcol = len(sector), len(sector[0])
    full_range = 2 * half_range
    for r1 in range(maxrow):
        mirrored_r1 = full_range + 1 - r1
        difference = 0
        if mirrored_r1 >= maxrow or mirrored_r1 < 0:
            continue
        for c in range(maxcol):
            if sector[r1][c] != sector[mirrored_r1][c] and clear_smudge is False:
                difference += 1
            elif sector[r1][c] != sector[mirrored_r1][c] and clear_smudge is True:
                return False
        if clear_smudge is False:
            if difference > 1:
                return False
            else:
                for c in range(maxcol):  # NOT EFFICIENT - Going through the list and changing to be the same
                    if sector[r1][c] != sector[mirrored_r1][c]:
                        sector[r1][c] = sector[mirrored_r1][c]
                        break

    return True


# Debugging Functions

def print_region(sector):
    for row in sector:
        print("".join(row))


if __name__ == '__main__':
    main()
