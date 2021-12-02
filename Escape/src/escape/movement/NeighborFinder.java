package escape.movement;

import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.required.EscapePiece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NeighborFinder
{
    enum Neighbor {TOP_LEFT, TOP, TOP_RIGHT, LEFT, RIGHT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT}
//    ArrayList<Neighbor> allDirections = Arrays.asList(Neighbor.TOP_LEFT);

    private NeighborSearch neighborSearch;
    private Board board;

    public NeighborFinder(EscapePiece.MovementPattern movementPattern, Board board)
    {
        this.board = board;
        switch (movementPattern){
            case OMNI:
                neighborSearch = OMNINeighborfinder;
            case ORTHOGONAL:
            case DIAGONAL:
            case LINEAR:
            case SKEW:
        }
    }

    public int minimumDistance(EscapeCoordinate from, EscapeCoordinate to)
    {
        if(getNeighbors(from).contains(to)) return 1;
        else
        {
            int differnceOfX = Integer.compare(to.getX(), from.getX());
            int differnceOfY = Integer.compare(to.getY(), from.getY());
            return minimumDistance(new EscapeCoordinate(from.getX() + differnceOfX, from.getY() + differnceOfY), to) + 1;
        }
    }

    public List<EscapeCoordinate> getNeighbors(EscapeCoordinate coordinate)
    {
        return neighborSearch.findNeighbors(coordinate)
                .stream().filter(neighbor -> board.isInBounds(neighbor) && board.isAccessible(neighbor))
                .collect(Collectors.toList());
    }

    public static NeighborSearch OMNINeighborfinder = (EscapeCoordinate coordinate)
            -> allNeighbors(coordinate);


//     will be used for other patterns

//    private static EscapeCoordinate getNeighbor(EscapeCoordinate coordinate, Neighbor neighborType){
//        List<EscapeCoordinate> neighbors = allNeighbors(coordinate);
//        return neighbors.get(neighborType.ordinal());
//    }

    private static List<EscapeCoordinate> selectNeighbors(EscapeCoordinate coordinate, Neighbor... neighborTypes){
        List<EscapeCoordinate> neighbors = allNeighbors(coordinate);
        return Arrays.stream(neighborTypes).map(neighborType -> neighbors.get(neighborType.ordinal())).collect(Collectors.toList());
    }

    private static List<EscapeCoordinate> allNeighbors(EscapeCoordinate coordinate)
    {
        ArrayList<EscapeCoordinate> allNeighbors = new ArrayList<>();
        allNeighbors.add(new EscapeCoordinate(coordinate.getX() - 1, coordinate.getY() + 1));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX(), coordinate.getY() + 1));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX() + 1, coordinate.getY() + 1));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX() - 1, coordinate.getY()));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX() + 1, coordinate.getY()));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX() - 1, coordinate.getY() - 1));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX(), coordinate.getY() - 1));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX() + 1, coordinate.getY() - 1));
        return allNeighbors;
    }


}
