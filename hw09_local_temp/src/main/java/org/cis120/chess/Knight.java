package org.cis120.chess;

import java.util.*;

public class Knight extends Piece {
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
            }
            if (blocker && !bSet.contains(p)) {
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
}