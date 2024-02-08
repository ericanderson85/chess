from piece import Piece


class Pawn(Piece):
    def can_move(self, new_position):

        row = self.position[0]
        col = self.position[1]

        dy = abs(new_position[0] - row)  # Absolute change in row
        dx = new_position[1] - col  # Change in column

        if self.color == True:  # The pawn is white
            if row == 1:  # Pawn is on row 1
                if dy != 1 and dy != 2:  # Can only move up 1 or up 2
                    return False
            else:
                if dy != 1:  # dy can only be 1 on any other row
                    return False
        else:  # The pawn is black
            if row == 6:  # Pawn is on row 6
                if dy != -1 and dy != -2:  # Can only move down 1 or down 2
                    return False
            else:
                if dy != -1:  # dy can only be -1 on any other row
                    return False
        # Can only move left or right once, can't move twice and diagonally
        if dx > 1 or abs(dy) == 2 and dx > 0:
            return False

        return True
