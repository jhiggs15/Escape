package escape.exception;

import escape.board.EscapeCoordinate;

public class MoveTooFar extends EscapeException
{

    public MoveTooFar(EscapeCoordinate from, EscapeCoordinate to) {
        super(createString(from, to));
    }

    public static String createString(EscapeCoordinate from, EscapeCoordinate to)
    {
        return "The move from coordinate " + from.toString() + " to " + to.toString() +
                " is too far for the piece at this position to handle.";
    }
}
