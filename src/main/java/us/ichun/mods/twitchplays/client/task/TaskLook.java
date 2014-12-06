package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MathHelper;

public class TaskLook extends Task
{
    public int moveType; //FBLR;

    public float targetYaw;
    public float targetPitch;

    public float oriYaw;
    public float oriPitch;

    public TaskLook(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public void init()
    {
        while(player.rotationYaw < 360F)
        {
            player.prevRotationYaw += 360F;
            player.rotationYaw += 360F;
        }
        targetYaw = oriYaw = player.rotationYaw;
        targetPitch = oriPitch = player.rotationPitch;

        if(moveType <= 2)
        {
            player.rotationPitch += 90F;
            targetPitch = MathHelper.clamp_float(player.rotationPitch - ((player.rotationPitch + 11.25F) % 22.5F - 11.25F) + (moveType == 1 ? -22.5F : 22.5F) - 90F, -90F, 90F);
            player.rotationPitch -= 90F;
        }
        else if(moveType <= 4)
        {
            targetYaw = player.rotationYaw - ((player.rotationYaw + 45F) % 90F - 45F) + (moveType == 3 ? -90F : 90F);
        }
    }

    @Override
    public boolean parse(String... args)
    {
        if(args.length == 2)
        {
            moveType = (args[1].equals("up") || args[1].equals("u")) ? 1 : (args[1].equals("down") || args[1].equals("d")) ? 2 : (args[1].equals("left") || args[1].equals("l")) ? 3 : (args[1].equals("right") || args[1].equals("r")) ? 4 : 0;
            return moveType != 0;
        }
        return false;
    }

    @Override
    public int maxActiveTime()
    {
        return 10;
    }

    @Override
    protected void update()
    {
        player.rotationYaw += (targetYaw - oriYaw) * (1F / (float)maxActiveTime());
        player.rotationPitch += (targetPitch - oriPitch) * (1F / (float)maxActiveTime());
        player.rotationPitch = MathHelper.clamp_float(player.rotationPitch, -90F, 90F);
    }

    @Override
    public String getName()
    {
        return "look " + (moveType == 1 ? "up" : moveType == 2 ? "down" : moveType == 3 ? "left" : "right");
    }

    @Override
    public void terminate()
    {
        player.rotationYaw = targetYaw;
        player.rotationPitch = targetPitch;
    }
}
