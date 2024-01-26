package com.github.fixpokebug.fixpokebug.util;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class EntityUtil {
    public static UUID getUUID(net.minecraft.entity.Entity entity){
        return entity.func_110124_au();
    }
    public static Entity getBukkitEntity(net.minecraft.entity.Entity entity){
        return Bukkit.getServer().getEntity(entity.func_110124_au());
    }

    public static net.minecraft.entity.Entity getForgeEntity(Entity entity){
        return (net.minecraft.entity.Entity)(Object)((CraftEntity) entity).getHandle();
    }
}
