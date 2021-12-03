package escape.movement;

import escape.board.EscapeCoordinate;
import escape.required.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class AllNeighborFinder
{
    private AllNeighborSearch allNeighborSearch;

    public AllNeighborFinder(Coordinate.CoordinateType coordinateType)
    {
        switch(coordinateType)
        {
            case SQUARE:
                allNeighborSearch = SQUAREAllNeighbors;
                break;
            case HEX:
                allNeighborSearch = HEXAllNeighbors;
                break;
            case TRIANGLE:
        }
    }

    public List<EscapeCoordinate> findAllNeighbors(EscapeCoordinate coordinate)
    {
        return allNeighborSearch.findAllNeighbors(coordinate);
    }

    public static AllNeighborSearch SQUAREAllNeighbors = (EscapeCoordinate coordinate)
            -> eightNeighbors(coordinate);
    public static AllNeighborSearch HEXAllNeighbors = (EscapeCoordinate coordinate)
            -> sixNeighbors(coordinate);


    private static List<EscapeCoordinate> eightNeighbors(EscapeCoordinate coordinate)
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

    private static List<EscapeCoordinate> sixNeighbors(EscapeCoordinate coordinate)
    {
        ArrayList<EscapeCoordinate> allNeighbors = new ArrayList<>();
        allNeighbors.add(new EscapeCoordinate(coordinate.getX(), coordinate.getY() + 1));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX() - 1, coordinate.getY() + 1));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX() + 1, coordinate.getY()));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX() - 1, coordinate.getY()));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX() + 1, coordinate.getY() - 1));
        allNeighbors.add(new EscapeCoordinate(coordinate.getX(), coordinate.getY() - 1));
        return allNeighbors;
    }
}
