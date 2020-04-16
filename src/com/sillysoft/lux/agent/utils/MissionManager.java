package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.Board;

import java.util.*;

public class MissionManager {
    private HashSet<AbstractMission> missions = new HashSet<AbstractMission>();

    public MissionManager(Board board){
        InitializeMissions(board);
    }

    private void InitializeMissions(Board board){
        missions.add(new TakeContinent());

        // Java 6 does not support lambdas :(
        for (AbstractMission mission : missions){
            mission.setBoard(board);
        }
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
