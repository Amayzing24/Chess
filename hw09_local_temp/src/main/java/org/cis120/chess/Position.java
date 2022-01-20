package org.cis120.chess;

/**
 * The position class is used to represent position (row, column) on the board
 */
public class Position {
    private int row;
    private int col;
    public final static String[] CHESS_LETTERS = new String[] { "A", "B", "C", "D", "E", "F", "G",
        "H" };

    public Position(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Position otherPos = (Position) other;
        return row == otherPos.getRow() && col == otherPos.getCol();
    }

    /**
     * Since the chess game only involves row and columns between 0-7, this
     * hash code suffices.
     * @return
     */
    @Override
    public int hashCode() {
        return row * 8 + col;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    /**
     * Gets the Position in terms of chess coordinate
     * @return the chess coordinate of the Position
     */
    public String toChessString() {
        return CHESS_LETTERS[col] + (8 - row);
    }
}
