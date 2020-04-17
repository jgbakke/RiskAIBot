package com.sillysoft.lux.agent.utils.simulation;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Random;

public class CombatRoundResult {

    private static final Random rng = new Random();

    // Map attacker dice rolled to defender dice rolled
    private static HashMap<Pair<Integer, Integer>, CombatRoundResult> diceLookup =
            new HashMap<Pair<Integer, Integer>, CombatRoundResult>(){{
                put(new Pair<Integer, Integer>(1, 1),
                        new CombatRoundResult(0.583, 0.417, 0, 1));

                put(new Pair<Integer, Integer>(2, 1),
                        new CombatRoundResult(0.421, 0.579, 0, 1));

                put(new Pair<Integer, Integer>(3, 1),
                        new CombatRoundResult(0.34, 0.66, 0, 1));

                put(new Pair<Integer, Integer>(1, 2),
                        new CombatRoundResult(0.745, 0.255, 0, 2));

                put(new Pair<Integer, Integer>(2, 2),
                        new CombatRoundResult(0.448, 0.228, 0.324, 2));

                put(new Pair<Integer, Integer>(3, 2),
                        new CombatRoundResult(0.292, 0.372, 0.336, 2));

            }};


    public static CombatRoundResult lookup(int attackers, int defenders){
        attackers = Math.max(attackers, 3);
        defenders = Math.max(defenders, 2);

        return diceLookup.get(new Pair<Integer, Integer>(attackers, defenders));
    }

    public Pair<Integer, Integer> getCasualtiesInflicted(){
        int attackerCasualties = 0;
        int defenderCasualties = 0;

        double rand = rng.nextDouble();

        if(rand < attackerLoss){
            attackerCasualties = totalArmiesLost;
        } else if(rand < attackerLoss + defenderLoss) {
            defenderCasualties = totalArmiesLost;
        } else {
            attackerCasualties = 1;
            defenderCasualties = 1;
        }

        return new Pair<Integer, Integer>(attackerCasualties, defenderCasualties);
    }

    public final double attackerLoss;
    public final double defenderLoss;
    public final double bothLoss;
    public final int totalArmiesLost;

    public CombatRoundResult(double attackerLoss, double defenderLoss, double bothLoss, int totalArmiesLost) {
        this.attackerLoss = attackerLoss;
        this.defenderLoss = defenderLoss;
        this.bothLoss = bothLoss;
        this.totalArmiesLost = totalArmiesLost;
    }
}
