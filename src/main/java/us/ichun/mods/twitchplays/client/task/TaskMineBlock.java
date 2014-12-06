package us.ichun.mods.twitchplays.client.task;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MovingObjectPosition;

public class TaskMineBlock extends Task
{
    public Block block;
    public int x;
    public int y;
    public int z;

    public boolean terminate;

    public TaskMineBlock(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public void init()
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.gameSettings.keyBindAttack.pressed = true;
        if(mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            x = mc.objectMouseOver.blockX;
            y = mc.objectMouseOver.blockY;
            z = mc.objectMouseOver.blockZ;

            block = mc.theWorld.getBlock(x, y, z);
            if(block.getBlockHardness(mc.theWorld, x, y, z) < 0.0F || block.isAir(mc.theWorld, x, y, z) || !player.isCurrentToolAdventureModeExempt(x, y, z))
            {
                terminate = true;
            }
        }
        mc.func_147116_af();
    }

    @Override
    public void terminate()
    {
        Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = false;
    }

    @Override
    public boolean parse(String... args)
    {
        return args.length == 1;
    }

    @Override
    public int maxActiveTime()
    {
        return terminate || timeActive > (20 * 60) ? 0 : timeActive + 2;
    }

    @Override
    protected void update()
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.func_147116_af();

        if(!mc.gameSettings.keyBindAttack.pressed || mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || mc.objectMouseOver.blockX != x || mc.objectMouseOver.blockY != y || mc.objectMouseOver.blockZ != z || mc.theWorld.getBlock(x, y, z) != block)
        {
            terminate = true;
        }
    }

    @Override
    public String getName()
    {
        return "mine";
    }
}
