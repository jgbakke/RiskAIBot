package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.Board;
import com.sillysoft.lux.Country;
import com.sillysoft.lux.agent.BigBadBot;
import com.sillysoft.lux.util.BoardHelper;
import com.sillysoft.lux.util.ContinentIterator;

import java.util.ArrayList;

public class TakeContinent extends AbstractMission {

    public TakeContinent(Board board, BigBadBot bot) {
        super(board, bot);
    }

    @Override
    public MissionType getMissionType() {
        return MissionType.TAKE_CONTINENT;
    }

    @Override
    public double acceptMissionChance(int armies) {
        cachedMission = bot.takeContinentChance(armies);
        return cachedMission.utility * mlMissionWeight;
    }

    @Override
    public int placeArmies(int numArmies) {
        if(cachedMission == null){
            bot.debugMessage("CACHED MISSION IS NULL");
            acceptMissionChance(numArmies);
        }

        bot.debugMessage("STARTING PLACE ARMIES PHASE");

        bot.debugMessage("TakeContinent has been chosen: " + board.getContinentName(cachedMission.optimalID));

        bot.placeArmiesToTakeContinent(numArmies, cachedMission.optimalID);

        bot.debugMessage("PLACE ARMIES PHASE DONE");

        return 0;
    }

    @Override
    public boolean executeMission() {
        bot.debugMessage("TakeContinent is executing its mission");
        return bot.takeContinent(cachedMission.optimalID);
    }

    // Returns the number of countries in continent NOT owned by owner
    public static int[] unownedCountriesInContinent(int owner, int continent, Country[] countries){
        ArrayList<Integer> unownedCountries = new ArrayList<Integer>();

        ContinentIterator iter = new ContinentIterator( continent, countries );
        while (iter.hasNext()) {
            Country next = iter.next();
            if(next.getOwner() != owner) {
                unownedCountries.add(next.getCode());
            }
        }

        // Java 6 sucks
        // No streams yet :(
        int[] retval = new int[unownedCountries.size()];
        for (int i = 0; i < retval.length; i++) {
            retval[i] = unownedCountries.get(i);
        }

        return retval;
    }
}
