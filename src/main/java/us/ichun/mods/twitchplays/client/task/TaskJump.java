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
        return 1;
    }

    @Override
    protected void update()
    {
        if(!player.isJumping && player.onGround)
        {
            player.jump();
            player.motionY *= 0.8F;
        }
    }
}
