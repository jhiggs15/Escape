package escape.movement;

import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.piece.EscapeGamePiece;
import escape.required.Coordinate;
import escape.required.EscapePiece;

import java.util.List;

public class MoveManager
{

    private NeighborFinder neighborFinder;
    private BoundsChecker boundsChecker;
    private Board board;


    public MoveManager(Coordinate.CoordinateType coordinateType, Board board, int xMax, int yMax)
    {
        this.boundsChecker = new BoundsChecker(xMax, yMax);
        this.board = board;
        this.neighborFinder = new NeighborFinder(coordinateType, board, boundsChecker);
    }

    public boolean canMove(EscapeCoordinate from, EscapeCoordinate to)
    {
        EscapeGamePiece piece = board.getPieceAt(from);
        int value = piece.getValue();
        EscapePiece.MovementPattern movementPattern = piece.getMovementPattern();

        neighborFinder.changeMovementPattern(movementPattern);
        return neighborFinder.minimumDistance(from, to) <= value;
    }


    @Override
    public boolean equals(Object otherMoveManager) {
        if(otherMoveManager.getClass() != this.getClass()) return false;
        MoveManager castMoveManger = (MoveManager) otherMoveManager;
        // TODO : add check here
        return true;
    }
}
