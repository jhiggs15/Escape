package escape.exception;

import escape.board.EscapeCoordinate;

public class MoveTooFar extends EscapeException
{
    public MoveTooFar(EscapeCoordinate from, EscapeCoordinate to) {
        super("The move from coordinate " + from.toString() + " to" + to.toString() +
                "is too far for the piece at this position to handle.");
    }
}
