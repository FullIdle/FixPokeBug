package com.github.fixpokebug.fixpokebug;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main main;
    @Override
    public void onEnable() {
        main = this;
        reloadConfig();
        getServer().getPluginManager().registerEvents(new PlayerListener(), main);
    }

    @Override
    public void reloadConfig() {
        main.saveDefaultConfig();
        super.reloadConfig();
    }
}
