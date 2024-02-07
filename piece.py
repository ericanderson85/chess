from abc import ABC, abstractmethod


class Piece(ABC):
    @abstractmethod
    def __init__(self, color, position):
        self.color = color
        self.position = position

    @abstractmethod
    def can_move(self, new_position):
        pass
