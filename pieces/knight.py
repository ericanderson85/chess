from piece import Piece


class Knight(Piece):
    def can_move(self, new_position):
        # Knight moves in L shape, either change in x = 1 and change in y = 2 or vice versa
        dx = abs(new_position[0] - self.position[0])
        dy = abs(new_position[1] - self.position[1])
        return (dx == 2 and dy == 1) or (dx == 1 and dy == 2)
