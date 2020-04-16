package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.Board;

public class TakeContinent extends AbstractMission {

    public TakeContinent(MissionType missionType, Board b) {
        super(missionType, b);
    }

    @Override
    public double acceptMissionChance() {
        return 0;
    }

    @Override
    public boolean executeMission() {
        return false;
    }
}
