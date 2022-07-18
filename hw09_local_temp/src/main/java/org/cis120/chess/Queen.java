package org.cis120.chess;

import java.util.*;

public class Queen extends Piece {

    private final int[][] positionValue = new int[][]{
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}
    };

    private final int materialValue = 900;
    public Queen(Position pos, int side) {
        super(pos, side);
    }

    @Override
    public Map<Position, Move> getPossibleMoves(ChessBoard board) {
        Map<Position, Move> moves = new HashMap<>();

        Bishop dummyBishop = new Bishop(getPos(), getSide());
        Rook dummyRook = new Rook(getPos(), getSide());
        moves.putAll(dummyBishop.getPossibleMoves(board));
        moves.putAll(dummyRook.getPossibleMoves(board));
        for (Position p : moves.keySet()) {
            moves.get(p).changePiece(this);
        }

        return moves;
    }

    @Override
    public Set<Position> getSquaresThreatened(ChessBoard board) {
        Set<Position> moves = new HashSet<>();

        Bishop dummyBishop = new Bishop(getPos(), getSide());
        Rook dummyRook = new Rook(getPos(), getSide());
        moves.addAll(dummyBishop.getSquaresThreatened(board));
        moves.addAll(dummyRook.getSquaresThreatened(board));

        return moves;
    }

    @Override
    public List<Integer> checkStatus(ChessBoard board) {
        List<Integer> status = new LinkedList<>();

        Bishop dummyBishop = new Bishop(getPos(), getSide());
        Rook dummyRook = new Rook(getPos(), getSide());
        List<Integer> statusBishop = dummyBishop.checkStatus(board);
        List<Integer> statusRook = dummyRook.checkStatus(board);
        if (statusBishop.get(0) == 1) {
            status = statusBishop;
        } else if (statusRook.get(0) == 1) {
            status = statusRook;
        } else if (statusBishop.get(0) == 2) {
            status = statusBishop;
        } else if (statusBishop.get(0) == 2) {
            status = statusRook;
        } else {
            status.add(0);
        }

        return status;
    }

    @Override
    public int getPieceID() {
        return 5;
    }

    @Override
    public String toString() {
        return getColor() + " " + "queen";
    }

    @Override
    public double getPoints() {
        if (getSide() == 1) {
            return materialValue * getSide() + positionValue[getPos().getRow()][getPos().getCol()];
        } else {
            return materialValue * getSide() + positionValue[7 - getPos().getRow()][getPos().getCol()];
        }
    }
}