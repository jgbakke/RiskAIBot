package com.sillysoft.lux.agent.utils.simulation;

public class MonteCarloMain {
    public static void main(String[] args){
        MonteCarloSimulator simulator = new MonteCarloSimulator();

        long start = System.nanoTime();
        SimulationResult res = simulator.simulateBattle(14, 6, 1000);
        long finish = System.nanoTime();

        System.out.println(String.format("Time to execution: %f", (finish - start) / Math.pow(10, 9)));
        System.out.println(res.victory ? String.format("Probably win with %d armies remaining", res.averageArmiesRemaining): "Probably lost");
    }
}
