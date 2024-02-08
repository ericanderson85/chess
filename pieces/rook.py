from piece import Piece


class Rook(Piece):
    # TODO
    def can_move(self, new_position):
        # moves horizontally
        dx = abs(new_position[0] - self.position[0])
        # move vertically
        dy = abs(new_position[1] - self.position[1])
        if((dy > 0 and dx != 0) or dx > 0 and dy != 0):
            return False
        else:
            return True


