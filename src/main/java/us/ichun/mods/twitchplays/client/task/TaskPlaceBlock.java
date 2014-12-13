package us.ichun.mods.twitchplays.client.task;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;

public class TaskPlaceBlock extends Task
{
    public int x;
    public int y;
    public int z;

    public TaskPlaceBlock(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public void init()
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.setIngameFocus();
        mc.inGameHasFocus = true;
        if(mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            x = mc.objectMouseOver.blockX;
            y = mc.objectMouseOver.blockY;
            z = mc.objectMouseOver.blockZ;

            Block block = mc.theWorld.getBlock(x, y, z);

            int side = mc.objectMouseOver.sideHit;

            if (block == Blocks.snow_layer && (mc.theWorld.getBlockMetadata(x, y, z) & 7) < 1)
            {
                side = 1;
            }
            else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(mc.theWorld, x, y, z))
            {
                if (side == 0)
                {
                    --y;
                }

                if (side == 1)
                {
                    ++y;
                }

                if (side == 2)
                {
                    --z;
                }

                if (side == 3)
                {
                    ++z;
                }

                if (side == 4)
                {
                    --x;
                }

                if (side == 5)
                {
                    ++x;
                }
            }
        }
    }

    @Override
    public void terminate()
    {
    }

    @Override
    public boolean parse(String... args)
    {
        return args.length == 1;
    }

    @Override
    public int maxActiveTime()
    {
        return 10;
    }

    @Override
    protected void update()
    {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.theWorld.checkNoEntityCollision(AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)))
        {
            boolean sneak = mc.thePlayer.isSneaking();
            mc.thePlayer.setSneaking(true);
            mc.func_147121_ag();
            mc.thePlayer.setSneaking(sneak);
            timeActive = 5000;
            return;
        }
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
        return "place";
    }
}
