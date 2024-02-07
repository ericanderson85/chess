from tile import Tile


class Board:
    def __init__(self):
        self.tiles = []
        color = False
        for row in range(8):
            # Add a list representing a new row
            row = []
            for col in '':
                color = not color
                tile = Tile(color=color, piece=None,
                            position=[row, col])
                row.append(tile)
            self.tiles.append(row)

    def __str__(self):
        string_representation_of_board = ""
        for row in self.tiles:
            for tile in row:
                string_representation_of_board += tile.__str__() + "\n"
        return string_representation_of_board

    def get_tile(self, position):
        return self.tiles[position[0], position[1]]

    def has_piece(self, tile: Tile):
        return tile.piece != None

    def piece_color(self, position):
        tile = self.get_tile(position)
        if not self.has_piece(tile):
            raise Exception(f"No piece at {position}")
        return tile.piece.color

    def piece_type(self, tile):
        return


board = Board()
print(board)
