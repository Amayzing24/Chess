package org.cis120.chess;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Superclass for Pieces
 * Pawn, Bishop, Rook, Knight, King, Queen extend this abstract class
 * Subtypes must define getPossibleMoves, getSquaresThreatened, checkStatus
 */
public abstract class Piece {
    private Position pos;
    private int side;
    private boolean hasMoved;

    public Piece(Position pos, int side) {
        if (ChessBoard.outOfBounds(pos)) {
            throw new IllegalArgumentException();
        } else if (side != 1 && side != -1) {
            throw new IllegalArgumentException();
        }
        this.pos = pos;
        this.side = side;
        hasMoved = false;
    }

    /**
     * Gets all possible movement options for a piece at its position on the board
     * 
     * @param board The ChessBoard object currently being used
     * @return A Map mapping possible movement positions to the Move object
     *         corresponding to that move
     */
    public abstract Map<Position, Move> getPossibleMoves(ChessBoard board);

    /**
     * Gets a set of all squares threatened by the piece. Threatening includes
     * pieces of the same color that this
     * piece can directly access.
     * 
     * @param board The ChessBoard object currently being used
     * @return A Set of Positions
     */
    public abstract Set<Position> getSquaresThreatened(ChessBoard board);

    /**
     * Returns the checking status of a piece
     * 
     * @param board the ChessBoard object currently being played on
     * @return an integer list. the first element is 0 if there isn't direct check
     *         and there isn't just one
     *         opposite colored piece blocking a potential check, 1 if there is a
     *         direct check, or 2 if there isn't
     *         a direct check and there is only one opposite colored piece blocking
     *         a potential check. If 1, then
     *         the subsequent pairs of integers in the list are the row and column
     *         respectively of positions
     *         to move to that will create a block of check. If 2, then the next two
     *         integers are the position
     *         of the blocker, and the subsequent pairs of integers are the
     *         positions of moves that will uphold
     *         a block of check.
     */
    public abstract List<Integer> checkStatus(ChessBoard board);

    public abstract int getPieceID();

    public Position getPos() {
        return pos;
    }

    public String getColor() {
        if (side == 1) {
            return "White";
        } else {
            return "Black";
        }
    }

    public int getSide() {
        return side;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * Changes the position of the Piece
     * @param newPos the position to move to
     */
    public void move(Position newPos) {
        if (ChessBoard.outOfBounds(newPos)) {
            throw new IllegalArgumentException();
        }
        hasMoved = true;
        pos = newPos;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Piece otherPiece = (Piece) other;
        return otherPiece.getPos().equals(getPos()) && otherPiece.getPieceID() == getPieceID()
                && otherPiece.getSide() == getSide();
    }
}
