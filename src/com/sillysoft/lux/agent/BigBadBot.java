package com.sillysoft.lux.agent;

import com.sillysoft.lux.Board;
import com.sillysoft.lux.Country;
import com.sillysoft.lux.agent.utils.AbstractMission;
import com.sillysoft.lux.agent.utils.MissionBenefit;
import com.sillysoft.lux.agent.utils.MissionManager;
import com.sillysoft.lux.agent.utils.TakeContinent;
import com.sillysoft.lux.agent.utils.simulation.MonteCarloSimulator;
import com.sillysoft.lux.agent.utils.simulation.PathSimulationResult;
import com.sillysoft.lux.agent.utils.simulation.SimulationResult;
import com.sillysoft.lux.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;


public class BigBadBot extends SmartAgentBase {

    private static BigBadBot botReference;

    public static BigBadBot getBotReference() {
        return botReference;
    }

    private final boolean DEBUG_MODE = true;

    private MissionManager missionManager;

    private MonteCarloSimulator simulator = new MonteCarloSimulator();

    private AbstractMission currentMission;

    //region Not allowed to use a Helper Class so have to do them here
    public static String httpRequest(String json) throws IOException{
        URL url = new URL("http://localhost:8080/request-params");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        try {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        } finally {
            os.close();
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8")
        );

        try {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } finally {
            br.close();
        }
    }
    //endregion

    private AbstractMission chooseMission(int armiesAvailable){
        return missionManager.getOptimalMission(armiesAvailable);
    }

    @Override
    public void setPrefs(int newID, Board theboard){
        super.setPrefs(newID, theboard);
        botReference = this;
        missionManager = new MissionManager(theboard);
    }

    @Override
    public String name() { return "Big Bad Bot"; }

    @Override
    public float version() {
        return 0.1f;
    }

    @Override
    public String description() {
        return "This is a bot.";
    }

    @Override
    public int pickCountry() {
        int goalCont = BoardHelper.getSmallestPositiveEmptyCont(countries, board);

        if (goalCont == -1) // oops, there are no unowned conts
            goalCont = BoardHelper.getSmallestPositiveOpenCont(countries, board);

        // So now pick a country in the desired continent
        return pickCountryInContinent(goalCont);
    }

    @Override
    public void placeArmies(int numberOfArmies){
        currentMission = chooseMission(numberOfArmies);

        debugMessage("My mission is: " + currentMission.getMissionType().name());

        int remainingArmies = currentMission.placeArmies(numberOfArmies);

        if(remainingArmies > 0){
            debugMessage(String.format("I did not place %d armies", remainingArmies));
            defaultArmyPlacement(remainingArmies);
        }

    }

    @Override
    public void attackPhase() {
        if(currentMission.executeMission()){
            debugMessage("I captured at least one country");
        } else {
            debugMessage("I did not capture anything");
        }
    }

    @Override
    public int moveArmiesIn(int cca, int ccd) {
        return countries[cca].getArmies()-1;
    }

    @Override
    public void fortifyPhase() {

    }

    @Override
    public String youWon() {
        return "I won haha";
    }

    private void defaultArmyPlacement(int numArmies){
        int mostEnemies = -1;
        Country placeOn = null;
        int subTotalEnemies = 0;
        CountryIterator neighbors = null;

        // Use a PlayerIterator to cycle through all the countries that we own.
        CountryIterator own = new PlayerIterator( ID, countries );
        while (own.hasNext())
        {
            Country us = own.next();
            subTotalEnemies = us.getNumberEnemyNeighbors();

            // If it's the worst so far store it
            if ( subTotalEnemies > mostEnemies )
            {
                mostEnemies = subTotalEnemies;
                placeOn = us;
            }
        }

        // So now placeOn is the country that we own with the most enemies.
        // Tell the board to place all of our armies there
        board.placeArmies( numArmies, placeOn);
    }

    public void debugMessage(String s){
        if(DEBUG_MODE){
            board.sendChat(s);
        }
    }

    public PathSimulationResult simulatePath(int[] path, int reinforcements){
        if(path == null){
            return new PathSimulationResult(-1, -1, false);
        }

        Country[] countriesInRoute = new Country[path.length];
        for (int i = 0; i < path.length; i++) {
            countriesInRoute[i] = countries[path[i]];
        }

        return simulator.simulatePathResults(countriesInRoute, reinforcements + countriesInRoute[0].getArmies(), 100);
    }

    public PathSimulationResult simulateEasiestPathToContinent(int cont, int reinforcements){
        int[] routeToCont = BoardHelper.cheapestRouteFromOwnerToCont(ID, cont, countries);
        return simulatePath(routeToCont, reinforcements);
    }

    // Go through every continent
    // Find how many countries we can take over if we go straight for a continent and
    // what chance we have to take the continent
    public MissionBenefit MissionBenefit(int reinforcements) {
        // Normally would override from SmartAgentBase but they do not accept a param
        // and cannot change that class (can't rebuild it)


        MissionBenefit optimalContinent = new MissionBenefit(-1, -1);

        for (int cont = 0; cont < numContinents; cont++) {
            double utility = 0;

            int enemies = BoardHelper.getEnemyArmiesInContinent( ID, cont, countries );

            PathSimulationResult pathToContinent = simulateEasiestPathToContinent(cont, reinforcements);
            int countriesGained = pathToContinent.countriesCaptured;

            if(pathToContinent.reachedContinent) {
                // Now that we reached the continent we also need to Monte Carlo simulate this
                int ours = pathToContinent.armiesLeft;

                int[] pathOnContinent = TakeContinent.unownedCountriesInContinent(ID, cont, countries);

                PathSimulationResult continentBattle = simulatePath(pathOnContinent, pathToContinent.armiesLeft);
                countriesGained += continentBattle.countriesCaptured;

                if (continentBattle.reachedContinent) {
                    countriesGained += board.getContinentBonus(cont);
                }
            }

            utility += countriesGained / 3.0;

            if(utility > optimalContinent.utility){
                optimalContinent = new MissionBenefit(cont, utility);
            }

        }

        return optimalContinent;
    }
}