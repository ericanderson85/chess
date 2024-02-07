from piece import Piece


class Bishop(Piece):
    def can_move(self, new_position):
        # Bishop can move diagonally, so change in x must == change in y
        dx = abs(new_position[0] - self.position[0])
        dy = abs(new_position[1] - self.position[1])
        return dx == dy
