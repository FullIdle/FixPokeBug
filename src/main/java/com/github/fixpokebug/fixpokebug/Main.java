package com.github.fixpokebug.fixpokebug;

import com.github.fixpokebug.fixpokebug.util.MsgUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main main;
    @Override
    public void onEnable() {
        main = this;
        reloadConfig();
        getServer().getPluginManager().registerEvents(new PlayerListener(), main);
        getLogger().info("§a插件已启用!--§3更多bug可请评论于:§a https://bbs.mc9y.net/resources/720/");
    }

    @Override
    public void reloadConfig() {
        main.saveDefaultConfig();
        super.reloadConfig();
        MsgUtil.init();
    }
}
