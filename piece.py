from abc import ABC, abstractmethod


class Piece(ABC):
    def __init__(self, color, position):
        # True for white, False for black
        self.color = color
        self.position = position

    @abstractmethod
    def can_move(self, new_position):
        pass
