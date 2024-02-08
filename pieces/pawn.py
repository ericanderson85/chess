from piece import Piece


class Pawn(Piece):
    def can_move(self, new_position):
        return new_position in self.possible_moves()

    def possible_moves(self):
        row, col = self.position
        moves = []

        if self.color:  # Pawn is white
            if row == 1:  # Pawn can move twice
                moves.append((row + 2, col))
            moves.append((row + 1, col))
            moves.append((row + 1, col + 1))
            moves.append((row + 1, col - 1))
        else:  # Pawn is black
            if row == 6:  # Pawn can move twice
                moves.append((row - 2, col))
            moves.append((row - 1, col))
            moves.append((row - 1, col + 1))
            moves.append((row - 1, col - 1))

        # Remove moves out of bounds
        moves = [move for move in moves if 0 <=
                 move[0] <= 7 and 0 <= move[1] <= 7]

        return moves
