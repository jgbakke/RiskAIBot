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
        bot.debugMessage("Cached mission is: " + cachedMission.toString());
        return cachedMission.utility * mlMissionWeight;
    }

    @Override
    public int placeArmies(int numArmies) {
        if(cachedMission == null){
            bot.debugMessage("CACHED MISSION IS NULL");
            acceptMissionChance(numArmies);

            if(cachedMission == null){
                bot.debugMessage("WHY IS IT STILL NULL");
            }
        }

        bot.debugMessage("Place Armies called");
        bot.debugMessage("TakeContinent has been chosen: " + board.getContinentName(cachedMission.optimalID));
        bot.debugMessage(String.format("Utility is %f", cachedMission.utility));

        bot.placeArmiesToTakeContinent(numArmies, cachedMission.optimalID);

        return numArmies;
    }

    @Override
    public boolean executeMission() {
        bot.debugMessage("TakeContinent is executing its mission");

        if(bot.takeContinent(cachedMission.optimalID)){
            bot.debugMessage("Bot took continent");
            return true;
        } else {
            // TODO: If continent not caputed then make it captured
            bot.debugMessage("Did not take continent");
        }

        return false;
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
