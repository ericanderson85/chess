package util;

public record Position(int row, int col) {
    @Override
    public String toString() {
        char colLetter = (char) ('a' + col); // Convert column to a-h
        return String.valueOf(colLetter) + (row + 1); // Convert row to 1 indexed
    }
}
