from piece import Piece


class Queen(Piece):
    def can_move(self, new_position):
        for move_list in self.possible_moves():
            if new_position in move_list:
                return True
        return False

    def possible_moves(self):
        row, col = self.position
        # 8 lists for 8 directions
        moves = [
            [], [], [], [], [], [], [], []
        ]

        for i in range(1, 8):
            moves[0].append((row+i, col+i))
            moves[1].append((row+i, col))
            moves[2].append((row+i, col-i))
            moves[3].append((row, col + i))
            moves[4].append((row, col - i))
            moves[5].append((row-i, col+i))
            moves[6].append((row-i, col))
            moves[7].append((row-i, col-i))

        # Remove moves out of bounds
        moves = [[move for move in move_list if 0 <=
                 move[0] <= 7 and 0 <= move[1] <= 7] for move_list in moves]

        return moves
