package escape.exception;

import escape.board.EscapeCoordinate;

public class OutOfBounds extends EscapeException
{
    public OutOfBounds(EscapeCoordinate coordinate) {
        super(createString(coordinate));
    }

    public static String createString(EscapeCoordinate coordinate)
    {
        return "The coordinate " + coordinate.toString() + " is out of bounds please select a coordinate in bounds.";
    }
}
