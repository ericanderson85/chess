class Square:
    def __init__(self, color, piece, position):
        self.color = color
        self.piece = piece
        self.position = position

    def __str__(self):
        return f"{self.piece} at {self.color} tile {self.position}"
