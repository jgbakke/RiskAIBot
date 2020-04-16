package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.Board;

public abstract class AbstractMission {
    public final MissionType missionType;
    private double mlMissionWeight = 0;

    private Board board;

    protected AbstractMission(MissionType missionType, Board b) {
        this.missionType = missionType;
        this.board = b;
    }

    // Get the chance based on current conditions to accept this mission
    public abstract double acceptMissionChance();

    // Execute the mission
    public abstract boolean executeMission();

    public void setMlMissionWeight(double mlMissionWeight) {
        this.mlMissionWeight = mlMissionWeight;
    }
}
