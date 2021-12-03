package escape;

import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.exception.EscapeException;
import escape.required.EscapePiece;
import escape.required.GameObserver;
import escape.required.Player;
import escape.util.EscapeGameInitializer;

public class GameManager implements EscapeGameManager<EscapeCoordinate> {

    private Board gameBoard;
    private ScoreManager scoreManager;
    private TurnManager turnManager;
    private RuleManager ruleManager;


    public GameManager(EscapeGameInitializer gameInitializer)
    {
        gameBoard = new Board(gameInitializer);
        scoreManager = new ScoreManager();
        turnManager = new TurnManager();
        ruleManager = new RuleManager(gameInitializer.getRules(), scoreManager, turnManager);
    }

    @Override
    public boolean move(EscapeCoordinate from, EscapeCoordinate to)
    {
        try
        {
            if(!ruleManager.gameIsOver())
            {
                Player currentPlayer = turnManager.getCurrentPlayer();
                Score score = gameBoard.move(currentPlayer, from, to);
                scoreManager.addScore(score);
                turnManager.endTurn();
                ruleManager.checkGame();
                return true;
            }
            else
                return false;

        }
        catch (EscapeException exception)
        {
            System.err.println(exception);
            return false;
        }
    }

    public int getPlayerScore(Player player)
    {
        return scoreManager.getPlayerScore(player);
    }

    public Player whoseTurn()
    {
        return turnManager.getCurrentPlayer();
    }

    @Override
    public EscapePiece getPieceAt(EscapeCoordinate coordinate) {
        return gameBoard.getPieceAt(coordinate);
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
