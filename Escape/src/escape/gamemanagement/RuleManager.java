package escape.gamemanagement;

import escape.board.Board;
import escape.board.EscapeCoordinate;
import escape.board.EscapeGamePiece;
import escape.required.Player;
import escape.required.Rule;
import escape.util.RuleDescriptor;

import java.util.*;

public class RuleManager
{
    private ScoreManager scoreManager;
    private TurnManager turnManager;
    private ObserverManager observerManager;
    private Map<Rule.RuleID, RuleDescriptor> rules = new Hashtable<>();
    private boolean gameIsOver = false;

    public RuleManager(RuleDescriptor[] rules, ScoreManager scoreManager, TurnManager turnManager, ObserverManager observerManager) {
        this.scoreManager = scoreManager;
        this.turnManager = turnManager;
        this.observerManager = observerManager;
        if(rules != null)
            for(RuleDescriptor rule : rules)
                this.rules.put(rule.ruleId, rule);
    }

    public RuleManager(RuleDescriptor[] rules) {
        this.scoreManager = new ScoreManager();
        this.turnManager = new TurnManager();
        this.observerManager = new ObserverManager();

    }

    public boolean gameIsOver()
    {
        return gameIsOver;
    }

    public boolean checkGame()
    {
        for(Rule.RuleID rule : rules.keySet())
            if(!gameIsOver)
                checkRule(rule);
        checkIfPlayersHavePieces();
        return gameIsOver;
    }

    private void checkIfPlayersHavePieces()
    {
        if(!gameIsOver)
        {
            if(!scoreManager.hasPiecesLeft(Player.PLAYER1))
                markWinner(Player.PLAYER2);
            else if(!scoreManager.hasPiecesLeft(Player.PLAYER2))
                markWinner(Player.PLAYER1);
        }

    }

    private boolean hasConflictRule()
    {
        Set<Rule.RuleID> ruleSet = rules.keySet();
        return ruleSet.contains(Rule.RuleID.REMOVE);
    }

    public boolean enforceConflictRules(Board gameBoard, EscapeCoordinate to)
    {
        EscapeGamePiece pieceAtDest = gameBoard.getPieceAt(to);
        if(pieceAtDest.getPlayer() != turnManager.getCurrentPlayer() && hasConflictRule())
        {
            gameBoard.removePieceAt(to);
            return true;
        }
        else
            return false;
    }

    private void checkRule(Rule.RuleID rule)
    {
        switch (rule)
        {
            case SCORE:
                checkScore();
                break;
            case TURN_LIMIT:
                checkTurnLimit();
                break;
//            case REMOVE:
//                break;
//            case POINT_CONFLICT:
//                break;
//            default:
//                break;
        }
    }

    private void checkScore()
    {
        int maxScore = rules.get(Rule.RuleID.SCORE).ruleValue;
//        if(scoreManager.getTotalPossibleScore(Player.PLAYER1) < maxScore
//                && scoreManager.getTotalPossibleScore(Player.PLAYER2) < maxScore && rules.keySet().size() == 1)
//            markTie();
        if(scoreManager.getPlayerScore(Player.PLAYER1) >= maxScore)
            markWinner(Player.PLAYER1);
        else if (scoreManager.getPlayerScore(Player.PLAYER2) >= maxScore)
            markWinner(Player.PLAYER2);
    }

    private void checkTurnLimit()
    {
        int maxTurns = rules.get(Rule.RuleID.TURN_LIMIT).ruleValue;
        if(turnManager.getTurnNumber() > maxTurns)
        {
            if(scoreManager.getPlayerScore(Player.PLAYER1) > scoreManager.getPlayerScore(Player.PLAYER2) )
                markWinner(Player.PLAYER1);
            else if(scoreManager.getPlayerScore(Player.PLAYER2) > scoreManager.getPlayerScore(Player.PLAYER1) )
                markWinner(Player.PLAYER2);
            else
                markTie();
        }
    }

    private void markWinner(Player player)
    {
        observerManager.notifyAll("Player " + (player.ordinal() + 1) + " wins");
        gameIsOver = true;
    }

    private void markTie()
    {
        observerManager.notifyAll("Game over in a draw");
        gameIsOver = true;
    }


}
