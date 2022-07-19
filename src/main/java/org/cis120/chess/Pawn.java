package org.cis120.chess;

import java.util.*;

public class Pawn extends Piece {
    private boolean hasMovedTwo;

    private final int[][] positionValue = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {5, 10, 10,-20,-20, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0}
    };

    private final int materialValue = 100;

    public Pawn(Position pos, int side) {
        super(pos, side);
        hasMovedTwo = false;
    }

    @Override
    public Map<Position, Move> getPossibleMoves(ChessBoard board) {
        Map<Position, Move> moves = new HashMap<>();
        Position pos = getPos();

        Set<Position> bSet = board.getBlockMaintainPositions(this);
        boolean blocker = bSet != null;
        Set<Position> cSet = board.getBlockCheckPositions();
        boolean check = board.sideInCheck(getSide());

        int vertDir = getSide() * -1;
        int vertScale;
        if (hasMoved()) {
            vertScale = 1;
        } else {
            vertScale = 2;
        }

        for (int i = -1; i < 2; i++) {
            for (int j = 1; j <= vertScale; j++) {
                if (!(j == 2 && i != 0)) {
                    Position newPos = new Position(pos.getRow() + j * vertDir, pos.getCol() + i);
                    if (!ChessBoard.outOfBounds(newPos)) {
                        Piece p = board.getPiece(newPos);
                        if (j == 2 && board.getPiece(
                                new Position(pos.getRow() + vertDir, pos.getCol())
                        ) != null) {
                            continue;
                        } else if (i == 0 && p == null) {
                            moves.put(newPos, new Move(this, newPos, board));
                        } else if (i != 0) {
                            // En passant piece check
                            Piece p2 = board.getPiece(new Position(pos.getRow(), pos.getCol() + i));

                            if (p != null && getSide() != p.getSide()) {
                                moves.put(newPos, new Move(this, newPos, board, p));
                            } else if (p2 != null && getSide() != p2.getSide()
                                    && p2.getPieceID() == 1
                                    && ((Pawn) p2).hasMovedTwo()) {
                                moves.put(newPos, new Move(this, newPos, board, p2));
                            }
                        }
                    }
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
        int vertDir = getSide() * -1;

        for (int i = -1; i < 2; i++) {
            if (i != 0) {
                Position newPos = new Position(pos.getRow() + vertDir, pos.getCol() + i);
                if (!ChessBoard.outOfBounds(newPos)) {
                    moves.add(newPos);
                }
            }
        }

        return moves;
    }

    @Override
    public List<Integer> checkStatus(ChessBoard board) {
        List<Integer> status = new LinkedList<>();
        Position kingPos = board.getKingPos(getSide() * -1);
        int rDiff = kingPos.getRow() - getPos().getRow();
        int cDiff = kingPos.getCol() - getPos().getCol();
        if (rDiff == getSide() * -1 && Math.abs(cDiff) == 1) {
            status.add(1);
        } else {
            status.add(0);
        }

        return status;
    }

    @Override
    public int getPieceID() {
        return 1;
    }

    @Override
    public void move(Position newPos, int turn) {
        if (Math.abs(getPos().getRow() - newPos.getRow()) == 2) {
            hasMovedTwo = true;
        }
        super.move(newPos, turn);
    }

    @Override
    public void undoMove(Position oldPosition, int turn) {
        if (getSide() == 1 && oldPosition.getRow() == 6) {
            hasMovedTwo = false;
        } else if (getSide() == -1 && oldPosition.getRow() == 1) {
            hasMovedTwo = false;
        }
        super.undoMove(oldPosition, turn);
    }

    @Override
    public String toString() {
        return getColor() + " " + "pawn";
    }

    public void deactivateMovedTwo() {
        hasMovedTwo = false;
    }

    public boolean hasMovedTwo() {
        return hasMovedTwo;
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
