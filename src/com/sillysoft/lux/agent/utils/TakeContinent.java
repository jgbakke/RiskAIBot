package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.Country;
import com.sillysoft.lux.agent.BigBadBot;
import com.sillysoft.lux.util.ContinentIterator;

import java.util.ArrayList;

public class TakeContinent extends AbstractMission {

    @Override
    public MissionType getMissionType() {
        return MissionType.TAKE_CONTINENT;
    }

    @Override
    public double acceptMissionChance(int armies) {
        BigBadBot bot = BigBadBot.getBotReference();


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

    // Returns the number of countries in continent NOT owned by owner
    public static int[] unownedCountriesInContinent(int owner, int continent, Country[] countries){
        ArrayList<Integer> unownedCountries = new ArrayList<Integer>();

        ContinentIterator iter = new ContinentIterator( continent, countries );
        while (iter.hasNext()) {
            Country next = iter.next();
            if(iter.next().getOwner() != owner) {
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
