package org.cis120.chess;

import java.util.*;

public class Bishop extends Piece {
    public Bishop(Position pos, int side) {
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

        // Diagonal directions of the bishop
        int[][] dirs = new int[][] { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

        for (int[] dir : dirs) {
            int rDir = dir[0];
            int cDir = dir[1];
            for (int i = 1; i <= 7; i++) {
                Position newPos = new Position(pos.getRow() + i * rDir, pos.getCol() + i * cDir);
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

        // Diagonal directions of the bishop
        int[][] dirs = new int[][] { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

        for (int[] dir : dirs) {
            int rDir = dir[0];
            int cDir = dir[1];
            for (int i = 1; i <= 7; i++) {
                Position newPos = new Position(pos.getRow() + i * rDir, pos.getCol() + i * cDir);
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
        // Checking to see if opposing king is in some diagonal path of bishop
        if (Math.abs(rDiff) == Math.abs(cDiff)) {
            boolean sameColorBlock = false;
            int rDir;
            int cDir;
            if (rDiff > 0) {
                rDir = 1;
            } else {
                rDir = -1;
            }
            if (cDiff > 0) {
                cDir = 1;
            } else {
                cDir = -1;
            }
            for (int i = 1; i <= 7; i++) {
                Position newPos = new Position(pos.getRow() + i * rDir, pos.getCol() + i * cDir);
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
        return 3;
    }

    @Override
    public String toString() {
        return getColor() + " " + "bishop";
    }
}