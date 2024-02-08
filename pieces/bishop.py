from piece import Piece


class Bishop(Piece):
    def can_move(self, new_position):
        return new_position in self.possible_moves()

    def possible_moves(self):
        row, col = self.position
        moves = []

        for i in range(1, 8):
            moves.append((row+i, col+i))
            moves.append((row+i, col-i))
            moves.append((row-i, col+i))
            moves.append((row-i, col-i))

        # Remove moves out of bounds
        moves = [move for move in moves if 0 <=
                 move[0] <= 7 and 0 <= move[1] <= 7]

        return moves
