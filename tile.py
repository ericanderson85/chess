from piece import Piece
from pieces.empty import Empty


class Tile:
    def __init__(self, piece: Piece, position):
        self.piece = piece
        self.position = position
        # Positions of tiles which are attacking this tile
        self.attacked = []

    def has_piece(self):
        return self.piece != None and self.piece.get_type() != "Empty"

    def remove_piece(self):
        piece = self.piece
        self.piece = Empty(True, self.position)
        return piece

    def set_piece(self, piece: Piece):
        piece.set_position(self.position)
        self.piece = piece

    def get_piece(self):
        return self.piece

    def get_position(self):
        return self.position

    def get_attacked(self):
        return self.attacked

    def add_attacked(self, position):
        if not (0 <= position[0] <= 7 and 0 <= position[1] <= 8):
            raise Exception("Illegal position")
        self.attacked.append(position)

    def remove_attacked(self, position):
        if position not in self.attacked:
            return
        self.attacked.remove(position)

    def clear_attacked(self):
        self.attacked = []

    def set_attacked(self, attacking):
        self.attacked = attacking

    def __str__(self):
        row = self.position[0] + 1
        # Convert int(0-7) to char(a-h)
        col = chr(self.position[1] + 97)
        row = self.position[0]
        # Convert int(0-7) to char(a-h)
        col = self.position[1]
        if self.piece.get_type() != "Empty":
            color = "White" if self.piece.color == True else "Black"
            piece_type = self.piece.__class__.__name__
        else:
            color = ''
            piece_type = ''

        return f"{row} {col}  {color} {piece_type}".ljust(16)
