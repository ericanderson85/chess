from piece import Piece


class Queen(Piece):
    # TODO
    def can_move(self, new_position):
        dx = abs(new_position[0] - self.position[0])
        dy = abs(new_position[1] - self.position[1])
        # queen check movement prototype
        if(((dy > 0 and dx != 0) or (dx > 0 and dy != 0)) and dy != dx):
            return False
        return True
