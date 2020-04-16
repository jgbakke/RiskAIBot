package com.sillysoft.lux.agent;

import com.sillysoft.lux.Board;
import com.sillysoft.lux.Country;
import com.sillysoft.lux.agent.utils.AbstractMission;
import com.sillysoft.lux.agent.utils.MissionManager;
import com.sillysoft.lux.agent.utils.MissionType;
import com.sillysoft.lux.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class BigBadBot extends SmartAgentBase {

    private MissionManager missionManager;

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

    private AbstractMission ChooseMission(){
        return missionManager.getOptimalMission();
    }

    @Override
    public void setPrefs(int newID, Board theboard){
        super.setPrefs(newID, theboard);
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
        board.placeArmies( numberOfArmies, placeOn);
    }

    @Override
    public void attackPhase() {

        System.out.println("BIG BOY BOT CALLED");
        CountryIterator armies = new ArmiesIterator( ID, 2, countries );
        if(armies.hasNext()){
            Country me = armies.next();

            Country[] adjacents = me.getAdjoiningList();
            for(Country c : adjacents){
                if(c.getOwner() != this.ID){
                    if(c.getArmies() < me.getArmies()){
                        board.attack(me, c, true);
                    }
                }
            }
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
}