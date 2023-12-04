def main():
    puzzle_input = readFile()
    # Task 1
    print(f"Task 1\nOutput: {calculatePoints(puzzle_input)}")
    # Task 2
    print(f"Task 2\nOutput: {calculateCards(puzzle_input)}")


def readFile():
    name = "day_four_input.txt"
    game = []
    with open(name, 'r') as file:
        for line in file.readlines():
            data = line.split(":")[-1]
            winning_num = sorted(list(map(int, (data.split("|")[0]).split())))
            given_num = sorted(list(map(int, (data.split("|")[-1]).split())))
            game.append((winning_num, given_num))
    return game


def calculatePoints(cards):
    total_points = 0
    for card in cards:
        amount_of_wins = get_wins(card)
        if amount_of_wins > 0:
            total_points += 2 ** (amount_of_wins - 1)
    return total_points


def calculateCards(cards):
    card_copies = len(cards)  # Initially have n cards
    for num in range(len(cards)):
        amount_wins = get_wins(cards[num])  # Calculate the cards won for the specific card
        card_copies += amount_wins  # Add the amount of cards won to the total
        card_copies += get_copies(cards, num, amount_wins)  # Calculates the copies of cards won from the cards won
    return card_copies

    # Helper Functions


def get_wins(card):
    amount_wins = 0
    win_nums, giv_nums = card[0], card[-1]
    p_win, p_giv = 0, 0
    while p_giv < len(giv_nums):
        if p_win >= len(win_nums):
            break

        elif giv_nums[p_giv] == win_nums[p_win]:  # A Winner Number is Spotted
            amount_wins += 1
            p_giv += 1

        elif giv_nums[p_giv] < win_nums[p_win]:
            p_giv += 1

        else:
            p_win += 1
    return amount_wins


def get_copies(cards, start, amount):
    total = 0
    if amount == 0:  # When the given card does not produce any winners, there no more copies
        return amount
    for i in range(1, amount + 1):
        cur_wins = get_wins(cards[start + i])
        total += cur_wins
        total += get_copies(cards, start + i, cur_wins)
    return total


if __name__ == '__main__':
    main()
