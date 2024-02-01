package com.github.fixpokebug.fixpokebug;

import com.github.fixpokebug.fixpokebug.util.EventHandlerUtil;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onForge(ForgeEvent event){
        if (event.getForgeEvent() instanceof PokeballImpactEvent){
            PokeballImpactEvent e = (PokeballImpactEvent) event.getForgeEvent();
            /*多玩家同时球丢错误触发对战*/
            EventHandlerUtil.PokeballImpactEvent.multiPlayerBattleErrorTrigger(e);
            if (e.isCanceled()) return;
            /*拦截具有相同 Uuid 的匹配(实体的uuid和宝可梦数据的uuid都判断)*/
            EventHandlerUtil.PokeballImpactEvent.interceptMatchesWithTheSameUuid(e);
        }
        if (event.getForgeEvent() instanceof BattleStartedEvent){
            BattleStartedEvent e = (BattleStartedEvent) event.getForgeEvent();
            /*无技能对战错误触发*/
            EventHandlerUtil.BattleStartedEvent.noSkillBattleErrorTriggers(e);
            if (e.isCanceled()) return;
            /*拦截具有相同 Uuid 的匹配(实体的uuid和宝可梦数据的uuid都判断)(配合PokeballImpactEvent内的同名方法)*/
            EventHandlerUtil.BattleStartedEvent.interceptMatchesWithTheSameUuid(e);
        }
        if (event.getForgeEvent() instanceof BattleEndEvent){
            BattleEndEvent e = (BattleEndEvent) event.getForgeEvent();
            /*拦截具有相同 Uuid 的匹配(实体的uuid和宝可梦数据的uuid都判断)-->需要和对战开始配合使用的*/
            EventHandlerUtil.BattleEndEvent.interceptMatchesWithTheSameUuid(e);
        }
    }
    @EventHandler
    public void onPlayerTp(PlayerTeleportEvent e){
        /*玩家不可在对战中传送*/
        EventHandlerUtil.PlayerTeleportEvent.unableToTeleportDuringBattle(e);
    }
}
