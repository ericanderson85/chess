from square import Square


class Board:
    def __init__(self):
        self.squares = []
        color = False
        for row in range(1, 9):
            # Add a list representing a new row
            self.squares.append([])
            for col in 'abcdefgh':
                color = not color
                square = Square(color=color, piece=None,
                                position=f"{col}{row}")
                self.squares[row - 1].append(square)

    def __str__(self):
        string_representation_of_board = ""
        for row in self.squares:
            for square in row:
                string_representation_of_board += square.__str__() + "\n"
        return string_representation_of_board


board = Board()
print(board)
