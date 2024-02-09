from piece import Piece


class Empty(Piece):
    def possible_moves(self):
        return []

    def can_move(self, destination):
        return False
