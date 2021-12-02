package escape.movement;

import escape.EscapeGameBuilder;
import escape.EscapeGameManager;
import escape.GameManager;
import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.piece.EscapeGamePiece;
import escape.required.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.stringtemplate.v4.misc.Coordinate;

import java.util.List;
import java.util.stream.Stream;

import static junit.framework.TestCase.*;

public class MoveCheckerTest
{


    @ParameterizedTest
    @MethodSource("movementProvider")
    void validateEndGame(EscapeCoordinate from, EscapeCoordinate to, Player player, boolean isMoveValid, String filename) throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder(filename);
        Board board = new Board(egb.getGameInitializer());
        EscapeGamePiece piece =  board.getPieceAt(player, from);
        assertEquals(isMoveValid, piece.canMove(from, to));
    }


    static Stream<Arguments> movementProvider()
    {
        return (
                Stream.of(
                        Arguments.arguments(
                                makeCoordinate(4, 4),
                                makeCoordinate(5, 5),
                                Player.PLAYER1,
                                true,
                                "Escape/config/egc/test1.egc"
                        )

                )
        );

    }

    static EscapeCoordinate makeCoordinate(int x, int y)
    {
        return new EscapeCoordinate(x, y);
    }




}
