package com.sillysoft.lux.agent.utils.simulation;

public class SimulationResult {
    // True if this simulation is expected to be a victroy
    public final boolean victory;

    // The average number of attacker armies, averaged from every simulation
    public final double averageArmiesRemaining;

    // The percent of simulations that resulted in a win
    public final double percentChanceAnyWin;

    public SimulationResult(boolean victory, double averageArmiesRemaining, double percentChanceAnyWin) {
        this.victory = victory;
        this.averageArmiesRemaining = averageArmiesRemaining;
        this.percentChanceAnyWin = percentChanceAnyWin;
    }
}
