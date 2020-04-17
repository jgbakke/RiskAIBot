package com.sillysoft.lux.agent.utils.simulation;

import javafx.util.Pair;

public class MonteCarloSimulator {

    public SimulationResult simulateBattle(int attackers, int defenders, int simulations){
        int cumSurvivors = 0;
        int cumVictories = 0;

        for(int sim = 0; sim < simulations; sim++){
            int currentAttackers = attackers;
            int currentDefenders = defenders;

            while(currentAttackers > 0) {
                CombatRoundResult rollResult = CombatRoundResult.lookup(attackers, defenders);
                Pair<Integer, Integer> casualties = rollResult.getCasualtiesInflicted();

                currentAttackers -= casualties.getKey();
                currentDefenders -= casualties.getValue();

                if (currentDefenders < 1) {
                    cumSurvivors += currentAttackers;
                    cumVictories++;
                    break;
                }
            }
        }

        double chanceToWin = (double)cumVictories / simulations;
        int averageSurvivors = (int)Math.round((double)cumSurvivors / simulations);

        return new SimulationResult(chanceToWin > 0.5, averageSurvivors, chanceToWin);
    }
}
