package escape.movement;

import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.piece.EscapeGamePiece;
import escape.required.Coordinate;
import escape.required.EscapePiece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NeighborFinder
{
//    enum Neighbor {TOP_LEFT, TOP, TOP_RIGHT, LEFT, RIGHT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT}

    private NeighborSearch neighborSearch;
    private static AllNeighborFinder allNeighborFinder;
    private Board board;
    private BoundsChecker boundsChecker;


    public NeighborFinder(Coordinate.CoordinateType type, Board board, BoundsChecker boundsChecker)
    {
        this.board = board;
        this.boundsChecker = boundsChecker;
        allNeighborFinder = new AllNeighborFinder(type);
    }

    public void changeMovementPattern(EscapePiece.MovementPattern movementPattern)
    {
        switch (movementPattern){
            case OMNI:
                neighborSearch = OMNINeighborfinder;
            case ORTHOGONAL:
            case DIAGONAL:
            case LINEAR:
                neighborSearch = LINEARNeighborfinder;
            case SKEW:
        }
    }

    public int minimumDistance(EscapeCoordinate from, EscapeCoordinate to)
    {
        if(from.equals(to)) return 0;
        else
        {
            int differnceOfX = Integer.compare(to.getX(), from.getX());
            int differnceOfY = Integer.compare(to.getY(), from.getY());
            return minimumDistance(new EscapeCoordinate(from.getX() + differnceOfX, from.getY() + differnceOfY), to) + 1;
        }
    }

    public List<EscapeCoordinate> getNeighbors(EscapeCoordinate coordinate, EscapeGamePiece piece)
    {
        return neighborSearch.findNeighbors(coordinate)
                .stream().filter(neighbor -> boundsChecker.isInBounds(neighbor) && board.isAccessible(neighbor, piece))
                .collect(Collectors.toList());
    }

    public static NeighborSearch OMNINeighborfinder = (EscapeCoordinate coordinate)
            -> allNeighborFinder.findAllNeighbors(coordinate);

    public static NeighborSearch LINEARNeighborfinder = (EscapeCoordinate coordinate)
            -> allNeighborFinder.findAllNeighbors(coordinate);


//     will be used for other patterns

//    private static EscapeCoordinate getNeighbor(EscapeCoordinate coordinate, Neighbor neighborType){
//        List<EscapeCoordinate> neighbors = allNeighbors(coordinate);
//        return neighbors.get(neighborType.ordinal());
//    }

//    private static List<EscapeCoordinate> selectNeighbors(EscapeCoordinate coordinate, Neighbor... neighborTypes){
//        List<EscapeCoordinate> neighbors = allNeighborFinder.findAllNeighbors(coordinate);
//        return Arrays.stream(neighborTypes).map(neighborType -> neighbors.get(neighborType.ordinal())).collect(Collectors.toList());
//    }




}
