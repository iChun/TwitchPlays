package us.ichun.mods.twitchplays.client.task;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class Task
{
    public int timeActive;

    public WorldClient world;
    public EntityPlayerSP player;

    public Task(WorldClient world, EntityPlayerSP player)
    {
        this.world = world;
        this.player = player;
    }

    public abstract boolean parse(String...args); // this includes the first arg as the command name. return false if arg count isn't something generally accepted and then the task won't be created.

    public void init(){} //called when the task finally starts ticking.

    public void terminate(){} //called when the task ends.

    public boolean canBeAdded(ImmutableList<Task> tasks, int timeSinceLastTriggered) //You have to check both tasks list if it hasn't been called yet, and time since last called (can be negative if world clock was changed). Integer.MAX_VALUE if it has never been called.
    {
        return true;
    }

    public boolean requiresOp(String...args)
    {
        return false;
    }

    public boolean bypassOrder(String...args) { return false; }

    public abstract int maxActiveTime();

    protected abstract void update();

    public abstract String getName();

    public void tick()
    {
        update();
        timeActive++;
    }
}
