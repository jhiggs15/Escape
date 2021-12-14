package escape.gamemanagement;

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
    private ObserverManager observerManager;


    public GameManager(EscapeGameInitializer gameInitializer)
    {
        gameBoard = new Board(gameInitializer);
        scoreManager = new ScoreManager();
        scoreManager.updatePiecesOnBoard(gameBoard.getPiecesOnTheBoard());
        turnManager = new TurnManager();
        observerManager = new ObserverManager();
        ruleManager = new RuleManager(gameInitializer.getRules(), scoreManager, turnManager, observerManager);

    }

    @Override
    public boolean move(EscapeCoordinate from, EscapeCoordinate to)
    {
        try
        {
            if(!ruleManager.gameIsOver())
            {
                Player currentPlayer = turnManager.getCurrentPlayer();
                Score score = gameBoard.move(currentPlayer, from, to, ruleManager);
                scoreManager.addScore(score);
                scoreManager.updatePiecesOnBoard(gameBoard.getPiecesOnTheBoard());
                turnManager.endTurn();
                if(!ruleManager.checkGame())
                    observerManager.notifyAll(currentPlayer.toString() + " move was successful");
                return true;
            }
            else
                return false;

        }
        catch (EscapeException exception)
        {
            observerManager.notifyAll(exception.getClass().toString(), exception);
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
        return observerManager.addObserver(observer);
    }

    public GameObserver removeObserver(GameObserver observer)
    {
        return observerManager.removeObserver(observer);
    }
}
