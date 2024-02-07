from tile import Tile
from pieces.bishop import Bishop
from pieces.pawn import Pawn
from pieces.king import King
from pieces.queen import Queen
from pieces.rook import Rook
from pieces.knight import Knight


class Board:
    def __init__(self):
        self.tiles = []
        for row in range(8):
            # Add a list representing a new row
            current_row = []
            for col in range(8):
                # Determine if the tile is white or black
                color = (row + col) % 2 == 0
                piece = None  # Default empty

                # Initialize pawns
                if row == 1:
                    piece = Pawn(color=True, position=[row, col])
                elif row == 6:
                    piece = Pawn(color=False, position=[row, col])

                # Initialize other pieces on the 1st and 8th rows
                if row in [0, 7]:
                    color_bool = row == 0  # False for black, True for white
                    if col in [0, 7]:
                        piece = Rook(color=color_bool, position=[row, col])
                    elif col in [1, 6]:
                        piece = Knight(color=color_bool, position=[row, col])
                    elif col in [2, 5]:
                        piece = Bishop(color=color_bool, position=[row, col])
                    elif col == 3:
                        # Queen on its color
                        if (color and row == 7) or (not color and row == 0):
                            piece = Queen(color=color_bool,
                                          position=[row, col])
                    elif col == 4:
                        piece = King(color=color_bool, position=[row, col])

                tile = Tile(color=color, piece=piece, position=[row, col])
                current_row.append(tile)
            self.tiles.append(current_row)

    def __str__(self):
        string_representation_of_board = ""
        for row in range(1, 9):
            for tile in self.tiles[8 - row]:
                string_representation_of_board += tile.__str__() + " "
            string_representation_of_board += "\n"
        return string_representation_of_board

    def get_tile(self, position):
        return self.tiles[position[0]][position[1]]

    def has_piece(self, tile: Tile):
        return tile.piece != None

    def piece_color(self, tile: Tile):
        if not self.has_piece(tile):
            raise Exception("There is no piece at the tile")

        # Returns the color of the piece at the tile (True = white, False = black)
        return tile.piece.color

    def piece_type(self, tile: Tile):
        return tile.piece.__class__.__name__

    def can_move(self, tile: Tile, destination):
        if 0 > destination[0] > 7 or 0 > destination[1] > 7:
            raise Exception("Illegal destination")
        # If the piece can not move in that way
        if not tile.piece.can_move(destination):
            return False
        destination_tile = self.get_tile(destination)
        # If there is a piece at destination and it is the same color as the piece being moved
        if self.has_piece(destination_tile) and self.piece_color(destination_tile) == tile.piece.color:
            return False

        # TODO Logic for checking if the move puts king in check

        return True

    def move_piece(self, tile: Tile, destination):
        if not self.has_piece(tile):
            raise Exception(f"No piece at {tile.position}")
        if not self.can_move(tile, destination):
            raise Exception(f"Can't move to that destination")
        # Set destination tile to the piece being moved
        self.get_tile(destination).piece = tile.piece
        # Tile being moevd from to none
        tile.piece = None


board = Board()
print(board)
board.move_piece(board.get_tile([0, 1]), [2, 2])
print(board)
