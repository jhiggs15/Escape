package escape.movement.pathfinding;

import escape.EscapeGameBuilder;
import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.movement.BoundsChecker;
import escape.movement.NeighborFinder;
import escape.required.Coordinate;
import escape.required.EscapePiece;
import escape.util.EscapeGameInitializer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class LineTest
{
    public NeighborFinder initializeNeighborFinder(Board board, EscapeGameInitializer egb, EscapePiece.MovementPattern movementPattern, Coordinate.CoordinateType coordinateType) throws Exception {
        int xMax = egb.getxMax();
        int yMax = egb.getyMax();
        NeighborFinder neighborFinder =new NeighborFinder(coordinateType, board, new BoundsChecker(xMax, yMax), false );
        neighborFinder.changeMovementPattern(movementPattern);

        return neighborFinder;
    }

    public boolean lineTest(String name, String filename, EscapeCoordinate from, EscapeCoordinate to, EscapePiece.MovementPattern movementPattern, Coordinate.CoordinateType coordinateType, int lengthOfPath, List<EscapeCoordinate> path) throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder(filename);
        Board board = new Board(egb.getGameInitializer());
        NeighborFinder neighborFinder = initializeNeighborFinder(board, egb.getGameInitializer(), movementPattern, coordinateType);
        boolean result = true;

        Line line = new Line(neighborFinder);
        line.changeType(movementPattern);
        List<EscapeCoordinate> generatedPath = line.findPath(from, to, board.getPieceAt(from));
        result = result && (generatedPath.isEmpty() || generatedPath.get(generatedPath.size() - 1).equals(to));
        result = result && generatedPath.size() == lengthOfPath;

        if(path != null)
        {
            for(int i = 0; i < path.size(); i++)
            {
                result = result && path.get(i).equals(generatedPath.get(i));
            }
        }

        return result;
    }

    @ParameterizedTest
    @MethodSource({"LINEARProvider"})
    void LINEARvalidatePath(String name, String filename, EscapeCoordinate from, EscapeCoordinate to, EscapePiece.MovementPattern movementPattern, Coordinate.CoordinateType coordinateType, int lengthOfPath, List<EscapeCoordinate> path) throws Exception {
        assertTrue(lineTest(name, filename, from, to, movementPattern, coordinateType, lengthOfPath, path));
    }

    @ParameterizedTest
    @MethodSource({"ORTHOGONALProvider"})
    void ORTHOGONALvalidatePath(String name, String filename, EscapeCoordinate from, EscapeCoordinate to, EscapePiece.MovementPattern movementPattern, Coordinate.CoordinateType coordinateType, int lengthOfPath, List<EscapeCoordinate> path) throws Exception {
        assertTrue(lineTest(name, filename, from, to, movementPattern, coordinateType, lengthOfPath, path));
    }

    @ParameterizedTest
    @MethodSource({"DIAGONALProvider"})
    void DIAGONALvalidatePath(String name, String filename, EscapeCoordinate from, EscapeCoordinate to, EscapePiece.MovementPattern movementPattern, Coordinate.CoordinateType coordinateType, int lengthOfPath, List<EscapeCoordinate> path) throws Exception {
        assertTrue(lineTest(name, filename, from, to, movementPattern, coordinateType, lengthOfPath, path));
    }

    static Stream<Arguments> LINEARProvider()
    {
        return (
                Stream.of(
                        Arguments.arguments("Path Length 0 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 0),
                                EscapePiece.MovementPattern.LINEAR,
                                Coordinate.CoordinateType.SQUARE,
                                0, null),
                        Arguments.arguments("Path Length 0 HEX",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 0),
                                EscapePiece.MovementPattern.LINEAR,
                                Coordinate.CoordinateType.HEX,
                                0, null),
                        Arguments.arguments("Path Length 1 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 1),
                                EscapePiece.MovementPattern.LINEAR,
                                Coordinate.CoordinateType.SQUARE,
                                1, null),
                        Arguments.arguments("Path Length 1 HEX",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 0),
                                EscapePiece.MovementPattern.LINEAR,
                                Coordinate.CoordinateType.HEX,
                                1, null),
                        Arguments.arguments("Path Length 2 HEX",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 2),
                                EscapePiece.MovementPattern.LINEAR,
                                Coordinate.CoordinateType.HEX,
                                2,
                                Arrays.asList(
                                        new EscapeCoordinate(-1, 1),
                                        new EscapeCoordinate(-2, 2)
                                )),
                        Arguments.arguments("Path Length 2 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 2),
                                EscapePiece.MovementPattern.LINEAR,
                                Coordinate.CoordinateType.SQUARE,
                                2,
                                Arrays.asList(
                                        new EscapeCoordinate(-1, 1),
                                        new EscapeCoordinate(-2, 2)
                                )),
                        Arguments.arguments("No Possible Path Not Witin Ability SQUARE",
                                "Escape/config/egc/InfiniteBoard.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -1),
                                EscapePiece.MovementPattern.LINEAR,
                                Coordinate.CoordinateType.SQUARE,
                                0,
                                null),
                        Arguments.arguments("No Possible Path Not Witin Ability HEX",
                                "Escape/config/egc/InfiniteBoard.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -1),
                                EscapePiece.MovementPattern.LINEAR,
                                Coordinate.CoordinateType.HEX,
                                0,
                                null),
                        Arguments.arguments("No Possible Path BLOCKED SQUARE",
                                "Escape/config/egc/TheBox.egc",
                                new EscapeCoordinate(2, 2),
                                new EscapeCoordinate(1, 2),
                                EscapePiece.MovementPattern.LINEAR,
                                Coordinate.CoordinateType.SQUARE,
                                0,
                                null),
                        Arguments.arguments("No Possible Path BLOCKED HEX",
                                "Escape/config/egc/TheBox.egc",
                                new EscapeCoordinate(2, 2),
                                new EscapeCoordinate(1, 2),
                                EscapePiece.MovementPattern.LINEAR,
                                Coordinate.CoordinateType.HEX,
                                0,
                                null)

                )
        );
    }

    static Stream<Arguments> ORTHOGONALProvider()
    {
        return (
                Stream.of(
                        Arguments.arguments("Path Length 0 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 0),
                                EscapePiece.MovementPattern.ORTHOGONAL,
                                Coordinate.CoordinateType.SQUARE,
                                0, null),
                        Arguments.arguments("Path Length 1 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 0),
                                EscapePiece.MovementPattern.ORTHOGONAL,
                                Coordinate.CoordinateType.SQUARE,
                                1, null),
                        Arguments.arguments("Path Length 2 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 0),
                                EscapePiece.MovementPattern.ORTHOGONAL,
                                Coordinate.CoordinateType.SQUARE,
                                2,
                                Arrays.asList(
                                        new EscapeCoordinate(-1, 0),
                                        new EscapeCoordinate(-2, 0)
                                )),
                        Arguments.arguments("No Possible Path Not Witin Ability SQUARE",
                                "Escape/config/egc/InfiniteBoard.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -2),
                                EscapePiece.MovementPattern.ORTHOGONAL,
                                Coordinate.CoordinateType.SQUARE,
                                0,
                                null),
                        Arguments.arguments("No Possible Path BLOCKED SQUARE",
                                "Escape/config/egc/TheBox.egc",
                                new EscapeCoordinate(2, 2),
                                new EscapeCoordinate(1, 1),
                                EscapePiece.MovementPattern.ORTHOGONAL,
                                Coordinate.CoordinateType.SQUARE,
                                0,
                                null)

                )
        );
    }

    static Stream<Arguments> DIAGONALProvider()
    {
        return (
                Stream.of(
                        Arguments.arguments("Path Length 0 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 0),
                                EscapePiece.MovementPattern.DIAGONAL,
                                Coordinate.CoordinateType.SQUARE,
                                0, null),
                        Arguments.arguments("Path Length 0 HEX",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 0),
                                EscapePiece.MovementPattern.DIAGONAL,
                                Coordinate.CoordinateType.HEX,
                                0, null),
                        Arguments.arguments("Path Length 1 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 1),
                                EscapePiece.MovementPattern.DIAGONAL,
                                Coordinate.CoordinateType.SQUARE,
                                1, null),
                        Arguments.arguments("Path Length 1 HEX",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 1),
                                EscapePiece.MovementPattern.DIAGONAL,
                                Coordinate.CoordinateType.HEX,
                                1, null),
                        Arguments.arguments("Path Length 2 HEX",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 2),
                                EscapePiece.MovementPattern.DIAGONAL,
                                Coordinate.CoordinateType.HEX,
                                2,
                                Arrays.asList(
                                        new EscapeCoordinate(-1, 1),
                                        new EscapeCoordinate(-2, 2)
                                )),
                        Arguments.arguments("Path Length 2 SQUARE",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 2),
                                EscapePiece.MovementPattern.DIAGONAL,
                                Coordinate.CoordinateType.SQUARE,
                                2,
                                Arrays.asList(
                                        new EscapeCoordinate(-1, 1),
                                        new EscapeCoordinate(-2, 2)
                                )),
                        Arguments.arguments("No Possible Path Not Witin Ability SQUARE",
                                "Escape/config/egc/InfiniteBoard.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, 1),
                                EscapePiece.MovementPattern.DIAGONAL,
                                Coordinate.CoordinateType.SQUARE,
                                0,
                                null),
                        Arguments.arguments("No Possible Path Not Witin Ability HEX",
                                "Escape/config/egc/InfiniteBoard.egc",
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 1),
                                EscapePiece.MovementPattern.DIAGONAL,
                                Coordinate.CoordinateType.HEX,
                                0,
                                null),
                        Arguments.arguments("No Possible Path BLOCKED SQUARE",
                                "Escape/config/egc/TheBox.egc",
                                new EscapeCoordinate(2, 2),
                                new EscapeCoordinate(3, 3),
                                EscapePiece.MovementPattern.DIAGONAL,
                                Coordinate.CoordinateType.SQUARE,
                                0,
                                null),
                        Arguments.arguments("No Possible Path BLOCKED HEX",
                                "Escape/config/egc/TheBox.egc",
                                new EscapeCoordinate(2, 2),
                                new EscapeCoordinate(1, 1),
                                EscapePiece.MovementPattern.DIAGONAL,
                                Coordinate.CoordinateType.HEX,
                                0,
                                null)

                )
        );
    }


}
