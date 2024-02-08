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
        # Mapping of column index to piece
        piece_map = {
            0: Rook,
            1: Knight,
            2: Bishop,
            3: Queen,
            4: King,
            5: Bishop,
            6: Knight,
            7: Rook
        }

        for row in range(8):
            current_row = []
            for col in range(8):
                piece = None  # Default to no piece

                # True for white (rows 0-1), False for black (rows 6-7)
                color = row < 5

                # Pawns
                if row in [1, 6]:
                    piece = Pawn(color=color, position=[row, col])

                # Other pieces
                elif row in [0, 7] and col in piece_map:
                    piece = piece_map[col](color=color, position=[row, col])

                tile = Tile(piece=piece, position=[row, col])
                current_row.append(tile)
            self.tiles.append(current_row)

    def __str__(self):
        string_representation_of_board = ""
        for row in range(1, 9):
            for tile in self.tiles[8 - row]:
                string_representation_of_board += str(tile) + " "
            string_representation_of_board += "\n"
        return string_representation_of_board

    def get_tile(self, position):
        return self.tiles[position[0]][position[1]]

    def piece_type(self, tile: Tile):
        return tile.get_piece().__class__.__name__

    def update_attacking(self, tile: Tile, destination: Tile):
        pass

    def is_legal_move(self, tile: Tile, destination: Tile):
        destination_position = destination.get_position()

        # If the destination is outside of the board
        if 0 > destination_position[0] > 7 or 0 > destination_position[1] > 7:
            raise Exception("Illegal destination")

        # If the piece can not move in that way
        if not tile.piece.can_move(destination_position):
            return False

        # If there is a piece at destination and it is the same color as the piece being moved
        if destination.has_piece() and destination.get_piece().get_color() == tile.get_piece().get_color():
            return False

        # TODO Logic for checking if the move puts king in check

        return True

    def move_piece(self, tile: Tile, destination: Tile):
        if not tile.has_piece():
            raise Exception(f"No piece at {tile.get_position}")

        if not self.is_legal_move(tile, destination):
            raise Exception(f"Can't move to that destination")

        # Set destination tile to the piece being moved
        destination.set_piece(tile.piece)

        # Tile being moevd from to none
        tile.remove_piece()


board = Board()
print(board)
