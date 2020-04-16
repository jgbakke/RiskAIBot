package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.Board;

public class TakeContinent extends AbstractMission {

    @Override
    public MissionType getMissionType() {
        return MissionType.TAKE_CONTINENT;
    }

    @Override
    public double acceptMissionChance() {
        return 0;
    }

    @Override
    public int placeArmies(int numArmies) {
        return numArmies;
    }

    @Override
    public boolean executeMission() {
        return false;
    }
}
