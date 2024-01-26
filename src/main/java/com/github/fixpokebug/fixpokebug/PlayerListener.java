package com.github.fixpokebug.fixpokebug;

import com.github.fixpokebug.fixpokebug.util.EventHandlerUtil;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onForge(ForgeEvent event){
        if (event.getForgeEvent() instanceof PokeballImpactEvent){
            PokeballImpactEvent e = (PokeballImpactEvent) event.getForgeEvent();
            /*多玩家同时球丢错误触发对战*/
            EventHandlerUtil.PokeballImpactEvent.multiPlayerBattleErrorTrigger(e);
        }
        if (event.getForgeEvent() instanceof BattleStartedEvent){
            BattleStartedEvent e = (BattleStartedEvent) event.getForgeEvent();
            /*无技能对战错误触发*/
            EventHandlerUtil.BattleStartedEvent.noSkillBattleErrorTriggers(e);
            EventHandlerUtil.BattleStartedEvent.samePokeBattleErrorTrigger(e);
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        EventHandlerUtil.PlayerMoveEvent.unableToMoveDuringBattle(e);
    }
    @EventHandler
    public void onPlayerTp(PlayerTeleportEvent e){
        EventHandlerUtil.PlayerTeleportEvent.unableToTeleportDuringBattle(e);
    }
}
