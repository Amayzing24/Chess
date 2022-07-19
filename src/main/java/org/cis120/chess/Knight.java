package org.cis120.chess;

import java.util.*;

public class Knight extends Piece {

    private final int[][] positionValue = new int[][]{
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}
    };

    private final int materialValue = 320;
    public Knight(Position pos, int side) {
        super(pos, side);
    }

    @Override
    public Map<Position, Move> getPossibleMoves(ChessBoard board) {
        Map<Position, Move> moves = new HashMap<>();
        Position pos = getPos();

        Set<Position> bSet = board.getBlockMaintainPositions(this);
        boolean blocker = bSet != null;
        Set<Position> cSet = board.getBlockCheckPositions();
        boolean check = board.sideInCheck(getSide());

        int[][] changes = new int[][] { { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }, { 2, 1 },
            { -2, 1 }, { 2, -1 }, { -2, -1 } };

        for (int[] c : changes) {
            Position newPos = new Position(pos.getRow() + c[0], pos.getCol() + c[1]);
            if (!ChessBoard.outOfBounds(newPos)) {
                Piece p = board.getPiece(newPos);
                if (p == null) {
                    moves.put(newPos, new Move(this, newPos, board));
                } else if (p != null && p.getSide() != getSide()) {
                    moves.put(newPos, new Move(this, newPos, board, p));
                }
            }
        }

        Iterator<Position> iter = moves.keySet().iterator();
        while (iter.hasNext()) {
            Position p = iter.next();
            if (check && !cSet.contains(p)) {
                iter.remove();
            } else if (blocker && !bSet.contains(p)) {
                iter.remove();
            }
        }

        return moves;
    }

    @Override
    public Set<Position> getSquaresThreatened(ChessBoard board) {
        Set<Position> moves = new HashSet<>();
        Position pos = getPos();

        int[][] changes = new int[][] { { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }, { 2, 1 },
            { -2, 1 }, { 2, -1 }, { -2, -1 } };

        for (int[] c : changes) {
            Position newPos = new Position(pos.getRow() + c[0], pos.getCol() + c[1]);
            if (!ChessBoard.outOfBounds(newPos)) {
                moves.add(newPos);
            }
        }

        return moves;
    }

    @Override
    public List<Integer> checkStatus(ChessBoard board) {
        List<Integer> status = new LinkedList<>();
        Position kingPos = board.getKingPos(getSide() * -1);
        Map<Position, Move> moves = getPossibleMoves(board);
        if (moves.containsKey(kingPos)) {
            status.add(1);
        } else {
            status.add(0);
        }
        return status;
    }

    @Override
    public int getPieceID() {
        return 4;
    }

    @Override
    public String toString() {
        return getColor() + " " + "knight";
    }

    @Override
    public double getPoints() {
        return materialValue * getSide() + positionValue[getPos().getRow()][getPos().getCol()];
    }
}