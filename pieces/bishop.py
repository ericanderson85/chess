from piece import Piece


class Bishop(Piece):
    def can_move(self, new_position):
        for move_list in self.possible_moves():
            if new_position in move_list:
                return True
        return False

    def possible_moves(self):
        row, col = self.position
        # 4 lists for 4 directions
        moves = [
            [], [], [], []
        ]

        for i in range(1, 8):
            moves[0].append((row+i, col+i))
            moves[1].append((row+i, col-i))
            moves[2].append((row-i, col+i))
            moves[3].append((row-i, col-i))

        # Remove moves out of bounds
        moves = [[move for move in move_list if 0 <=
                 move[0] <= 7 and 0 <= move[1] <= 7] for move_list in moves]

        return moves
