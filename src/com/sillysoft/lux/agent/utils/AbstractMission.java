package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.Board;
import com.sillysoft.lux.agent.BigBadBot;

public abstract class AbstractMission {
    protected double mlMissionWeight = 1;
    protected Board board;
    protected BigBadBot bot;

    public AbstractMission(Board board, BigBadBot bot){
        this.board = board;
        this.bot = bot;
    }

    protected MissionBenefit cachedMission;

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

    public void clearMission(){
        cachedMission = null;
    }
}
