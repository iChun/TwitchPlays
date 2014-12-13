package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class TaskJump extends Task
{
    public TaskJump(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public boolean parse(String... args)
    {
        return args.length == 1;
    }

    @Override
    public int maxActiveTime()
    {
        return player.isInWater() && player.isEntityAlive() && timeActive < (20 * 60) ? timeActive + 2 : 10;
    }

    @Override
    protected void update()
    {
        if(!player.isJumping && player.onGround)
        {
            player.jump();
        }
        if (player.isInWater() || player.handleLavaMovement())
        {
            player.motionY += 0.03999999910593033D;
        }
    }

    @Override
    public String getName()
    {
        return "jump";
    }
}
