package escape.movement.pathfinding;

import escape.EscapeGameBuilder;
import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.movement.BoundsChecker;
import escape.movement.NeighborFinder;
import escape.required.Coordinate;
import escape.required.EscapePiece;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class AStarTest
{
    public NeighborFinder initializeNeighborFinder(String filename, EscapePiece.MovementPattern movementPattern, Coordinate.CoordinateType coordinateType) throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder(filename);
        Board board = new Board(egb.getGameInitializer());
        int xMax = egb.getGameInitializer().getxMax();
        int yMax = egb.getGameInitializer().getyMax();
        NeighborFinder neighborFinder =new NeighborFinder(coordinateType, board, new BoundsChecker(xMax, yMax) );
        neighborFinder.changeMovementPattern(movementPattern);

        return neighborFinder;
    }

    @ParameterizedTest
    @MethodSource({"OMNIAStarProvider"})
    void validatePath(String name, String filename, EscapeCoordinate from, EscapeCoordinate to, EscapePiece.MovementPattern movementPattern, Coordinate.CoordinateType coordinateType, int lengthOfPath, List<EscapeCoordinate> path) throws Exception {
        NeighborFinder neighborFinder = initializeNeighborFinder(filename, movementPattern, coordinateType);
        AStar aStar = new AStar(neighborFinder);
        List<EscapeCoordinate> generatedPath = aStar.findPath(from, to);
        assertTrue(generatedPath.isEmpty() || generatedPath.get(generatedPath.size() - 1).equals(to));
        assertEquals(generatedPath.size(), lengthOfPath);
        if(path != null)
        {
            for(int i = 0; i < path.size(); i++)
            {
                assertEquals(path.get(i), generatedPath.get(i));
            }
        }
    }

    static Stream<Arguments> OMNIAStarProvider()
    {
        return (
                Stream.of(
                        Arguments.arguments("Path Length 0 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 0),
                                EscapePiece.MovementPattern.OMNI,
                                Coordinate.CoordinateType.SQUARE,
                                0, null),
                        Arguments.arguments("Path Length 0 HEX",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 0),
                                EscapePiece.MovementPattern.OMNI,
                                Coordinate.CoordinateType.HEX,
                                0, null),
                        Arguments.arguments("Path Length 1 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 1),
                                EscapePiece.MovementPattern.OMNI,
                                Coordinate.CoordinateType.SQUARE,
                                1, null),
                        Arguments.arguments("Path Length 1 HEX",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 0),
                                EscapePiece.MovementPattern.OMNI,
                                Coordinate.CoordinateType.HEX,
                                1, null),
                        Arguments.arguments("Path Length 2 HEX",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 1),
                                EscapePiece.MovementPattern.OMNI,
                                Coordinate.CoordinateType.HEX,
                                2, null),
                        Arguments.arguments("Path Length 2 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 2),
                                EscapePiece.MovementPattern.OMNI,
                                Coordinate.CoordinateType.SQUARE,
                                2, null),
                        Arguments.arguments("No Possible Path SQUARE",
                                "Escape/config/egc/TheBox.egc",
                                new EscapeCoordinate(2, 2),
                                new EscapeCoordinate(1, -1),
                                EscapePiece.MovementPattern.OMNI,
                                Coordinate.CoordinateType.SQUARE,
                                0,
                                null),
                        Arguments.arguments("No Possible Path SQUARE",
                                "Escape/config/egc/TheBox.egc",
                                new EscapeCoordinate(2, 2),
                                new EscapeCoordinate(1, -1),
                                EscapePiece.MovementPattern.OMNI,
                                Coordinate.CoordinateType.HEX,
                                0,
                                null),
                        Arguments.arguments("One Possible Path SQUARE",
                                "Escape/config/egc/ForcedPath.egc",
                                new EscapeCoordinate(-2, -1),
                                new EscapeCoordinate(1, -1),
                                EscapePiece.MovementPattern.OMNI,
                                Coordinate.CoordinateType.SQUARE,
                                4,
                                Arrays.asList(
                                        new EscapeCoordinate(-2, 0),
                                        new EscapeCoordinate(-1, 1),
                                        new EscapeCoordinate(0, 0),
                                        new EscapeCoordinate(1, -1)
                                )),
                        Arguments.arguments("One Possible Path HEX",
                                "Escape/config/egc/ForcedPath.egc",
                                new EscapeCoordinate(-2, -1),
                                new EscapeCoordinate(1, -1),
                                EscapePiece.MovementPattern.OMNI,
                                Coordinate.CoordinateType.HEX,
                                5,
                                Arrays.asList(
                                        new EscapeCoordinate(-2, 0),
                                        new EscapeCoordinate(-2, 1),
                                        new EscapeCoordinate(-1, 1),
                                        new EscapeCoordinate(0, 0),
                                        new EscapeCoordinate(1, -1)
                                ))

                )
        );
    }


}
