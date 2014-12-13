package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import us.ichun.mods.twitchplays.common.TwitchPlays;

public class TaskCommand extends Task
{
    public String[] commands;
    public TaskCommand(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public void init()
    {
        Minecraft mc = Minecraft.getMinecraft();

        String s = "/";
        for(int i = 1; i < commands.length; i++)
        {
            s = s + commands[i] + " ";
        }
        s = s.trim();

        mc.ingameGUI.getChatGUI().addToSentMessages(s);
        if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(mc.thePlayer, s) != 0) return;
        mc.thePlayer.sendChatMessage(s);
    }

    @Override
    public boolean requiresOp(String...args)
    {
        return true;
    }

    @Override
    public boolean bypassOrder(String...args) { return true; };

    @Override
    public boolean parse(String... args)
    {
        commands = args;
        return true;
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
    public boolean canWorkDead()
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "command";
    }
}
