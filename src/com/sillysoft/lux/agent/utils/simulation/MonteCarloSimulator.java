package com.sillysoft.lux.agent.utils.simulation;

import com.sillysoft.lux.Country;
import com.sillysoft.lux.agent.utils.MissionBenefit;
import javafx.util.Pair;

public class MonteCarloSimulator {

    private final int BATTLE_SIMULATIONS = 500;

    public PathSimulationResult simulatePathResults(Country[] countries, int startingArmies, int simulations){
        double averageCountriesCaptured = 0;
        double armiesLeft = 0;
        double reachedContinentChance = 0;

        for(int trial = 0; trial < simulations; trial++){
            PathSimulationResult result = simulatePathResults(countries, startingArmies);
            averageCountriesCaptured += result.countriesCaptured;
            armiesLeft = result.armiesLeft;
            reachedContinentChance += result.reachedContinent ? 1 : 0;
        }

        averageCountriesCaptured /= simulations;
        armiesLeft /= simulations;

        return new PathSimulationResult(
                (int)Math.round(averageCountriesCaptured),
                (int)Math.round(armiesLeft),
                reachedContinentChance / simulations > 0.5
        );

    }

    public PathSimulationResult simulatePathResults(Country[] countries, int startingArmies){
        int currentArmies = startingArmies;
        int currentCountry = 1;

        while(currentCountry < countries.length && currentArmies > 1){
            currentArmies--; // Need to leave 1 back

            SimulationResult battleResult = simulateBattle(
                    currentArmies,
                    countries[currentCountry].getArmies(),
                    BATTLE_SIMULATIONS
            );

            currentArmies = (int)Math.round(battleResult.averageArmiesRemaining);

            if(battleResult.victory){
                currentCountry++;
            }
        }

        return new PathSimulationResult(currentCountry, currentArmies, currentCountry >= countries.length);
    }

    public SimulationResult simulateBattle(int attackers, int defenders){
        return simulateBattle(attackers, defenders, BATTLE_SIMULATIONS);
    }

    public SimulationResult simulateBattle(int attackers, int defenders, int simulations){
        int cumSurvivors = 0;
        int cumVictories = 0;

        for(int sim = 0; sim < simulations; sim++){
            int currentAttackers = attackers;
            int currentDefenders = defenders;

            while(currentAttackers > 0) {
                CombatRoundResult rollResult = CombatRoundResult.lookup(currentAttackers, currentDefenders);
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
        double averageSurvivors = (double)cumSurvivors / simulations;

        return new SimulationResult(chanceToWin > 0.5, averageSurvivors, chanceToWin);
    }
}
