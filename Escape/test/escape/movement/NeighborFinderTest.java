package escape.movement;

import escape.EscapeGameBuilder;
import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.required.EscapePiece;
import escape.required.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized;
import static junit.framework.TestCase.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class NeighborFinderTest
{

    public NeighborFinder initializeNeighborFinder(String filename, EscapePiece.MovementPattern movementPattern) throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder(filename);
        Board board = new Board(egb.getGameInitializer());

        return new NeighborFinder(movementPattern, board);
    }

    // ------ get Neighbors

    boolean genericNeighborCheck(String filename, EscapeCoordinate coordinate, EscapePiece.MovementPattern movementPattern, List<EscapeCoordinate> neighbors) throws Exception {
        NeighborFinder neighborFinder = initializeNeighborFinder(filename, movementPattern);
        List<EscapeCoordinate> foundNeighbors = neighborFinder.getNeighbors(coordinate);
        boolean isCorrect = true;
        for(EscapeCoordinate neighbor : neighbors)
        {
            isCorrect = foundNeighbors.contains(neighbor) && isCorrect;
            if(!isCorrect)
                break;
        }
        for(EscapeCoordinate neighbor : foundNeighbors)
        {
            isCorrect = neighbors.contains(neighbor) && isCorrect;
            if(!isCorrect)
                break;
        }
        return isCorrect && neighbors.size() == foundNeighbors.size();
    }

    @ParameterizedTest
    @MethodSource("OMNIProvider")
    void OMNICheck(String name, String filename, EscapeCoordinate coordinate, EscapePiece.MovementPattern movementPattern, List<EscapeCoordinate> neighbors) throws Exception {
        assertTrue(genericNeighborCheck(filename, coordinate, movementPattern, neighbors));

    }


    static Stream<Arguments> OMNIProvider()
    {
        return (
                Stream.of(
                        Arguments.arguments("OMNI 0,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                EscapePiece.MovementPattern.OMNI,
                                        Arrays.asList(
                                                new EscapeCoordinate(1, 0),
                                                new EscapeCoordinate(-1, 0),
                                                new EscapeCoordinate(0, 1),
                                                new EscapeCoordinate(0, -1),
                                                new EscapeCoordinate(1, 1),
                                                new EscapeCoordinate(-1, -1),
                                                new EscapeCoordinate(-1, 1),
                                                new EscapeCoordinate(1, -1))
                        ),
                        Arguments.arguments("OMNI 1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(1, 1),
                                EscapePiece.MovementPattern.OMNI,
                                Arrays.asList(
                                        new EscapeCoordinate(2, 1),
                                        new EscapeCoordinate(0, 1),
                                        new EscapeCoordinate(1, 2),
                                        new EscapeCoordinate(1, 0),
                                        new EscapeCoordinate(2, 2),
                                        new EscapeCoordinate(0, 0),
                                        new EscapeCoordinate(0, 2),
                                        new EscapeCoordinate(2, 0))
                        ),
                        Arguments.arguments("OMNI -4,3",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(-4, 3),
                                EscapePiece.MovementPattern.OMNI,
                                Arrays.asList(
                                        new EscapeCoordinate(-3, 3),
                                        new EscapeCoordinate(-5, 3),
                                        new EscapeCoordinate(-4, 4),
                                        new EscapeCoordinate(-4, 2),
                                        new EscapeCoordinate(-3, 4),
                                        new EscapeCoordinate(-5, 2),
                                        new EscapeCoordinate(-5, 4),
                                        new EscapeCoordinate(-3, 2))
                        ),
                        Arguments.arguments("OMNI -4, -7",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(-4, -7),
                                EscapePiece.MovementPattern.OMNI,
                                Arrays.asList(

                                        new EscapeCoordinate(-3, -7),
                                        new EscapeCoordinate(-5, -7),
                                        new EscapeCoordinate(-4, -6),
                                        new EscapeCoordinate(-4, -8),
                                        new EscapeCoordinate(-3, -6),
                                        new EscapeCoordinate(-5, -8),
                                        new EscapeCoordinate(-5, -6),
                                        new EscapeCoordinate(-3, -8))

                        ),
                        Arguments.arguments("OMNI -4, -7",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(9, -3),
                                EscapePiece.MovementPattern.OMNI,
                                Arrays.asList(
                                        new EscapeCoordinate(10, -3),
                                        new EscapeCoordinate(8, -3),
                                        new EscapeCoordinate(9, -2),
                                        new EscapeCoordinate(9, -4),
                                        new EscapeCoordinate(10, -2),
                                        new EscapeCoordinate(8, -4),
                                        new EscapeCoordinate(8, -2),
                                        new EscapeCoordinate(10, -4))
                        ),
                        Arguments.arguments("OMNI All Out of Bounds",
                                "Escape/config/egc/NoNeighbors.egc",
                                new EscapeCoordinate(1, 1),
                                EscapePiece.MovementPattern.OMNI,
                                new ArrayList<>()
                        ),
                        Arguments.arguments("OMNI Some are out of Bounds",
                                "Escape/config/egc/PlayersHaveSamePiece.egc",
                                new EscapeCoordinate(40, 30),
                                EscapePiece.MovementPattern.OMNI,
                                Arrays.asList(
                                        new EscapeCoordinate(39, 30),
                                        new EscapeCoordinate(40, 29),
                                        new EscapeCoordinate(39, 29))
                        ),
                        Arguments.arguments("OMNI At the boundary",
                                "Escape/config/egc/JustBigEnough.egc",
                                new EscapeCoordinate(2, 2),
                                EscapePiece.MovementPattern.OMNI,
                                Arrays.asList(
                                        new EscapeCoordinate(3, 2),
                                        new EscapeCoordinate(1, 2),
                                        new EscapeCoordinate(2, 3),
                                        new EscapeCoordinate(2, 1),
                                        new EscapeCoordinate(3, 3),
                                        new EscapeCoordinate(1, 1),
                                        new EscapeCoordinate(1, 3),
                                        new EscapeCoordinate(3, 1))
                        )

                )
        );

    }


    // ----- minimum Distance


    @ParameterizedTest
    @MethodSource("DistanceProvider")
    void DistanceCheck(String name, String filename, EscapePiece.MovementPattern movementPattern, EscapeCoordinate from, EscapeCoordinate to, int distance) throws Exception
    {
        NeighborFinder neighborFinder = initializeNeighborFinder(filename, movementPattern);
        assertEquals(neighborFinder.minimumDistance(from, to), distance );
    }


    static Stream<Arguments> DistanceProvider()
    {
        return (
                Stream.of(
                        // one away
                        Arguments.arguments("OMNI 0,0 -> 1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 1),
                                1),
                        Arguments.arguments("OMNI 0,0 -> -1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 1),
                                1),
                        Arguments.arguments("OMNI 0,0 -> 0,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 1),
                                1),
                        Arguments.arguments("OMNI 0,0 -> 1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 1),
                                1),
                        Arguments.arguments("OMNI 0,0 -> -1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 0),
                                1),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 0),
                                1),
                        Arguments.arguments("OMNI 0,0 -> -1,-1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, -1),
                                1),
                        Arguments.arguments("OMNI 0,0 -> 1,-1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, -1),
                                1),

                        // two away
                        Arguments.arguments("OMNI 0,0 -> -2, 2",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 2),
                                2),
                        Arguments.arguments("OMNI 0,0 -> -1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 2),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 0,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 2),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 2),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, 2),
                                2),
                        Arguments.arguments("OMNI 0,0 -> -1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 1),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, 1),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, 0),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 0),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, -1),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -1),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, -2),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, -2),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, -2),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, -2),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -2),
                                2),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -2),
                                2),

                        // three away
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-3, 3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, 3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(3, 3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-3, 2),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-3, 1),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-3, 0),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-3, -1),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-3, -2),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-3, -3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-3, -3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, -3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, -3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, -3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, -3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(3, -3),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(3, -2),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(3, -1),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(3, 0),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(3, 1),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(3, 2),
                                3),
                        Arguments.arguments("OMNI 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                EscapePiece.MovementPattern.OMNI,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(3, 3),
                                3)
                        )
        );

    }


}
