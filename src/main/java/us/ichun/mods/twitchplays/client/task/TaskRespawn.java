package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;

public class TaskRespawn extends Task
{
    public TaskRespawn(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public void init()
    {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.currentScreen instanceof GuiGameOver)
        {
            mc.thePlayer.respawnPlayer();
            mc.displayGuiScreen((GuiScreen)null);
        }
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

    }

    @Override
    public String getName()
    {
        return "respawn";
    }
}
