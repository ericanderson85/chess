from piece import Piece


class Tile:
    def __init__(self, color: bool, piece: Piece, position):
        self.color = color
        self.piece = piece
        self.position = position

    def __str__(self):
        return f"{self.piece} at {self.color} tile {self.position}"
