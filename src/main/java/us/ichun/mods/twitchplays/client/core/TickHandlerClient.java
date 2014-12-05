package us.ichun.mods.twitchplays.client.core;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.stream.ChatController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.lwjgl.input.Keyboard;
import tv.twitch.AuthToken;
import tv.twitch.chat.Chat;
import tv.twitch.chat.StandardChatAPI;
import us.ichun.mods.twitchplays.client.task.Task;
import us.ichun.mods.twitchplays.client.task.TaskRegistry;

import java.util.ArrayList;

public class TickHandlerClient
{
    public TickHandlerClient()
    {
        chatController = new ChatController();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            Minecraft mc = Minecraft.getMinecraft();

            if(mc.theWorld != null)
            {
                if(!init)
                {
                    init = true;

                    String streamer = "ayloBot";
                    //TODO if possible I would like to connect to a chat anonymously. Twitch API seems to have some form of support for it. If you enable line 50, do you crash?

//                    (new Chat(new StandardChatAPI())).initialize(streamer, false);

//                    chatController.func_152998_c(streamer);
//                    chatController.func_152985_f(streamer);// set user
//                    chatController.field_153005_c = streamer;

                    //func_152993_m shutdown
                    //func_152985_f initialize(name)
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            Minecraft mc = Minecraft.getMinecraft();
            if(mc.theWorld != null)
            {
                if(clock != mc.theWorld.getWorldTime())
                {
                    clock = mc.theWorld.getWorldTime();
                    if(!tasks.isEmpty())
                    {
                        Task task = tasks.get(0);
                        if(task.timeActive == 0)
                        {
                            task.world = mc.theWorld;
                            task.player = mc.thePlayer;
                            task.init();
                        }
                        task.tick();
                        if(task.timeActive >= task.maxActiveTime())
                        {
                            task.terminate();
                            tasks.remove(0);
                        }
                    }
                }
            }
        }
    }

    public boolean parseChat(WorldClient world, EntityPlayerSP player, String s)//return true if task is added
    {
        //TODO might have to split string after <name>
        String[] args = s.split(" ");
        ArrayList<String> actualArgs = new ArrayList<String>();
        for(int i = 0; i < args.length; i++)
        {
            if(!args[i].toLowerCase().trim().isEmpty())
            {
                actualArgs.add(args[i].toLowerCase().trim());
            }
        }
        if(!actualArgs.isEmpty() && TaskRegistry.hasTask(actualArgs.get(0)))
        {
            Task task = TaskRegistry.createTask(world, player, actualArgs.toArray(new String[actualArgs.size()]));
            if(task != null && task.canBeAdded(ImmutableList.copyOf(tasks)))
            {
                tasks.add(task);
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onChatEvent(ClientChatReceivedEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.theWorld != null && event.message instanceof ChatComponentTranslation)
        {
            ChatComponentTranslation msg = (ChatComponentTranslation)event.message;
            if(msg.getKey().equals("chat.type.text") && msg.getFormatArgs().length > 1)
            {
                String s = "";
                for(int i = 1; i < msg.getFormatArgs().length; i++)
                {
                    if(msg.getFormatArgs()[i] instanceof ChatComponentText)
                    {
                        s = s + ((ChatComponentText)msg.getFormatArgs()[i]).getUnformattedTextForChat();
                    }
                    else if(msg.getFormatArgs()[i] instanceof ChatComponentTranslation)
                    {
                        s = s + ((ChatComponentTranslation)msg.getFormatArgs()[i]).getUnformattedTextForChat();
                    }
                    else
                    {
                        s = s + msg.getFormatArgs()[i].toString();
                    }
                }
                parseChat(mc.theWorld, mc.thePlayer, s);
            }
        }
    }

    //TODO render first/third person as mini window?
    //TODO commands parsed, time enlapsed, etc.

    public long clock;

    public boolean init;

    public ArrayList<Task> tasks = new ArrayList<Task>();

    public ChatController chatController;
}
