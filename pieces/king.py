from piece import Piece


class King(Piece):
    def can_move(self, new_position):
        return new_position in self.possible_moves()

    def possible_moves(self):
        row, col = self.position
        moves = [
            (row + 1, col + 1), (row + 1, col - 1),
            (row, col + 1), (row, col - 1),
            (row - 1, col + 1), (row - 1, col - 1),
            (row - 1, col), (row - 1, col)
        ]

        # Remove moves out of bounds
        moves = [move for move in moves if 0 <=
                 move[0] <= 7 and 0 <= move[1] <= 7]

        return moves
