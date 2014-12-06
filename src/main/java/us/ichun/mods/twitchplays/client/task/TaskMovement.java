package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class TaskMovement extends Task
{
    public int moveType; //FBLR;

    public double startX;
    public double startY;
    public double startZ;

    public TaskMovement(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public boolean parse(String... args)
    {
        if(args.length == 1)
        {
            moveType = (args[0].equals("forward") || args[0].equals("fwd") || args[0].equals("f") || args[0].equals("w")) ? 1 : (args[0].equals("back") || args[0].equals("bck") || args[0].equals("b") || args[0].equals("s")) ? 2 : (args[0].equals("left") || args[0].equals("l") || args[0].equals("a")) ? 3 : (args[0].equals("right") || args[0].equals("r") || args[0].equals("d")) ? 4 : 0;
            return moveType != 0;
        }
        return false;
    }

    @Override
    public void init()
    {
        startX = player.posX;
        startY = player.posY;
        startZ = player.posZ;
    }

    @Override
    public int maxActiveTime()
    {
        return 30;
    }

    @Override
    public void update()
    {
        if(player.rotationYaw < 0)
        {
            player.rotationYaw -= (player.rotationYaw - 45F) % 90F + 45F;
        }
        else
        {
            player.rotationYaw -= (player.rotationYaw + 45F) % 90F - 45F;
        }
        float moveFactor = 1.0F;
        double dist = player.getDistance(startX, player.posY, startZ);
        if(dist > 0.80D)
        {
            moveFactor = (float)((dist - 0.80D) / (1.0D - 0.80D));
        }

        if(dist > 0.975D)
        {
            timeActive = 300;
        }
        else
        {
            double motionY = player.motionY;
            player.moveEntityWithHeading(moveType == 3 ? moveFactor : moveType == 4 ? -moveFactor : 0.0F, moveType == 1 ? moveFactor : moveType == 2 ? -moveFactor : 0.0F);
            if(!(player.isCollidedHorizontally && player.isOnLadder()))
            {
                player.motionY = motionY;
            }
            else if(motionY > 0D)
            {
                timeActive--;
            }
            if(player.isCollidedHorizontally && !player.isJumping && player.onGround)
            {
                player.jump();
                player.motionY *= 0.8F;
            }
        }
    }

    @Override
    public String getName()
    {
        return (moveType == 1 ? "forward" : moveType == 2 ? "back" : moveType == 3 ? "left" : "right");
    }

    @Override
    public void terminate()
    {
        //TODO is riding...? movement jumps? sprint jumps?
        if(!player.isSneaking())
        {
            player.posX = Math.floor(player.posX) + 0.5D;
            player.posZ = Math.floor(player.posZ) + 0.5D;
            player.setPosition(player.posX, player.posY, player.posZ);
            if(!player.onGround)
            {
                player.motionX = 0.0D;
                player.motionZ = 0.0D;
            }
//            player.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        }
    }
}
