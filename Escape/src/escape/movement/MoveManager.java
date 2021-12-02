package escape.movement;

import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.required.EscapePiece;

public class MoveManager
{
    private Board board;

    private NeighborFinder neighborFinder;


    public MoveManager(EscapePiece.MovementPattern movementPattern, Board board)
    {
        this.board = board;
        neighborFinder = new NeighborFinder(movementPattern, board);

    }

    public boolean canMove(EscapeCoordinate from, EscapeCoordinate to, int value)
    {
        return neighborFinder.minimumDistance(from, to) <= value;
    }


    @Override
    public boolean equals(Object otherMoveManager) {
        if(otherMoveManager.getClass() != this.getClass()) return false;
        MoveManager castMoveManger = (MoveManager) otherMoveManager;
        return this.board.getBoardType().equals(castMoveManger.board.getBoardType());
    }
}
