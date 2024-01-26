package com.github.fixpokebug.fixpokebug.util;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

import java.util.ArrayList;
import java.util.UUID;

import static com.github.fixpokebug.fixpokebug.Main.main;

public class EventHandlerUtil {
    public static class PokeballImpactEvent{
        public static void multiPlayerBattleErrorTrigger(com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent e){
            /*判断是否是空球，也就是球有没有精灵,R和右键出去的区别*/
            /*判断是否砸中生物,且属于宝可梦*/
            if (e.isEmptyBall || (e.getEntityHit()) == null || !(e.getEntityHit() instanceof EntityPixelmon)) {return;}
            EntityPixelmon ep = (EntityPixelmon) e.getEntityHit();
            EntityLivingBase thrower = e.pokeball.field_70192_c;
            if (!(thrower instanceof EntityPlayer))return;
            Player p = PlayerUtil.getBukkitPlayer(((EntityPlayer) thrower));
            /*在主线程内执行*/
            Bukkit.getScheduler().runTask(main,()->{
                BattleControllerBase bc = ep.battleController;
                /*判断是否有对战*/
                if (bc == null) {
                    return;
                }
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
                BattleControllerBase battle = BattleRegistry.getBattle(PlayerUtil.getEntityPlayer(p));
                if (battle == null) {return;}
                /*对局存在,获取他对局中的对手是否是投掷到的对象,是就结束并提示*/
                PlayerParticipant par = battle.getPlayer(p.getName());
                if (!par.player.getBukkitEntity().getUniqueId().equals(p.getUniqueId())) {return;}
                for (PixelmonWrapper wrapper : par.getOpponentPokemon()) {
                    if (wrapper.pokemon.getUUID().equals(ep.getBukkitEntity().getUniqueId())) {
                        p.sendMessage(main.getConfig().getString("ThatPokemonIsAlreadyFighting").replace("&","§"));
                        battle.endBattle();
                    }
                }
            });
        }
    }
    public static class BattleStartedEvent{
        private static final ArrayList<EntityPixelmon> alreadyInBattleEntity = new ArrayList<>();
        private static final ArrayList<UUID> alreadyInBattleUUID = new ArrayList<>();

        public static void noSkillBattleErrorTriggers(com.pixelmonmod.pixelmon.api.events.BattleStartedEvent e){
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
        public static void samePokeBattleErrorTrigger(com.pixelmonmod.pixelmon.api.events.BattleStartedEvent e){
            BattleControllerBase bc = e.bc;
            if (bc.playerNumber < 1)return;
            for (BattleParticipant participant : bc.participants) {
                if (!(participant instanceof WildPixelmonParticipant)) {
                    continue;
                }
                WildPixelmonParticipant wild = (WildPixelmonParticipant) participant;
                if (wild.getEntity() == null) {
                    bc.endBattle(EnumBattleEndCause.FORCE);
                }
            }
        }
    }
    public static class PlayerMoveEvent{
        public static void unableToMoveDuringBattle(org.bukkit.event.player.PlayerMoveEvent e){
            interceptAllMoveInBattle(e);
        }
    }


    private static void interceptAllMoveInBattle(PlayerEvent event){
        if (!(event instanceof Cancellable)){return;}
        Player player = event.getPlayer();
        EntityPlayer ep = PlayerUtil.getEntityPlayer(player);
        BattleControllerBase bc = BattleRegistry.getBattle(ep);
        if (bc == null){return;}
        String unmovable = main.getConfig().getString("Unmovable").replace("&", "§");
        player.sendMessage(unmovable);
        ((Cancellable) event).setCancelled(true);
    }

    public static class PlayerTeleportEvent {
        public static void unableToTeleportDuringBattle(org.bukkit.event.player.PlayerTeleportEvent e) {
            interceptAllMoveInBattle(e);
        }
    }
}
