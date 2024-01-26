package com.github.fixpokebug.fixpokebug;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static com.github.fixpokebug.fixpokebug.Main.main;

public class PlayerListener implements Listener {
    @EventHandler
    public void onForge(ForgeEvent event){
        /*<-碰撞的事件(修复多人同时对战同一个野生宝可梦)->*/
        if (event.getForgeEvent() instanceof PokeballImpactEvent){
            PokeballImpactEvent e = (PokeballImpactEvent) event.getForgeEvent();
            /*判断是否是空球，也就是球有没有精灵,R和右键出去的区别*/
            if (!e.isEmptyBall) {
                /*判断是否砸中生物,且属于宝可梦*/
                if ((e.getEntityHit()) == null || !(e.getEntityHit() instanceof EntityPixelmon)) {return;}
                EntityPixelmon ep = (EntityPixelmon) e.getEntityHit();
                Player p = (Player) e.pokeball.field_70192_c.getBukkitEntity();
                PlayerPartyStorage pps = Pixelmon.storageManager.getParty(p.getUniqueId());
                /*在主线程内执行*/
                Bukkit.getScheduler().runTask(main,()->{
                    BattleControllerBase bc = ep.battleController;
                    /*判断是否有对战*/
                    if (bc != null) {
                        if (p.getPlayer() == null) {
                            return;
                        }
                        /*判断这个对战是否有投掷者,有就不管了(被判断为了第一个进行对战的了)*/
                        for (PlayerParticipant player : bc.getPlayers()) {
                            if (player.player.getBukkitEntity().getUniqueId().equals(p.getUniqueId())) {
                                return;
                            }
                        }
                        /*如果没有投掷者,就获取投掷者的对局,对局不存在就不管*/
                        BattleControllerBase battle = BattleRegistry.getBattle((EntityPlayer) ((Object) ((CraftPlayer) p).getHandle()));
                        if (battle == null) {
                            return;
                        }
                        /*对局存在,获取他对局中的对手是否是投掷到的对象,是就结束并提示*/
                        PlayerParticipant par = battle.getPlayer(p.getName());
                        if (par.player.getBukkitEntity().getUniqueId().equals(p.getUniqueId())) {
                            for (PixelmonWrapper wrapper : par.getOpponentPokemon()) {
                                if (wrapper.pokemon.getUUID().equals(ep.getBukkitEntity().getUniqueId())) {
                                    p.sendMessage(main.getConfig().getString("ThatPokemonIsAlreadyFighting").replace("&","§"));
                                    battle.endBattle();
                                }
                            }
                        }
                    }
                });
            }
        }
        /*<--对战开始事件(修复了空技能的情况)-->*/
        if (event.getForgeEvent() instanceof BattleStartedEvent){
            BattleStartedEvent e = (BattleStartedEvent) event.getForgeEvent();
            for (BattleParticipant bp : e.bc.participants) {
                for (PixelmonWrapper pw : bp.allPokemon) {
                    if (pw.getMoveset().isEmpty()) {
                        pw.pokemon.rerollMoveset();
                        pw.setTemporaryMoveset(pw.pokemon.getMoveset());
                        Bukkit.getPlayer(pw.getPlayerOwner().func_110124_au()).sendMessage(main.getConfig().getString("SkillIsEmpty").replace("&","§"));
                    }
                }
            }
        }
    }
}
