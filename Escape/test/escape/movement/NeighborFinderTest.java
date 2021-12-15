package escape.movement;

import escape.EscapeGameBuilder;
import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.board.EscapeGamePiece;
import escape.required.Coordinate;
import escape.required.EscapePiece;
import escape.required.Player;
import escape.util.EscapeGameInitializer;
import escape.util.PieceAttribute;
import escape.util.PieceTypeDescriptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static junit.framework.TestCase.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NeighborFinderTest
{

    public NeighborFinder initializeNeighborFinder(Board board, EscapeGameInitializer egb, EscapePiece.MovementPattern movementPattern, Coordinate.CoordinateType coordinateType) throws Exception {
        int xMax = egb.getxMax();
        int yMax = egb.getyMax();
        NeighborFinder neighborFinder =new NeighborFinder(coordinateType, board, new BoundsChecker(xMax, yMax), false );
        neighborFinder.changeMovementPattern(movementPattern);

        return neighborFinder;
    }

    // ------ get Neighbors

    public static EscapeGamePiece makePiece(Player player, EscapePiece.PieceName pieceName, EscapePiece.MovementPattern movementPattern, PieceAttribute[] attributes){
        return new EscapeGamePiece(player,
                new PieceTypeDescriptor(
                        pieceName,
                        movementPattern,
                        attributes));
    }

    boolean genericNeighborCheck(String filename, EscapePiece.MovementPattern movementPattern, List<EscapePiece.PieceAttributeID> attributes, Coordinate.CoordinateType coordinateType, EscapeCoordinate coordinate, List<EscapeCoordinate> neighbors) throws Exception {
        EscapeGameBuilder egb = new EscapeGameBuilder(filename);
        Board board = new Board(egb.getGameInitializer());
        NeighborFinder neighborFinder = initializeNeighborFinder(board, egb.getGameInitializer(), movementPattern, coordinateType);

        List<PieceAttribute> pieceAttributes =  attributes.stream().map(attributeID -> new PieceAttribute(attributeID, 1)).collect(Collectors.toList());

        EscapeGamePiece snail = makePiece(
                Player.PLAYER1,
                EscapePiece.PieceName.SNAIL,
                movementPattern,
                pieceAttributes.toArray(new PieceAttribute[pieceAttributes.size()]));

        List<EscapeCoordinate> foundNeighbors = neighborFinder.getNeighbors(coordinate, snail);
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


    // -------- Movement Patterns
    @ParameterizedTest
    @MethodSource("OMNIProvider")
    void OMNINeighborCheck(String name, String filename, Coordinate.CoordinateType coordinateType, EscapeCoordinate coordinate, List<EscapeCoordinate> neighbors) throws Exception {
        assertTrue(genericNeighborCheck(filename, EscapePiece.MovementPattern.OMNI, Arrays.asList(EscapePiece.PieceAttributeID.DISTANCE), coordinateType, coordinate, neighbors));

    }


    static Stream<Arguments> OMNIProvider()
    {
        return (
                Stream.of(
                        Arguments.arguments("SQUARE 0,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
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
                        Arguments.arguments("SQUARE POS POS",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(1, 1),
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
                        Arguments.arguments("SQUARE NEG POS",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(-4, 3),
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
                        Arguments.arguments("SQUARE NEG NEG",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(-4, -7),
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
                        Arguments.arguments("SQUARE NEG NEG",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(9, -3),
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
                        Arguments.arguments("SQUARE All Out of Bounds",
                                "Escape/config/egc/NoNeighbors.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(1, 1),
                                new ArrayList<>()
                        ),
                        Arguments.arguments("SQUARE Some are out of Bounds",
                                "Escape/config/egc/PlayersHaveSamePiece.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(40, 30),
                                Arrays.asList(
                                        new EscapeCoordinate(39, 30),
                                        new EscapeCoordinate(40, 29),
                                        new EscapeCoordinate(39, 29))
                        ),
                        Arguments.arguments("SQUARE At the boundary",
                                "Escape/config/egc/JustBigEnough.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(2, 2),
                                Arrays.asList(
                                        new EscapeCoordinate(3, 2),
                                        new EscapeCoordinate(1, 2),
                                        new EscapeCoordinate(2, 3),
                                        new EscapeCoordinate(2, 1),
                                        new EscapeCoordinate(3, 3),
                                        new EscapeCoordinate(1, 1),
                                        new EscapeCoordinate(1, 3),
                                        new EscapeCoordinate(3, 1))
                        ),

                        // HEX
                        Arguments.arguments("HEX 0,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                Arrays.asList(
                                        new EscapeCoordinate(1, 0),
                                        new EscapeCoordinate(-1, 0),
                                        new EscapeCoordinate(0, 1),
                                        new EscapeCoordinate(0, -1),
                                        new EscapeCoordinate(-1, 1),
                                        new EscapeCoordinate(1, -1))
                        ),
                        Arguments.arguments("HEX POS POS",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(1, 1),
                                Arrays.asList(
                                        new EscapeCoordinate(2, 1),
                                        new EscapeCoordinate(0, 1),
                                        new EscapeCoordinate(1, 2),
                                        new EscapeCoordinate(0, 2),
                                        new EscapeCoordinate(1, 0),
                                        new EscapeCoordinate(2, 0))
                        ),
                        Arguments.arguments("HEX NEG POS",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(-4, 3),
                                Arrays.asList(
                                        new EscapeCoordinate(-3, 3),
                                        new EscapeCoordinate(-5, 3),
                                        new EscapeCoordinate(-4, 4),
                                        new EscapeCoordinate(-4, 2),
                                        new EscapeCoordinate(-5, 4),
                                        new EscapeCoordinate(-3, 2))
                        ),
                        Arguments.arguments("HEX NEG NEG",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(-4, -7),
                                Arrays.asList(
                                        new EscapeCoordinate(-3, -7),
                                        new EscapeCoordinate(-5, -7),
                                        new EscapeCoordinate(-4, -6),
                                        new EscapeCoordinate(-4, -8),
                                        new EscapeCoordinate(-5, -6),
                                        new EscapeCoordinate(-3, -8))

                        ),
                        Arguments.arguments("HEX NEG NEG",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(9, -3),
                                Arrays.asList(
                                        new EscapeCoordinate(10, -3),
                                        new EscapeCoordinate(8, -3),
                                        new EscapeCoordinate(9, -2),
                                        new EscapeCoordinate(9, -4),
                                        new EscapeCoordinate(8, -2),
                                        new EscapeCoordinate(10, -4))
                        ),
                        Arguments.arguments("HEX All Out of Bounds",
                                "Escape/config/egc/NoNeighbors.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(1, 1),
                                new ArrayList<>()
                        ),
                        Arguments.arguments("HEX Some are out of Bounds",
                                "Escape/config/egc/PlayersHaveSamePiece.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(40, 30),
                                Arrays.asList(
                                        new EscapeCoordinate(39, 30),
                                        new EscapeCoordinate(40, 29))
                        ),
                        Arguments.arguments("HEX At the boundary",
                                "Escape/config/egc/JustBigEnough.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(2, 2),
                                Arrays.asList(
                                        new EscapeCoordinate(3, 2),
                                        new EscapeCoordinate(1, 2),
                                        new EscapeCoordinate(2, 3),
                                        new EscapeCoordinate(2, 1),
                                        new EscapeCoordinate(1, 3),
                                        new EscapeCoordinate(3, 1))
                        )



                )
        );

    }

    // --------- Attributes

    @ParameterizedTest
    @MethodSource("UNBLOCKProvider")
    void UNBLOCKNeighborCheck(String name, String filename, Coordinate.CoordinateType coordinateType, EscapeCoordinate coordinate, List<EscapeCoordinate> neighbors) throws Exception {
        assertTrue(genericNeighborCheck(filename, EscapePiece.MovementPattern.OMNI, Arrays.asList(EscapePiece.PieceAttributeID.DISTANCE, EscapePiece.PieceAttributeID.UNBLOCK), coordinateType, coordinate, neighbors));

    }

    static Stream<Arguments> UNBLOCKProvider()
    {
        return (
                Stream.of(
                        Arguments.arguments("UNBLOCK can move past blocks",
                                "Escape/config/egc/Block.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(2, 2),
                                Arrays.asList(
                                        new EscapeCoordinate(3, 2),
                                        new EscapeCoordinate(1, 2),
                                        new EscapeCoordinate(2, 3),
                                        new EscapeCoordinate(2, 1),
                                        new EscapeCoordinate(3, 3),
                                        new EscapeCoordinate(1, 1),
                                        new EscapeCoordinate(1, 3),
                                        new EscapeCoordinate(3, 1))
                        ),
                        Arguments.arguments("UNBLOCK cannot move past pieces",
                                "Escape/config/egc/TheBox.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(2, 2),
                                new ArrayList<EscapeCoordinate>())
                        )

        );

    }

    @ParameterizedTest
    @MethodSource("FLYProvider")
    void FLYNeighborCheck(String name, String filename, Coordinate.CoordinateType coordinateType, EscapeCoordinate coordinate, List<EscapeCoordinate> neighbors) throws Exception {
        assertTrue(genericNeighborCheck(filename, EscapePiece.MovementPattern.OMNI, Arrays.asList(EscapePiece.PieceAttributeID.FLY), coordinateType, coordinate, neighbors));

    }

    static Stream<Arguments> FLYProvider()
    {
        return (
                Stream.of(
                        Arguments.arguments("UNBLOCK can move past blocks",
                                "Escape/config/egc/Block.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(2, 2),
                                Arrays.asList(
                                        new EscapeCoordinate(3, 2),
                                        new EscapeCoordinate(1, 2),
                                        new EscapeCoordinate(2, 3),
                                        new EscapeCoordinate(2, 1),
                                        new EscapeCoordinate(3, 3),
                                        new EscapeCoordinate(1, 1),
                                        new EscapeCoordinate(1, 3),
                                        new EscapeCoordinate(3, 1))
                        ),
                        Arguments.arguments("UNBLOCK cannot move past pieces",
                                "Escape/config/egc/TheBox.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(2, 2),
                                new ArrayList<EscapeCoordinate>())
                )
        );

    }



    // ----- minimum Distance


    @ParameterizedTest
    @MethodSource("OMNIDistanceProvider")
    void OMNIDistanceCheck(String name, String filename, Coordinate.CoordinateType coordinateType, EscapeCoordinate from, EscapeCoordinate to, int distance) throws Exception
    {
        EscapeGameBuilder egb = new EscapeGameBuilder(filename);
        Board board = new Board(egb.getGameInitializer());
        NeighborFinder neighborFinder = initializeNeighborFinder(board, egb.getGameInitializer(), EscapePiece.MovementPattern.OMNI, coordinateType);
        assertEquals(neighborFinder.minimumDistance(from, to), distance );
    }


    static Stream<Arguments> OMNIDistanceProvider()
    {
        return (
                Stream.of(
                        // zero away
                        Arguments.arguments("SQUARE 0,0 -> 0,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 0),
                                0),
                        // one away

                        Arguments.arguments("SQUARE 0,0 -> 1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 1),
                                1),
                        Arguments.arguments("SQUARE 0,0 -> -1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 1),
                                1),
                        Arguments.arguments("SQUARE 0,0 -> 0,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 1),
                                1),
                        Arguments.arguments("SQUARE 0,0 -> 1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 1),
                                1),
                        Arguments.arguments("SQUARE 0,0 -> -1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 0),
                                1),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 0),
                                1),
                        Arguments.arguments("SQUARE 0,0 -> -1,-1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, -1),
                                1),
                        Arguments.arguments("SQUARE 0,0 -> 1,-1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, -1),
                                1),

                        // two away
                        Arguments.arguments("SQUARE 0,0 -> -2, 2",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 2),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> -1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 2),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 0,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 2),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 2),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, 2),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> -1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 1),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, 1),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, 0),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 0),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, -1),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -1),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, -2),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, -2),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, -2),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, -2),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -2),
                                2),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -2),
                                2),

                        // three away
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-3, 3),
                                3),
                        Arguments.arguments("SQUARE 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.SQUARE,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 3),
                                3),


                        // HEX

                        // zero away
                        Arguments.arguments("HEX 0,0 -> 0,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 0),
                                0),
                        // one away

                        Arguments.arguments("HEX 0,0 -> -1,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 1),
                                1),
                        Arguments.arguments("HEX 0,0 -> 0,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 1),
                                1),
                        Arguments.arguments("HEX 0,0 -> -1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 0),
                                1),
                        Arguments.arguments("HEX 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, 0),
                                1),
                        Arguments.arguments("HEX 0,0 -> -1,-1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, -1),
                                1),
                        Arguments.arguments("HEX 0,0 -> 1,-1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, -1),
                                1),

                        // two away
                        Arguments.arguments("HEX 0,0 -> -2, 2",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 2),
                                2),
                        Arguments.arguments("HEX 0,0 -> -1,2",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 2),
                                2),
                        Arguments.arguments("HEX 0,0 -> 0,2",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, 2),
                                2),
                        Arguments.arguments("HEX 0,0 -> -2,1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 1),
                                2),
                        Arguments.arguments("HEX 0,0 -> 2,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, 0),
                                2),
                        Arguments.arguments("HEX 0,0 -> -2,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, 0),
                                2),
                        Arguments.arguments("HEX 0,0 -> -2,-1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-2, -1),
                                2),
                        Arguments.arguments("HEX 0,0 -> 2,-1",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -1),
                                2),
                        Arguments.arguments("HEX 0,0 -> 0,-2",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(0, -2),
                                2),
                        Arguments.arguments("HEX 0,0 -> 1,-2",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(1, -2),
                                2),
                        Arguments.arguments("HEX 0,0 -> 2,-2",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(2, -2),
                                2),


                        // three away
                        Arguments.arguments("HEX 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-1, 3),
                                3),
                        Arguments.arguments("HEX 0,0 -> 1,0",
                                "Escape/config/egc/NoPiecesInfinite.egc",
                                Coordinate.CoordinateType.HEX,
                                new EscapeCoordinate(0, 0),
                                new EscapeCoordinate(-3, 1),
                                3)


                        )
        );

    }





}
