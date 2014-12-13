package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import us.ichun.mods.twitchplays.common.TwitchPlays;

public class TaskClearTasks extends Task
{
    public TaskClearTasks(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public void init()
    {
        if(!TwitchPlays.tickHandlerClient.tasks.isEmpty())
        {
            TwitchPlays.tickHandlerClient.tasks.get(0).terminate();
        }
        TwitchPlays.tickHandlerClient.tasks.clear();
    }

    @Override
    public boolean requiresOp(String...args)
    {
        return true;
    }

    @Override
    public boolean bypassOrder(String...args) { return true; };

    @Override
    public boolean parse(String... args)
    {
        return args.length == 1;
    }

    @Override
    public int maxActiveTime()
    {
        return 0;
    }

    @Override
    protected void update()
    {

    }

    @Override
    public boolean canWorkDead()
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "cleartasks";
    }
}
