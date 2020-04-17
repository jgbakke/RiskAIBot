package com.sillysoft.lux.agent.utils.simulation;

public class PathSimulationResult {
    public final int countriesCaptured;
    public final int armiesLeft;
    public final boolean reachedContinent;

    public PathSimulationResult(int countriesCaptured, int armiesLeft, boolean reachedContinent) {
        this.countriesCaptured = countriesCaptured;
        this.armiesLeft = armiesLeft;
        this.reachedContinent = reachedContinent;
    }
}
