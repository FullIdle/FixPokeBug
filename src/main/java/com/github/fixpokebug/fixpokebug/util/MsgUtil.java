package com.github.fixpokebug.fixpokebug.util;

import com.github.fixpokebug.fixpokebug.Main;

public class MsgUtil {
    public static String thatPokemonIsAlreadyFighting;
    public static String skillIsEmpty;
    public static String unmovable;
    public static void init(){
        MsgUtil.thatPokemonIsAlreadyFighting = Main.main.getConfig().getString("ThatPokemonIsAlreadyFighting");
        MsgUtil.skillIsEmpty = Main.main.getConfig().getString("SkillIsEmpty");
        MsgUtil.unmovable = Main.main.getConfig().getString("Unmovable");
    }

    public static String getMsg(String msg){
        return msg.replace("&","§");
    }
}
