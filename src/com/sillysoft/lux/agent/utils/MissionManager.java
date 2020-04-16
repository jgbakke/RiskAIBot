package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.Board;

import java.util.*;

public class MissionManager {
    private HashSet<AbstractMission> missions = new HashSet<AbstractMission>();

    public MissionManager(Board board){
        missions.add(new TakeContinent(MissionType.TAKE_CONTINENT, board));
    }

    public AbstractMission getOptimalMission(){
        return Collections.max(missions, new Comparator<AbstractMission>() {
            @Override
            public int compare(AbstractMission a, AbstractMission b) {
                return Double.compare(a.acceptMissionChance(), b.acceptMissionChance());
            }
        });
    }
}
