package com.github.fixpokebug.fixpokebug.util;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.entity.Player;

public class PixelmonUtil {
    public static PlayerPartyStorage getPlayerPartyStorage(Player player){
        return Pixelmon.storageManager.getParty(player.getUniqueId());
    }
    public static PlayerPartyStorage getPlayerPartyStorage(EntityPlayer player){
        return Pixelmon.storageManager.getParty(player.func_110124_au());
    }
}
