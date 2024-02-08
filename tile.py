from piece import Piece


class Tile:
    def __init__(self, color: bool, piece: Piece, position):
        # True for white, False for black
        self.color = color
        self.piece = piece
        self.position = position

    def __str__(self):
        row = self.position[0] + 1
        col = chr(self.position[1] + 97)
        if self.piece != None:
            color = "White" if self.piece.color == True else "Black"
            piece_type = self.piece.__class__.__name__
        else:
            color = ''
            piece_type = ''

        return f"{col}{row}  {color} {piece_type}".ljust(16)
