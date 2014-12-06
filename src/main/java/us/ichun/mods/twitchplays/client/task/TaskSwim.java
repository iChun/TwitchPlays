package us.ichun.mods.twitchplays.client.task;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
        hitLandTimeout = -1;
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
            if(args[1].equals("dammit") && player.isInWater())
            {
                moveType = 1;
                if(world.rand.nextFloat() < 0.05F)//1 in 20 chance
                {
                    player.motionY += 3D;
                }
            }
            return moveType != 0;
        }
        return false;
    }

    @Override
    public void init()
    {
        Block block = world.getBlock((int)Math.floor(player.posX), (int)Math.floor(player.boundingBox.minY) - 1, (int)Math.floor(player.posZ));
        if(!player.onGround && !(block.getMaterial() == Material.water || block.getMaterial() == Material.lava) || player.onGround && !player.isInWater())
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
        return (player.isInWater() && player.motionY > 0.03999999910593033D || hitLandTimeout > 0) && player.isEntityAlive() && timeActive < (20 * 60)? timeActive + 2 : 10;
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
        if(!(player.isInWater() || player.handleLavaMovement()))
        {
            if(hitLand)
            {
                hitLand = false;
                player.jump();
                player.motionY *= 0.8F;
            }
            if(hitLandTimeout > 0)
            {
                hitLandTimeout--;

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
                hitLandTimeout = 0;
            }
            else
            {
                double motionY = player.motionY;
                player.moveEntityWithHeading(moveType == 3 ? moveFactor : moveType == 4 ? -moveFactor : 0.0F, moveType == 1 ? moveFactor : moveType == 2 ? -moveFactor : 0.0F);
                if(!(player.isCollidedHorizontally && player.isOnLadder()))
                {
                    player.motionY = motionY;
                }
                if(player.isCollidedHorizontally && hitLandTimeout == -1)
                {
                    hitLand = true;
                    hitLandTimeout = 10;
                }
            }
            player.motionY += 0.03999999910593033D;
            if(player.motionY < 0.03999999910593033D && Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ) < 0.01D)
            {
                hitLandTimeout = 0;
            }
        }
    }

    @Override
    public String getName()
    {
        return "swim " + (moveType == 1 ? "forward" : moveType == 2 ? "back" : moveType == 3 ? "left" : "right");
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
