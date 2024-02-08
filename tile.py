from piece import Piece


class Tile:
    def __init__(self, piece: Piece, tile_position):
        self.piece = piece
        self.tile_position = tile_position

    def __str__(self):
        row = self.tile_position[0] + 1
        # Convert int(0-7) to char(a-h)
        col = chr(self.tile_position[1] + 97)
        if self.piece != None:
            color = "White" if self.piece.color == True else "Black"
            piece_type = self.piece.__class__.__name__
        else:
            color = ''
            piece_type = ''

        return f"{col}{row}  {color} {piece_type}".ljust(16)
