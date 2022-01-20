package org.cis120.chess;

import java.util.*;

public class King extends Piece {
    private boolean inCheck;

    public King(Position pos, int side) {
        super(pos, side);
        inCheck = false;
    }

    @Override
    public Map<Position, Move> getPossibleMoves(ChessBoard board) {
        Map<Position, Move> moves = new HashMap<>();
        Position pos = getPos();
        Set<Position> invalidMoves = board.getAllSquaresThreatened(getSide() * -1);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    Position newPos = new Position(pos.getRow() + i, pos.getCol() + j);
                    if (!ChessBoard.outOfBounds(newPos) && !invalidMoves.contains(newPos)) {
                        Piece p = board.getPiece(newPos);
                        if (p == null) {
                            moves.put(newPos, new Move(this, newPos, board));
                        } else if (p != null && p.getSide() != getSide()) {
                            moves.put(newPos, new Move(this, newPos, board, p));
                        }
                    }
                }
            }
        }

        // Check for castling right side
        Piece rook = board.getPiece(new Position(getPos().getRow(), 7));
        if (rook != null && !board.sideInCheck(getSide()) && !hasMoved() && !rook.hasMoved()) {
            boolean castle = true;
            for (int i = 1; i <= 2; i++) {
                Position newPos = new Position(getPos().getRow(), getPos().getCol() + i);
                Piece p = board.getPiece(newPos);
                if (p != null || invalidMoves.contains(newPos)) {
                    castle = false;
                }
            }
            if (castle) {
                Position newPos = new Position(getPos().getRow(), 6);
                if (!invalidMoves.contains(newPos)) {
                    Move m = new Move(this, newPos, board);
                    m.makeCastle();
                    moves.put(newPos, m);
                }
            }
        }

        // Check for castling left side
        rook = board.getPiece(new Position(getPos().getRow(), 0));
        if (rook != null && !board.sideInCheck(getSide()) && !hasMoved() && !rook.hasMoved()) {
            boolean castle = true;
            for (int i = 1; i <= 3; i++) {
                Position newPos = new Position(getPos().getRow(), getPos().getCol() - i);
                Piece p = board.getPiece(newPos);
                if (p != null || invalidMoves.contains(newPos)) {
                    castle = false;
                }
            }
            if (castle) {
                Position newPos = new Position(getPos().getRow(), 2);
                if (!invalidMoves.contains(newPos)) {
                    Move m = new Move(this, newPos, board);
                    m.makeCastle();
                    moves.put(newPos, m);
                }
            }
        }

        return moves;
    }

    @Override
    public Set<Position> getSquaresThreatened(ChessBoard board) {
        Set<Position> moves = new HashSet<>();
        Position pos = getPos();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    Position newPos = new Position(pos.getRow() + i, pos.getCol() + j);
                    if (!ChessBoard.outOfBounds(newPos)) {
                        moves.add(newPos);
                    }
                }
            }
        }

        return moves;
    }

    @Override
    public List<Integer> checkStatus(ChessBoard board) {
        List<Integer> status = new LinkedList<>();
        status.add(0);
        return status;
    }

    @Override
    public int getPieceID() {
        return 0;
    }

    @Override
    public String toString() {
        return getColor() + " " + "king";
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public void putInCheck() {
        inCheck = true;
    }

    public void notInCheck() {
        inCheck = false;
    }
}
