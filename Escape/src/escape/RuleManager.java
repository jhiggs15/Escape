package escape;

import escape.required.Player;
import escape.required.Rule;
import escape.util.RuleDescriptor;

import java.util.*;

public class RuleManager
{
    private ScoreManager scoreManager;
    private TurnManager turnManager;
    private Map<Rule.RuleID, RuleDescriptor> rules = new Hashtable<>();
    private boolean gameIsOver = false;

    public RuleManager(RuleDescriptor[] rules, ScoreManager scoreManager, TurnManager turnManager) {
        this.scoreManager = scoreManager;
        this.turnManager = turnManager;
        for(RuleDescriptor rule : rules)
            this.rules.put(rule.ruleId, rule);
    }

    public boolean gameIsOver()
    {
        return gameIsOver;
    }

    public void checkGame()
    {
        for(Rule.RuleID rule : rules.keySet())
            if(!gameIsOver) checkRule(rule);
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
        System.out.print("PLAYER " + (player.ordinal() + 1) + " wins");
        gameIsOver = true;
    }

    private void markTie()
    {
        System.out.print("Game Over");
        gameIsOver = true;
    }


}
