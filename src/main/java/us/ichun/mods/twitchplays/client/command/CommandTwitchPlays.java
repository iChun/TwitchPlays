package us.ichun.mods.twitchplays.client.command;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import us.ichun.mods.twitchplays.common.TwitchPlays;

import java.util.List;

public class CommandTwitchPlays extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "twitchplays";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + "           " + StatCollector.translateToLocal("twitchplays.command.help");
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2)
    {
        boolean needHelp = false;
        if(var2.length == 1)
        {
            if(var2[0].equalsIgnoreCase("end"))
            {
                if(TwitchPlays.tickHandlerClient.init)
                {
                    var1.addChatMessage(new ChatComponentTranslation("twitchplays.command.ended", TwitchPlays.tickHandlerClient.chatOwner).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
                    TwitchPlays.tickHandlerClient.endSession();
                }
                else
                {
                    var1.addChatMessage(new ChatComponentTranslation("twitchplays.command.noSession").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
                }
            }
            else if(var2[0].equalsIgnoreCase("start"))
            {
                var1.addChatMessage(new ChatComponentTranslation("twitchplays.command.start").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
            }
            else
            {
                needHelp = true;
            }
        }
        else if(var2.length == 2)
        {
            if(var2[0].equalsIgnoreCase("start") && !var2[1].isEmpty())
            {
                if(TwitchPlays.tickHandlerClient.init)
                {
                    var1.addChatMessage(new ChatComponentTranslation("twitchplays.command.alreadyStarted", TwitchPlays.tickHandlerClient.chatOwner).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
                }
                else
                {
                    var1.addChatMessage(new ChatComponentTranslation("twitchplays.command.started", var2[1].trim()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
                    TwitchPlays.tickHandlerClient.startSession(var2[1].trim());
                }
            }
            else
            {
                needHelp = true;
            }
        }
        else
        {
            var1.addChatMessage(new ChatComponentTranslation("twitchplays.command.end").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
            var1.addChatMessage(new ChatComponentTranslation("twitchplays.command.start").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
        }
        if(needHelp)
        {
            var1.addChatMessage(new ChatComponentTranslation("twitchplays.command.end").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
            var1.addChatMessage(new ChatComponentTranslation("twitchplays.command.start").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(true)));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "start", "end")  : null;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1)
    {
        return true;
    }
}
