package escape;

import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.required.EscapePiece;
import escape.required.GameObserver;
import escape.util.EscapeGameInitializer;

public class GameManager implements EscapeGameManager<EscapeCoordinate> {

    private Board gameBoard;

    public GameManager(EscapeGameInitializer gameInitializer)
    {
        this.gameBoard = new Board(gameInitializer);
    }

    @Override
    public boolean move(EscapeCoordinate from, EscapeCoordinate to) {
        return false;
    }

    @Override
    public EscapePiece getPieceAt(EscapeCoordinate coordinate) {
        return null;
    }

    public EscapeCoordinate makeCoordinate(int x, int y)
    {
        return gameBoard.makeCoordinate(x, y);
    }

    public GameObserver addObserver(GameObserver observer)
    {
        return EscapeGameManager.super.addObserver(observer);
    }

    public GameObserver removeObserver(GameObserver observer)
    {
        return EscapeGameManager.super.removeObserver(observer);
    }
}
