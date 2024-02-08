from piece import Piece


class Pawn(Piece):
    # TODO
    def can_move(self, new_position):
        dx = abs(new_position[0] - self.position[0])
        dy = abs(new_position[1] - self.position[1])
        # check if incorrect dy change and if dy and dx change accordingly
        if (dy > 3 or (dy >=2 and dx > 1)):
            return False
        # check if moved on row 1 or row 7
        if (self.position[0] == 7 and not self.color and dy == 2):
            return True
        if (self.position[0] == 1 and self.color and dy == 2):
            return True
        # lastly check if dx change illegaly
        if(dx >= 1 and dy == 0):
            return False
        return True
        


