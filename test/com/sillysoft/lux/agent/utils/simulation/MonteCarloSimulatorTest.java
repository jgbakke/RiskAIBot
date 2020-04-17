package com.sillysoft.lux.agent.utils.simulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class MonteCarloSimulatorTest {

    public static final double WIN_DELTA = 0.03;
    public static final double ARMIES_DELTA = 1;
    private final int SIMULATIONS = 3000;

    private MonteCarloSimulator simulator = new MonteCarloSimulator();

    @Test
    void simulateBattle() {
        Assertions.assertEquals(0.76, simulator.simulateBattle(7, 5, SIMULATIONS).percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0.73, simulator.simulateBattle(10, 8, SIMULATIONS).percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0.9, simulator.simulateBattle(22, 15, SIMULATIONS).percentChanceAnyWin,  WIN_DELTA);
        Assertions.assertEquals(1, simulator.simulateBattle(36, 11, SIMULATIONS).percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0.94, simulator.simulateBattle(9, 4, SIMULATIONS).percentChanceAnyWin, WIN_DELTA);
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

        res = simulator.simulateBattle(9, 10, SIMULATIONS);
        Assertions.assertFalse(res.victory);
        Assertions.assertEquals(0.46, res.percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0, res.averageArmiesRemaining, ARMIES_DELTA);

        res = simulator.simulateBattle(11, 14, SIMULATIONS);
        Assertions.assertFalse(res.victory);
        Assertions.assertEquals(0.44, res.percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0, res.averageArmiesRemaining, ARMIES_DELTA);

        res = simulator.simulateBattle(1, 1, SIMULATIONS);
        Assertions.assertFalse(res.victory);
        Assertions.assertEquals(0.41, res.percentChanceAnyWin, WIN_DELTA);
        Assertions.assertEquals(0, res.averageArmiesRemaining, ARMIES_DELTA);
    }
}