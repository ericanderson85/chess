from piece import Piece
from board import Board as b


class Bishop(Piece):
    def can_move(self, new_position):
        if b.get_piece_color(new_position) == self.color:
            raise Exception("Tile has same color piece")
        # Bishop can move diagonally
        dx = abs(new_position[0] - self.position[0])
        dy = abs(new_position[1] - self.position[1])
        return dx == dy
