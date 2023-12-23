package com.github.fullidle.boredplugin.fifix;

import com.pixelmonmod.pixelmon.Pixelmon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main main;
    @Override
    public void onEnable() {
        main = this;
        main.saveDefaultConfig();
        main.getServer().getPluginManager().registerEvents(new PlayerListener(), main);
    }

}
