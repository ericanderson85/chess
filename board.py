from tile import Tile
from pieces.bishop import Bishop
from pieces.pawn import Pawn
from pieces.king import King
from pieces.queen import Queen
from pieces.rook import Rook
from pieces.knight import Knight
from pieces.empty import Empty


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

                # True for white (rows 0-1), False for black (rows 6-7)
                color = row < 5

                piece = Empty(color=color, position=[
                              row, col])  # Default to empty

                # Pawns
                if row in [1, 6]:
                    piece = Pawn(color=color, position=(row, col))

                # Other pieces
                elif row in [0, 7] and col in piece_map:
                    piece = piece_map[col](color=color, position=(row, col))

                tile = Tile(piece=piece, position=(row, col))
                current_row.append(tile)
            self.tiles.append(current_row)
        self.update_attacked()

    def __str__(self):
        string_representation_of_board = ""
        for row in range(1, 9):
            for tile in self.tiles[8 - row]:
                string_representation_of_board += str(tile) + " "
            string_representation_of_board += "\n"
        return string_representation_of_board

    def get_tile(self, position):
        return self.tiles[position[0]][position[1]]

    def update_attacked(self):
        # Reset current attacked
        for row in self.tiles:
            for tile in row:
                tile.clear_attacked()
        # king_in_check[0] is white, 1 is black
        king_in_check = [False, False]
        for row in self.tiles:
            for tile in row:
                if tile.has_piece():
                    if tile.get_piece().get_type() == "Pawn":
                        pawn_check = self.pawn_attacking(tile)
                        king_in_check[0] = pawn_check[0] or king_in_check[0]
                        king_in_check[1] = pawn_check[1] or king_in_check[1]
                    else:
                        for move in tile.get_piece().possible_moves():
                            if type(move) == list:
                                vision_check = self.vision_attacking(
                                    tile, move)
                                king_in_check[0] = king_in_check[0] or vision_check[0]
                                king_in_check[1] = king_in_check[1] or vision_check[1]
                            else:

                                attacked_tile = self.get_tile(move)
                                if attacked_tile.has_piece() and attacked_tile.get_piece().get_type() == "King":
                                    if attacked_tile.get_piece().get_color() != tile.get_piece().get_color():
                                        if attacked_tile.get_piece().get_color():
                                            king_in_check[0] = True
                                        else:
                                            king_in_check[1] = True
                                attacked_tile.add_attacked(
                                    tile.get_position())

        return king_in_check

    def pawn_attacking(self, tile):
        # Pawns only attack diagonally
        king_in_check = [False, False]

        row, col = tile.get_position()
        if tile.get_position()[1] != 0:

            other_tile1 = self.get_tile(
                (row + (1 if tile.get_piece().get_color() else -1), col - 1))
            if other_tile1.has_piece() and other_tile1.get_piece().get_type() == "King":
                if other_tile1.get_piece().get_color() != tile.get_piece().get_color():
                    if other_tile1.get_piece().get_color():
                        king_in_check[0] = True
                    else:
                        king_in_check[1] = True
            other_tile1.add_attacked(tile.get_position())

        if tile.get_position()[1] != 7:
            other_tile2 = self.get_tile(
                (row + (1 if tile.get_piece().get_color() else -1), col + 1))
            if other_tile2.has_piece() and other_tile2.get_piece().get_type() == "King":
                if other_tile2.get_piece().get_color() != tile.get_piece().get_color():
                    if other_tile2.get_piece().get_color():
                        king_in_check[0] = True
                    else:
                        king_in_check[1] = True
            other_tile2.add_attacked(tile.get_position())

        return king_in_check

    def vision_attacking(self, tile, moves):
        # The vision of rooks, queens, bishops may get cut off
        king_in_check = [False, False]

        for i in range(len(moves)):
            # If we run into a piece
            if self.get_tile(moves[i]).has_piece():
                attacked_tile = self.get_tile(moves[i])
                if attacked_tile.get_piece().get_type() == "King":
                    if attacked_tile.get_piece().get_color() != tile.get_piece().get_color():
                        if attacked_tile.get_piece().get_color():
                            king_in_check[0] = True
                        else:
                            king_in_check[1] = True
                # Stop adding after this piece
                self.get_tile(moves[i]).add_attacked(tile.get_position())

        return king_in_check

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

        return True

    def move_piece(self, tile: Tile, destination: Tile):
        if not tile.has_piece():
            raise Exception(f"No piece at {tile.get_position()}")

        if not self.is_legal_move(tile, destination):
            raise Exception(f"Can't move to that destination")

        # Check if move puts own king in check
        piece = tile.remove_piece()
        other_piece = destination.remove_piece()
        destination.set_piece(piece)
        white_king_in_check, black_king_in_check = self.update_attacked()
        if piece.get_color():
            if white_king_in_check:
                destination.set_piece(other_piece)
                tile.set_piece(piece)
                self.update_attacked()
                raise Exception("Can't put own king in check")
        elif black_king_in_check:
            destination.set_piece(other_piece)
            tile.set_piece(piece)
            self.update_attacked()
            raise Exception("Can't put own king in check")


board = Board()
print(board)
board.move_piece(board.get_tile((1, 2)), board.get_tile((3, 2)))
print(board)
board.move_piece(board.get_tile((1, 4)), board.get_tile((3, 4)))
print(board)
board.move_piece(board.get_tile((6, 4)), board.get_tile((4, 4)))
print(board)
board.move_piece(board.get_tile((7, 3)), board.get_tile((4, 6)))
print(board)
board.move_piece(board.get_tile((4, 6)), board.get_tile((2, 6)))
print(board)
board.move_piece(board.get_tile((2, 6)), board.get_tile((1, 5)))
print(board)
