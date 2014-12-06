package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class TaskSwim extends Task
{
    public int moveType; //FBLR;

    public double startX;
    public double startY;
    public double startZ;

    public boolean hitLand;
    public int hitLandTimeout;

    public TaskSwim(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public boolean parse(String... args)
    {
        if(args.length == 1)
        {
            moveType = 1;
            return true;
        }
        if(args.length == 2)
        {
            moveType = (args[1].equals("forward") || args[1].equals("fwd") || args[1].equals("f") || args[1].equals("w")) ? 1 : (args[1].equals("back") || args[1].equals("bck") || args[1].equals("b") || args[1].equals("s")) ? 2 : (args[1].equals("left") || args[1].equals("l") || args[1].equals("a")) ? 3 : (args[1].equals("right") || args[1].equals("r") || args[1].equals("d")) ? 4 : 0;
            return moveType != 0;
        }
        return false;
    }

    @Override
    public void init()
    {
        if(!player.isInWater())
        {
            timeActive = 10000;
        }
        startX = player.posX;
        startY = player.posY;
        startZ = player.posZ;
    }

    @Override
    public int maxActiveTime()
    {
        return (player.isInWater() || hitLand || hitLandTimeout > 0) && player.isEntityAlive() && timeActive < (20 * 60) ? timeActive + 2 : 10;
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
        if(hitLand && !player.isInWater())
        {
            hitLand = false;
            player.jump();
            player.motionY *= 0.8F;
        }
        if(!hitLand)
        {
            hitLandTimeout--;

            if(hitLandTimeout > 0)
            {
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
                }
            }
        }
        if (player.isInWater() || player.handleLavaMovement())
        {
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
                if(player.isCollidedHorizontally)
                {
                    hitLand = true;
                    hitLandTimeout = 10;
                }
            }
            player.motionY += 0.03999999910593033D;
        }
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