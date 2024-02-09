from abc import ABC, abstractmethod


class Piece(ABC):
    def __init__(self, color, position):
        # True for white, False for black
        self.color = color
        self.position = position

    def get_color(self):
        return self.color

    def get_position(self):
        return self.position

    def set_position(self, position):
        # Position must be on the board
        if not (0 <= position[0] <= 7 and 0 <= position[1] <= 7):
            raise Exception("Illegal position")

        self.position = position

    def get_type(self):
        return self.__class__.__name__

    @abstractmethod
    def can_move(self, new_position):
        pass

    @abstractmethod
    def possible_moves(self):
        pass
