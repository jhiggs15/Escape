package escape.board;

import escape.gamemanagement.RuleManager;
import escape.gamemanagement.Score;
import escape.exception.NoPathExists;
import escape.movement.MoveManager;
import escape.piece.EscapeGamePiece;
import escape.required.EscapePiece;
import escape.required.LocationType;
import escape.required.Player;
import escape.required.Rule;
import escape.util.EscapeGameInitializer;
import escape.util.LocationInitializer;
import escape.util.PieceTypeDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

    private HashMap<EscapeCoordinate, BoardSpace> board = new HashMap<>();
    private MoveManager moveManager;
    private List<EscapeGamePiece> piecesOnTheBoard = new ArrayList<>();
    private HashMap<EscapeCoordinate, BoardSpace> exitSpaces = new HashMap<>();


    public Board(EscapeGameInitializer gameInitializer)
    {
        boolean hasConflictRule = gameInitializer.getRules() != null && Arrays.stream(gameInitializer.getRules()).anyMatch(rule -> rule.ruleId.equals(Rule.RuleID.REMOVE) );
        this.moveManager = new MoveManager(gameInitializer.getCoordinateType(), this,
                gameInitializer.getxMax(), gameInitializer.getyMax(), hasConflictRule);

        HashMap<EscapePiece.PieceName, EscapeGamePiece> piecesWithoutOwner = new HashMap();
        for(PieceTypeDescriptor pieceTypeDescriptor : gameInitializer.getPieceTypes())
            piecesWithoutOwner.put(pieceTypeDescriptor.getPieceName(), new EscapeGamePiece(pieceTypeDescriptor, this));

        // create pieces
        // create locations with pieces
        for(LocationInitializer location : gameInitializer.getLocationInitializers())
        {
            // gets the piece at the location and null otherwise
            EscapeGamePiece pieceAtCurrentLocation = piecesWithoutOwner.get(location.pieceName);
            // if a piece exists in the current location create a new piece with a player attached to it
            if(pieceAtCurrentLocation != null)
            {
                pieceAtCurrentLocation = pieceAtCurrentLocation.makeCopy(location.player);
                piecesOnTheBoard.add(pieceAtCurrentLocation);
            }
            // creates a board space on the board, or adds to it depending on the context
            createBoardSpace(makeCoordinate(location.x, location.y), location.locationType, pieceAtCurrentLocation);
        }
    }

    void createBoardSpace(EscapeCoordinate coordinate, LocationType locationType, EscapeGamePiece piece)
    {
        if(spaceExists(coordinate))
            board.get(coordinate).addPiece(piece);
        else
        {
            BoardSpace boardSpace = new BoardSpace(piece, locationType);
            if(locationType != null && locationType.equals(LocationType.EXIT)) exitSpaces.put(coordinate, boardSpace);
            board.put(
                    coordinate,
                    boardSpace
            );
        }



    }

    public EscapeGamePiece getPieceAt(EscapeCoordinate coordinate)
    {
        if(spaceExists(coordinate))
        {
            BoardSpace boardSpace = board.get(coordinate);
            return boardSpace.getPiece();
        }

        return null;
    }

    HashMap<EscapeCoordinate, BoardSpace> getBoard() {
        return board;
    }

    public LocationType getLocationType(EscapeCoordinate coordinate)
    {
        if(spaceExists(coordinate))
            return board.get(coordinate).getType();
        else
            return LocationType.CLEAR;
    }

    public List<EscapeGamePiece> getPiecesOnTheBoard()
    {
        return piecesOnTheBoard;
    }

    public boolean canMove(Player player, EscapeCoordinate from, EscapeCoordinate to)
    {
        return moveManager.canMove(player, from, to);
    }

    public Score move(Player player, EscapeCoordinate from, EscapeCoordinate to, RuleManager ruleManager)
    {
        if(canMove(player, from, to))
        {
            // no path exists
            EscapeGamePiece targetedPiece = getPieceAt(from);
            List<EscapeCoordinate> path = moveManager.findPath(from, to, targetedPiece);
            int pathSize = path.size();
            if(pathSize == 0 && !from.equals(to)) throw new NoPathExists(from, to);

            // the shortest path is larger than the possible spaces the piece can move
            int pieceValue = targetedPiece.getMovementValue();
            if(pathSize > pieceValue) throw new NoPathExists(from, to, pathSize, pieceValue);

            EscapeCoordinate destination = executeMove(from, path, ruleManager);

            Score score = findScore(player, destination);
            return score;

        }

        return null;

    }

    private EscapeCoordinate executeMove(EscapeCoordinate from, List<EscapeCoordinate> path, RuleManager ruleManager)
    {
        EscapeCoordinate destination = path.get(path.size() - 1);

        if(spaceExists(destination) && !board.get(destination).spaceIsEmpty())
            if(!ruleManager.enforceConflictRules(this, destination))
                throw new NoPathExists(from, destination, true);

        EscapeGamePiece pieceToMove = board.get(from).removePiece();

        List<EscapeCoordinate> possibleNewDestination =
                path.stream()
                        .filter(coordinate -> exitSpaces.containsKey(coordinate))
                        .collect(Collectors.toList());
        if(possibleNewDestination.size() >= 1)
            destination = possibleNewDestination.get(0);



        createBoardSpace(destination, LocationType.CLEAR, pieceToMove);

        return destination;
    }


    private Score findScore(Player player, EscapeCoordinate to)
    {
        Score score = new Score(player);

        if(exitSpaces.containsKey(to))
        {
            EscapeGamePiece removedPiece = removePieceAt(to);;
            score.incrementPlayerScore(removedPiece);
        }

        return score;
    }

    public EscapeGamePiece removePieceAt(EscapeCoordinate coordinate)
    {
        EscapeGamePiece removedPiece = board.get(coordinate).removePiece();
        piecesOnTheBoard.remove(removedPiece);
        return removedPiece;
    }

    public EscapeCoordinate makeCoordinate(int x, int y)
    {
        return new EscapeCoordinate(x, y);
    }

    private boolean spaceExists(EscapeCoordinate coordinate)
    {
        return board.get(coordinate) != null;
    }

    public boolean isAccessible(EscapeCoordinate coordinate, EscapeGamePiece piece)
    {
        return (spaceExists(coordinate) && board.get(coordinate).isAccessible(piece)) || !spaceExists(coordinate) ;
    }


}
