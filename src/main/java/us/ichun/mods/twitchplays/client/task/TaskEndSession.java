package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import us.ichun.mods.twitchplays.common.TwitchPlays;

public class TaskEndSession extends Task
{
    public TaskEndSession(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public void init()
    {
        player.addChatMessage(new ChatComponentTranslation("twitchplays.command.ended", TwitchPlays.tickHandlerClient.chatOwner).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
        TwitchPlays.tickHandlerClient.endSession();
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
    public boolean canWorkDead()
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "endsession";
    }
}
