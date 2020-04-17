package com.sillysoft.lux.agent.utils;

import com.sillysoft.lux.agent.BigBadBot;

public class BigBadBotHelper {
    private static BigBadBotHelper helper;

    private BigBadBot botBase;

    private BigBadBotHelper(BigBadBot botBase){
        this.botBase = botBase;
    }

    public static void initInstance(BigBadBot botBase){
        helper = new BigBadBotHelper(botBase);
    }


    public static BigBadBotHelper getInstance(){
        return helper;
    }
}
