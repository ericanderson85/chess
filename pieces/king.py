from piece import Piece


class King(Piece):
    def can_move(self, new_position):
        # King can move one column and one row at a time
        dx = abs(new_position[0] - self.position[0])
        dy = abs(new_position[1] - self.position[1])
        return dx <= 1 and dy <= 1
