package net.gamerservices.npclibfork;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.gamerservices.npcx.myNPC;
import net.minecraft.server.Entity;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.CreatureType;


public class NpcSpawner {

    protected static WorldServer GetWorldServer(World world) {
        try {
            CraftWorld w = (CraftWorld) world;
            Field f;
            f = CraftWorld.class.getDeclaredField("world");

            f.setAccessible(true);
            return (WorldServer) f.get(w);

        } catch (Exception e) {
           e.printStackTrace();
        }

        return null;
    }
    private static MinecraftServer GetMinecraftServer(Server server) {

        if (server instanceof CraftServer) {
            CraftServer cs = (CraftServer) server;
            Field f;
            try {
                f = CraftServer.class.getDeclaredField("console");
            } catch (NoSuchFieldException ex) {
                return null;
            } catch (SecurityException ex) {
                return null;
            }
            MinecraftServer ms;
            try {
                f.setAccessible(true);
                ms = (MinecraftServer) f.get(cs);
            } catch (IllegalArgumentException ex) {
                return null;
            } catch (IllegalAccessException ex) {
                return null;
            }
            return ms;
        }
        return null;
    }

    public static BasicHumanNpc SpawnBasicHumanNpc(myNPC parent, String uniqueId, String name, World world, double x, double y, double z, double yaw, double pitch) {
        try {
            WorldServer ws = GetWorldServer(world);
            MinecraftServer ms = GetMinecraftServer(ws.getServer());

            CHumanNpc eh = new CHumanNpc(ms, ws, name, new ItemInWorldManager(ws));
            eh.forceSetName(name);
            Float yaw2 = new Float(yaw);
            Float pitch2 = new Float(pitch);
            
            eh.setLocation(x, y, z, yaw2.floatValue(), pitch2.floatValue());

            int m = MathHelper.floor(eh.locX / 16.0D);
            int n = MathHelper.floor(eh.locZ / 16.0D);

            ws.getChunkAt(m, n).a(eh);
            
            // TODO here eclipse says :
            // TODO Type safety: The method add(Object) belongs to the raw type List. References to generic type list<E> should be parameterized
            // TODO tickleman does not know what to do with this !
            ws.entityList.add(eh);

            //ws.b(eh);
            // TODO here eclipse says :
            // TODO Class is a raw type. References to generic type Class<T> should be parameterized
            // TODO tickleman does not know what to do with this !
            Class params[] = new Class[1];
            params[0] = Entity.class;

            Method method;
            method = net.minecraft.server.World.class.getDeclaredMethod("c", params);
            method.setAccessible(true);
            Object margs[] = new Object[1];
            margs[0] = eh;
            method.invoke(ws, margs);


            return new BasicHumanNpc(parent, eh, uniqueId, name, x,y,z, yaw2, pitch2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void RemoveBasicHumanNpc(BasicHumanNpc npc) {
        try {
            npc.getMCEntity().world.removeEntity(npc.getMCEntity());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static LivingEntity SpawnMob(CreatureType type, World world, double x, double y, double z) {
    	return world.spawnCreature(new org.bukkit.Location(world, x, y, z), type);
    }

}
