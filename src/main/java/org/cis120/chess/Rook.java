package org.cis120.chess;

import java.util.*;

public class Rook extends Piece {

    private final int[][] positionValue = new int[][]{
            {0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10, 10, 10, 10, 10,  5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {0,  0,  0,  5,  5,  0,  0,  0}
    };

    private final int materialValue = 500;
    public Rook(Position pos, int side) {
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

        // Vertical moves
        for (int i = -1; i <= 1; i++) {
            if (i != 0) {
                for (int j = 1; j <= 7; j++) {
                    Position newPos = new Position(pos.getRow() + j * i, pos.getCol());
                    if (!ChessBoard.outOfBounds(newPos)) {
                        Piece p = board.getPiece(newPos);
                        if (p == null) {
                            moves.put(newPos, new Move(this, newPos, board));
                        } else if (p != null) {
                            if (p.getSide() != getSide()) {
                                moves.put(newPos, new Move(this, newPos, board, p));
                            }
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        // Horizontal moves
        for (int i = -1; i <= 1; i++) {
            if (i != 0) {
                for (int j = 1; j <= 7; j++) {
                    Position newPos = new Position(pos.getRow(), pos.getCol() + j * i);
                    if (!ChessBoard.outOfBounds(newPos)) {
                        Piece p = board.getPiece(newPos);
                        if (p == null) {
                            moves.put(newPos, new Move(this, newPos, board));
                        } else if (p != null) {
                            if (p.getSide() != getSide()) {
                                moves.put(newPos, new Move(this, newPos, board, p));
                            }
                            break;
                        }
                    } else {
                        break;
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

        // Vertical moves
        for (int i = -1; i <= 1; i++) {
            if (i != 0) {
                for (int j = 1; j <= 7; j++) {
                    Position newPos = new Position(pos.getRow() + j * i, pos.getCol());
                    if (!ChessBoard.outOfBounds(newPos)) {
                        Piece p = board.getPiece(newPos);
                        if (p == null) {
                            moves.add(newPos);
                        } else if (p != null) {
                            moves.add(newPos);
                            if (p.getPieceID() != 0) {
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        // Horizontal moves
        for (int i = -1; i <= 1; i++) {
            if (i != 0) {
                for (int j = 1; j <= 7; j++) {
                    Position newPos = new Position(pos.getRow(), pos.getCol() + j * i);
                    if (!ChessBoard.outOfBounds(newPos)) {
                        Piece p = board.getPiece(newPos);
                        if (p == null) {
                            moves.add(newPos);
                        } else if (p != null) {
                            moves.add(newPos);
                            if (p.getPieceID() != 0) {
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        return moves;
    }

    @Override
    public List<Integer> checkStatus(ChessBoard board) {
        List<Integer> status = new LinkedList<>();
        List<Integer> blockPositions = new LinkedList<>();
        blockPositions.add(getPos().getRow());
        blockPositions.add(getPos().getCol());
        List<Piece> blockers = new LinkedList<Piece>();

        Position pos = getPos();
        Position kingPos = board.getKingPos(getSide() * -1);
        int rDiff = kingPos.getRow() - pos.getRow();
        int cDiff = kingPos.getCol() - pos.getCol();
        if (rDiff * cDiff == 0) {
            boolean sameColorBlock = false;
            int dir = 1;
            boolean vert = true;
            if (rDiff == 0) {
                vert = false;
            }
            if (cDiff < 0 || rDiff < 0) {
                dir = -1;
            }
            for (int i = 1; i <= 7; i++) {
                Position newPos;
                if (vert) {
                    newPos = new Position(pos.getRow() + i * dir, pos.getCol());
                } else {
                    newPos = new Position(pos.getRow(), pos.getCol() + i * dir);
                }
                if (!ChessBoard.outOfBounds(newPos)) {
                    blockPositions.add(newPos.getRow());
                    blockPositions.add(newPos.getCol());
                    Piece p = board.getPiece(newPos);
                    if (p != null) {
                        if (p.getPieceID() == 0 && p.getSide() != getSide()) {
                            break;
                        } else {
                            if (p.getSide() == getSide()) {
                                sameColorBlock = true;
                            }
                            blockers.add(p);
                        }
                    }
                } else {
                    break;
                }
            }
            if (blockers.isEmpty() && !sameColorBlock) {
                status.add(1);
                status.addAll(blockPositions);
            } else if (blockers.size() == 1 && !sameColorBlock) {
                status.add(2);
                status.add(blockers.get(0).getPos().getRow());
                status.add(blockers.get(0).getPos().getCol());
                status.addAll(blockPositions);
            } else {
                status.add(0);
            }
        } else {
            status.add(0);
        }
        return status;
    }

    @Override
    public int getPieceID() {
        return 2;
    }

    @Override
    public String toString() {
        return getColor() + " " + "rook";
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
