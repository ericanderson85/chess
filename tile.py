from piece import Piece


class Tile:
    def __init__(self, piece: Piece, position):
        self.piece = piece
        self.position = position
        # Attacking[0] is white tiles that are attacking it, attacking[1] is black tiles
        self.attacking = ([], [])

    def has_piece(self):
        return self.piece != None

    def remove_piece(self):
        self.piece = None

    def set_piece(self, piece: Piece):
        self.piece = piece

    def get_piece(self):
        return self.piece

    def get_position(self):
        return self.position

    def __str__(self):
        row = self.position[0] + 1
        # Convert int(0-7) to char(a-h)
        col = chr(self.position[1] + 97)
        if self.piece != None:
            color = "White" if self.piece.color == True else "Black"
            piece_type = self.piece.__class__.__name__
        else:
            color = ''
            piece_type = ''

        return f"{col}{row}  {color} {piece_type}".ljust(16)
