package escape.movement.pathfinding;

import escape.movement.NeighborFinder;
import escape.required.EscapePiece;

public class PathFindingFactory
{
    private NeighborFinder neighborFinder;

    public PathFindingFactory(NeighborFinder neighborFinder)
    {
        this.neighborFinder = neighborFinder;
    }

    public PathFinding makePathFinder(EscapePiece.MovementPattern movementPattern)
    {

        switch (movementPattern)
        {
            case OMNI:
                return makeOMNIPathFinding();
//            case SKEW:
//                return null;
            case LINEAR:
            case DIAGONAL:
            case ORTHOGONAL:
                return makeLINEPathFinding(movementPattern);
            default:
                return null;
        }

    }

    private PathFinding makeOMNIPathFinding()
    {
        return new AStar(neighborFinder);
    }

    private PathFinding makeLINEPathFinding(EscapePiece.MovementPattern movementPattern)
    {
        Line line = new Line(neighborFinder);
        line.changeType(movementPattern);
        return line;
    }
}
