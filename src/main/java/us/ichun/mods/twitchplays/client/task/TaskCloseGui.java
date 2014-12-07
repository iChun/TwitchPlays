package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import us.ichun.mods.twitchplays.common.TwitchPlays;

public class TaskCloseGui extends Task
{
    public TaskCloseGui(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public void init()
    {
        if(Minecraft.getMinecraft().currentScreen != null && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat))
        {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
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
        return 0;
    }

    @Override
    protected void update()
    {

    }

    @Override
    public String getName()
    {
        return "closegui";
    }
}
