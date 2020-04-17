package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.Board;

public abstract class AbstractMission {
    private double mlMissionWeight = 0;
    private Board board;

    public abstract MissionType getMissionType();

    // Get the chance based on current conditions to accept this mission
    public abstract double acceptMissionChance(int armies);

    // Place armies, returns number of unplaced armies, should usually be 0
    public abstract int placeArmies(int numArmies);

    // Execute the mission, returns true on mission success
    public abstract boolean executeMission();

    public void setMlMissionWeight(double mlMissionWeight) {
        this.mlMissionWeight = mlMissionWeight;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
