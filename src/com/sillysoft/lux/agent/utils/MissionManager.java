package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.Board;
import com.sillysoft.lux.agent.BigBadBot;

import java.util.*;

public class MissionManager {
    private HashSet<AbstractMission> missions = new HashSet<AbstractMission>();

    private BigBadBot bot;

    public MissionManager(Board board, BigBadBot bot){
        this.bot = bot;
        InitializeMissions(board);
    }

    private void InitializeMissions(Board board){
        missions.add(new TakeContinent(board, this.bot));
    }

    public void clearMissions(){
        for (AbstractMission mission : missions) {
            mission.clearMission();
        }
    }

    public AbstractMission getOptimalMission(final int armies){
        return Collections.max(missions, new Comparator<AbstractMission>() {
            @Override
            public int compare(AbstractMission a, AbstractMission b) {
                return Double.compare(a.acceptMissionChance(armies), b.acceptMissionChance(armies));
            }
        });
    }
}
