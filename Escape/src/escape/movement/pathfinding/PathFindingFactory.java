package escape.movement.pathfinding;

import escape.movement.NeighborFinder;
import escape.required.EscapePiece;

public class PathFindingFactory
{
    NeighborFinder neighborFinder;

    public PathFindingFactory(NeighborFinder neighborFinder)
    {
        this.neighborFinder = neighborFinder;
    }

    public PathFinding makePathFinder(EscapePiece.MovementPattern movementPattern)
    {
        return makeOMNIPathFinding();

//        switch (movementPattern)
//        {
//            case OMNI:
//                return makeOMNIPathFinding();
//            case SKEW:
//                return null;
//            case LINEAR:
//                return null;
//            case DIAGONAL:
//                return null;
//            case ORTHOGONAL:
//                return null;
//            default:
//                return null;
//        }
    }

    private PathFinding makeOMNIPathFinding()
    {
        return new AStar(neighborFinder);
    }
}
