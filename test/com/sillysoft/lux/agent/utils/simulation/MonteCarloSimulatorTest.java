package com.sillysoft.lux.agent.utils.simulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class MonteCarloSimulatorTest {

    public static final double WIN_DELTA = 0.02;
    public static final double ARMIES_DELTA = 1;
    private final int SIMULATIONS = 3000;

    private MonteCarloSimulator simulator = new MonteCarloSimulator();

    @Test
    void simulateBattle() {
        Assertions.assertEquals(0.74, simulator.simulateBattle(7, 5, SIMULATIONS).percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0.72, simulator.simulateBattle(10, 8, SIMULATIONS).percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0.91, simulator.simulateBattle(22, 15, SIMULATIONS).percentChanceAnyWin,  WIN_DELTA);
        Assertions.assertEquals(1, simulator.simulateBattle(36, 11, SIMULATIONS).percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0.93, simulator.simulateBattle(9, 4, SIMULATIONS).percentChanceAnyWin, WIN_DELTA);
    }

    @Test
    void simulateBattleRemainingArmies() {
        Assertions.assertEquals(3, simulator.simulateBattle(7, 5, SIMULATIONS).averageArmiesRemaining, ARMIES_DELTA);
        Assertions.assertEquals(4, simulator.simulateBattle(10, 8, SIMULATIONS).averageArmiesRemaining, ARMIES_DELTA);
        Assertions.assertEquals(9, simulator.simulateBattle(22, 15, SIMULATIONS).averageArmiesRemaining,  ARMIES_DELTA);
        Assertions.assertEquals(27, simulator.simulateBattle(36, 11, SIMULATIONS).averageArmiesRemaining, ARMIES_DELTA);
        Assertions.assertEquals(6, simulator.simulateBattle(9, 4, SIMULATIONS).averageArmiesRemaining, ARMIES_DELTA);
    }

    @Test
    void simulateBattleLoss() {
        // TODO: Investigate this
        SimulationResult res = simulator.simulateBattle(1, 3, SIMULATIONS);
        Assertions.assertFalse(res.victory);
        Assertions.assertEquals(0.03, res.percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0, res.averageArmiesRemaining, ARMIES_DELTA);

        res = simulator.simulateBattle(2, 6, SIMULATIONS);
        Assertions.assertFalse(res.victory);
        Assertions.assertEquals(0.02, res.percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0, res.averageArmiesRemaining, ARMIES_DELTA);

        res = simulator.simulateBattle(9, 15, SIMULATIONS);
        Assertions.assertFalse(res.victory);
        Assertions.assertEquals(0.17, res.percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0, res.averageArmiesRemaining, ARMIES_DELTA);

        res = simulator.simulateBattle(11, 24, SIMULATIONS);
        Assertions.assertFalse(res.victory);
        Assertions.assertEquals(0.03, res.percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0, res.averageArmiesRemaining, ARMIES_DELTA);

        res = simulator.simulateBattle(1, 1, SIMULATIONS);
        Assertions.assertFalse(res.victory);
        Assertions.assertEquals(0.41, res.percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0, res.averageArmiesRemaining, ARMIES_DELTA);
    }
}