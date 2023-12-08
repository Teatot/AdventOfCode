def main():
    puzzle_input = readFile()
    # Task 1
    print(f"Task 1\nOutput: {calculate_total_winnings_v1(puzzle_input.copy())}")
    # Task 2
    print(f"Task 2\nOutput: {calculate_total_winnings_v2(puzzle_input)}")


def readFile():
    name = "day_seven_input.txt"
    hand_bets = {}
    with open(name, 'r') as file:
        for line in file.readlines():
            hand, bets = line.split()
            hand_bets[hand] = int(bets)
    return hand_bets


def calculate_total_winnings_v1(hand):
    hand_types = []
    for card_set in hand.keys():
        set_type = calculate_type(card_set)
        hand_types.append((card_set, set_type))
    sorted_hand_by_types = sort_on_types(hand_types)
    # Sorting Each Hand for Each Type
    # Ranked from Lowest to Highest (Respectively)
    true_sorted = []
    possible_types = ("high_card", "one_pair", "two_pair", "three_kind", "full_house", "four_kind", "five_kind")
    for htype in possible_types:
        cur_list = sort_on_cards(create_sub_list(sorted_hand_by_types, htype))
        if cur_list is not None:
            true_sorted.extend(cur_list)
    # Calculating Total Winnings
    total_wins = 0
    for i in range(len(true_sorted)):
        card_hand = true_sorted[i][0]
        bet_price = hand[card_hand]
        total_wins += bet_price * (i + 1)
    return total_wins


def calculate_total_winnings_v2(hand):
    hand_types = []
    for card_set in hand.keys():
        set_type = calculate_type(card_set, maximize=True)
        hand_types.append((card_set, set_type))
    sorted_hand_by_types = sort_on_types(hand_types)
    # Sorting Each Hand for Each Type
    # Ranked from Lowest to Highest (Respectively)
    true_sorted = []
    possible_types = ("high_card", "one_pair", "two_pair", "three_kind", "full_house", "four_kind", "five_kind")
    valid_ref = ('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
    for htype in possible_types:
        cur_list = sort_on_cards(create_sub_list(sorted_hand_by_types, htype), valid_ref)
        if cur_list is not None:
            true_sorted.extend(cur_list)
    # Calculating Total Winnings
    total_wins = 0
    for i in range(len(true_sorted)):
        card_hand = true_sorted[i][0]
        bet_price = hand[card_hand]
        total_wins += bet_price * (i + 1)
    return total_wins

    # Helper Functions


def get_unique_cards(hand):
    unique_cards = {}
    for card in hand:
        unique_cards[card] = unique_cards.get(card, 0) + 1
    return unique_cards


def most_cards(num_of_cards):
    popular_card = None
    for card, num in num_of_cards.items():
        if popular_card is None:
            popular_card = card
        elif num_of_cards[popular_card] < num:
            popular_card = card
    return popular_card


def calculate_type(cards, maximize=False):
    sorted_order = sorted(cards)
    # Special Case - If Maximize
    if maximize:
        num_of_cards = get_unique_cards(cards)
        if num_of_cards.get('J', -1) != -1:  # If "J" is in the Hand
            common_card = most_cards(num_of_cards)
            num_of_cards.pop("J")
            if len(num_of_cards) > 0:
                common_card = most_cards(num_of_cards)
            sorted_order = sorted("".join([card if card != 'J' else common_card for card in cards]))
    num_of_groups = 0
    set_of_cards = set()
    p_start, p_end = 0, 0
    while p_start <= p_end < len(sorted_order):
        if sorted_order[p_start] != sorted_order[p_end]:
            set_of_cards.add(sorted_order[p_start])
            if p_end - p_start > 1:  # A Pair or a group
                num_of_groups += 1
            p_start = p_end
        else:
            p_end += 1
    set_of_cards.add(sorted_order[p_start])
    if p_end - p_start > 1:
        num_of_groups += 1
    # Type Checking
    if len(set_of_cards) == 5:  # 'High Card'
        return "high_card"
    elif len(set_of_cards) == 4 and num_of_groups == 1:  # One Pair
        return "one_pair"
    elif len(set_of_cards) == 3 and num_of_groups == 2:  # Two Pair
        return "two_pair"
    elif len(set_of_cards) == 3 and num_of_groups == 1:  # Three of a Kind
        return "three_kind"
    elif len(set_of_cards) == 2 and num_of_groups == 2:  # Full House
        return "full_house"
    elif len(set_of_cards) == 2 and num_of_groups == 1:  # Four of a kind
        return "four_kind"
    elif len(set_of_cards) == 1:  # Five of a Kind
        return "five_kind"


def sort_on_types(hand_types):  # Insertion Sort
    # Ranked from Lowest to Highest (Respectively)
    possible_types = ("high_card", "one_pair", "two_pair", "three_kind", "full_house", "four_kind", "five_kind")
    for i in range(1, len(hand_types)):
        j = i - 1
        ckey = hand_types[i]
        while j >= 0 and possible_types.index(ckey[-1]) < possible_types.index(hand_types[j][-1]):
            hand_types[j + 1] = hand_types[j]
            j -= 1
        hand_types[j + 1] = ckey
    return hand_types


def sort_on_cards(hand_types, data=('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')):
    for i in range(1, len(hand_types)):
        j = i - 1
        key = hand_types[i]
        while j >= 0 and check_card_descending_order(key[0], hand_types[j][0], data):
            hand_types[j + 1] = hand_types[j]
            j -= 1
        hand_types[j + 1] = key
    return hand_types


def check_card_descending_order(card1, card2, card_order):
    p = 0
    while p < len(card1) and p < len(card2) and card1[p] == card2[p]:
        p += 1
    if p < 5 and card_order.index(card1[p]) > card_order.index(card2[p]):  # Lower Order than Card 2
        return True
    return False


def create_sub_list(hand, htype):
    start, end = find_interval(hand, htype)
    return hand[start:end]


def find_interval(hand, hand_type):
    start, end = False, False
    for ind in range(len(hand)):
        if hand[ind][-1] == hand_type:
            if start is False:
                start = ind
        elif start is not False:
            end = ind
            break
    if start is not False and end is False:
        end = len(hand)
    return start, end


if __name__ == '__main__':
    main()
