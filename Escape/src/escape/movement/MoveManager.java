package escape.movement;

import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.exception.*;
import escape.movement.pathfinding.PathFinding;
import escape.movement.pathfinding.PathFindingFactory;
import escape.piece.EscapeGamePiece;
import escape.required.Coordinate;
import escape.required.EscapePiece;
import escape.required.LocationType;
import escape.required.Player;

import java.util.List;

public class MoveManager
{

    private NeighborFinder neighborFinder;
    private BoundsChecker boundsChecker;
    private Board board;
    private PathFinding pathFinding;
    private PathFindingFactory pathFindingFactory;


    public MoveManager(Coordinate.CoordinateType coordinateType, Board board, int xMax, int yMax)
    {
        this.boundsChecker = new BoundsChecker(xMax, yMax);
        this.board = board;
        this.neighborFinder = new NeighborFinder(coordinateType, board, boundsChecker);
        this.pathFindingFactory = new PathFindingFactory(neighborFinder);
    }

    public List<EscapeCoordinate> findPath(EscapeCoordinate from, EscapeCoordinate to, EscapeGamePiece piece)
    {
        return pathFinding.findPath(from, to, piece);
    }

    public boolean canMove(Player player, EscapeCoordinate from, EscapeCoordinate to)
    {
        // ensure from and to are differnt

        if(from.equals(to)) throw new PieceHasNotMoved(from);

        // ensure source or destination is in of bounds
        if(!isInBounds(from)) throw new OutOfBoundsException(from);
        if(!isInBounds(to)) throw new OutOfBoundsException(to);

        // ensure a piece exists at the given space
        EscapeGamePiece piece = board.getPieceAt(from);
        if(piece == null) throw new SpaceMissingPiece(player, from);

        // ensure the piece that exists at the given space is owned by the player
        if(!piece.getPlayer().equals(player)) throw new SpaceMissingPiece(player, from);

        // ensure the piece that exists at the given space can travel far enough to reach the destination
        int value = piece.getMovementValue();
        EscapePiece.MovementPattern movementPattern = piece.getMovementPattern();
        neighborFinder.changeMovementPattern(movementPattern);
        pathFinding = pathFindingFactory.makePathFinder(movementPattern);
        if( (piece.hasAttribute(EscapePiece.PieceAttributeID.UNBLOCK) || piece.hasAttribute(EscapePiece.PieceAttributeID.FLY)  )
                && board.getLocationType(to).equals(LocationType.BLOCK))
            throw new NoPathExists(from, to, true);

        if(neighborFinder.minimumDistance(from, to) > value) throw new MoveTooFar(from, to);

        return true;
    }

    private boolean isInBounds(EscapeCoordinate coordinate)
    {
        return boundsChecker.isInBounds(coordinate);
    }

}
