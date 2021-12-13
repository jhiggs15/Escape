package escape.movement.pathfinding;

import escape.board.EscapeCoordinate;
import escape.movement.NeighborFinder;
import escape.piece.EscapeGamePiece;
import escape.required.EscapePiece;

import java.util.ArrayList;
import java.util.List;

// !! Adapted from https://duck.cs.wpi.edu/user/jhiggins/notebooks/cs4233-notebooks.git/Coding/SimpleExample/FirstExample-v3.ipynb
public class Line implements PathFinding
{
    LineChecker lineChecker;
    private NeighborFinder neighborFinder;

    public Line(NeighborFinder neighborFinder)
    {
        this.neighborFinder = neighborFinder;
    }

    public void changeType(EscapePiece.MovementPattern movementPattern)
    {
        switch (movementPattern)
        {
            case LINEAR:
                lineChecker = LINEAR;
                break;
            case ORTHOGONAL:
                lineChecker = ORTHOGONAL;
                break;
            case DIAGONAL:
                lineChecker = DIAGONAL;
                break;
        }
    }

    @Override
    public List<EscapeCoordinate> findPath(EscapeCoordinate from, EscapeCoordinate to, EscapeGamePiece piece)
    {
        if(!lineChecker.isSpecifiedLineType(from, to))
            return new ArrayList<>();

        int xDiff = Integer.compare(to.getX() - from.getX(), 0);
        int yDiff = Integer.compare(to.getY() - from.getY(), 0);

        return findPath(new ArrayList<>(), from, to, piece, xDiff, yDiff);
    }

    private List<EscapeCoordinate> findPath(List<EscapeCoordinate> path, EscapeCoordinate current, EscapeCoordinate destination, EscapeGamePiece piece, int xDiff, int yDiff)
    {
        if(current.equals(destination))
            return path;

        EscapeCoordinate nextCoordinate = new EscapeCoordinate(current.getX() + xDiff, current.getY() + yDiff);

        if(neighborFinder.getNeighbors(current, piece).contains(nextCoordinate))
        {
            path.add(nextCoordinate);
            return findPath(path, nextCoordinate, destination, piece, xDiff, yDiff);
        }
        else
            return new ArrayList<>();
    }

    // !! Adapted from https://duck.cs.wpi.edu/user/jhiggins/notebooks/cs4233-notebooks.git/Coding/SimpleExample/FirstExample-v3.ipynb
    public static LineChecker DIAGONAL = (EscapeCoordinate from, EscapeCoordinate to) ->
    {
        int rowDiffernce = Math.abs(from.getX() - to.getX());
        int columnDiffernce = Math.abs(from.getY() - to.getY());
        return rowDiffernce == columnDiffernce;
    };

    // !! Adapted from https://duck.cs.wpi.edu/user/jhiggins/notebooks/cs4233-notebooks.git/Coding/SimpleExample/FirstExample-v3.ipynb
    public static LineChecker ORTHOGONAL = (EscapeCoordinate from, EscapeCoordinate to) ->
            from.getX() == to.getX() || from.getY() == to.getY();


    // !! Adapted from https://duck.cs.wpi.edu/user/jhiggins/notebooks/cs4233-notebooks.git/Coding/SimpleExample/FirstExample-v3.ipynb
    public static LineChecker LINEAR = (EscapeCoordinate from, EscapeCoordinate to) ->
            ORTHOGONAL.isSpecifiedLineType(from, to) || DIAGONAL.isSpecifiedLineType(from, to);

}
