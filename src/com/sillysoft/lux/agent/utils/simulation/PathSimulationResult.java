package com.sillysoft.lux.agent.utils.simulation;

public class PathSimulationResult {
    public final double countriesCaptured;
    public final double armiesLeft;
    public final boolean reachedContinent;

    public PathSimulationResult(double countriesCaptured, double armiesLeft, boolean reachedContinent) {
        this.countriesCaptured = countriesCaptured;
        this.armiesLeft = armiesLeft;
        this.reachedContinent = reachedContinent;
    }
}
