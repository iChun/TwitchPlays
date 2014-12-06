package us.ichun.mods.twitchplays.client.core;

import com.google.common.collect.ImmutableList;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import ichun.client.render.RendererHelper;
import ichun.common.core.util.ObfHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.ChatController;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.lwjgl.opengl.GL11;
import tv.twitch.chat.ChatMessage;
import tv.twitch.chat.ChatUserInfo;
import tv.twitch.chat.ChatUserMode;
import us.ichun.mods.twitchplays.client.task.Task;
import us.ichun.mods.twitchplays.client.task.TaskRegistry;
import us.ichun.mods.twitchplays.common.TwitchPlays;

import java.util.ArrayList;

public class TickHandlerClient
        implements ChatController.ChatListener
{
    public TickHandlerClient()
    {
        chatController = new ChatController();
        chatController.func_152990_a(this);
        chatController.func_152984_a("nmt37qblda36pvonovdkbopzfzw3wlq");

        if(TwitchPlays.config.getInt("minicam") == 1)
        {
            if(OpenGlHelper.isFramebufferEnabled())
            {
                minicam = RendererHelper.createFrameBuffer("TwitchPlays", true);
            }
            else
            {
                TwitchPlays.console("Your system does not support Frame buffers, sorry!", true);
            }
        }

        showMinicam = true;
        oriPitch = targetPitch = prevCamPitch = camPitch = 90F;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START)
        {
            renderMinicam(event);
        }
        else
        {
            renderMinicamOnScreen(event);

            chatController.func_152997_n();

            Minecraft mc = Minecraft.getMinecraft();

            if(mc.theWorld != null)
            {
                if(!init)
                {
                    init = true;

                    String streamer = "ohaiichun".toLowerCase();

                    if(ObfHelper.obfuscation || !ObfHelper.obfuscation && TwitchPlays.config.getInt("twitchChatHook") == 1)
                    {
                        chatController.field_153010_h = true;
                        chatController.func_152998_c(streamer);
                        chatController.func_152985_f(streamer);
                        chatController.field_153005_c = streamer;
                        //TODO proper disconnecting.
                        //These lines occcasionally crash in dev env. I do not know why. -iChun
                    }

                    //field_153010_h connectAnonymously
                    //func_152997_n connectToChat
                    //func_153000_j chatState
                    //func_152991_c isConnected
                    //func_152993_m shutdown
                    //func_152985_f initialize(name) ???
                    //func_152997_n update
                }
            }
        }
    }

    public void renderMinicam(TickEvent.RenderTickEvent event)
    {
        if(minicam == null || !init || !updateMinicam || !showMinicam)
        {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if((playerInstance == null || playerInstance.worldObj != mc.theWorld) && mc.theWorld != null)
        {
            playerInstance = new EntityClientPlayerMP(mc, mc.theWorld, new Session(mc.thePlayer.getCommandSenderName(), mc.thePlayer.getGameProfile().getId().toString().replace("-", ""), "fakeToken", "mojang"), mc.thePlayer != null ? mc.thePlayer.sendQueue : null, null);
            ObfuscationReflectionHelper.setPrivateValue(EntityPlayer.class, playerInstance, mc.thePlayer.getGameProfile(), ObfHelper.gameProfile);
        }

        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        minicam.bindFramebuffer(true);

        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glColor3f(1.0F, 0.0F, 0.0F);

        EntityLivingBase viewEntity = mc.renderViewEntity;
        EntityClientPlayerMP mcPlayer = mc.thePlayer;

        mc.renderViewEntity = playerInstance;
        mc.thePlayer = playerInstance;

        NBTTagCompound tag = new NBTTagCompound();
        mcPlayer.writeToNBT(tag);
        playerInstance.readFromNBT(tag);

        playerInstance.posX = mcPlayer.posX;
        playerInstance.posY = mcPlayer.posY + 0.01D;
        playerInstance.posZ = mcPlayer.posZ;

        playerInstance.prevPosX = mcPlayer.prevPosX;
        playerInstance.prevPosY = mcPlayer.prevPosY + 0.01D;
        playerInstance.prevPosZ = mcPlayer.prevPosZ;

        playerInstance.lastTickPosX = mcPlayer.lastTickPosX;
        playerInstance.lastTickPosY = mcPlayer.lastTickPosY + 0.01D;
        playerInstance.lastTickPosZ = mcPlayer.lastTickPosZ;

        playerInstance.prevRenderYawOffset = mcPlayer.prevRenderYawOffset;
        playerInstance.renderYawOffset = mcPlayer.renderYawOffset;

        playerInstance.prevRotationYaw = mcPlayer.prevRotationYaw;
        playerInstance.prevRotationYawHead = mcPlayer.prevRotationYawHead;
        playerInstance.prevRotationPitch = mcPlayer.prevRotationPitch;

        playerInstance.rotationYaw = mcPlayer.rotationYaw;
        playerInstance.rotationYawHead = mcPlayer.rotationYawHead;
        playerInstance.rotationPitch = mcPlayer.rotationPitch;

        playerInstance.deathTime = mcPlayer.deathTime;
        playerInstance.hurtTime = mcPlayer.hurtTime;

        playerInstance.swingProgress = mcPlayer.swingProgress;
        playerInstance.swingProgressInt = mcPlayer.swingProgressInt;
        playerInstance.isSwingInProgress = mcPlayer.isSwingInProgress;

        playerInstance.prevLimbSwingAmount = mcPlayer.prevLimbSwingAmount;
        playerInstance.limbSwingAmount = mcPlayer.limbSwingAmount;
        playerInstance.limbSwing = mcPlayer.limbSwing;

        playerInstance.ticksExisted = mcPlayer.ticksExisted;

        playerInstance.yOffset = mcPlayer.yOffset;
        playerInstance.ySize = mcPlayer.ySize;

        playerInstance.setInvisible(mcPlayer.isInvisible());

        boolean hideGui = mc.gameSettings.hideGUI;
        mc.gameSettings.hideGUI = true;

        int tp = mc.gameSettings.thirdPersonView;
        mc.gameSettings.thirdPersonView = 1;

        float tpDist = mc.entityRenderer.thirdPersonDistance;
        mc.entityRenderer.thirdPersonDistance = (float)TwitchPlays.config.getInt("minicamDistance") * 0.1F;

        float tpDistTemp = mc.entityRenderer.thirdPersonDistanceTemp;
        mc.entityRenderer.thirdPersonDistanceTemp = (float)TwitchPlays.config.getInt("minicamDistance") * 0.1F;

        playerInstance.prevRotationYaw = prevCamYaw;
        playerInstance.rotationYaw = camYaw;
        playerInstance.prevRotationPitch = prevCamPitch;
        playerInstance.rotationPitch = camPitch;

        mc.entityRenderer.renderWorld(event.renderTickTime, 0L);

        mc.entityRenderer.thirdPersonDistance = tpDist;
        mc.entityRenderer.thirdPersonDistanceTemp = tpDistTemp;

        mc.gameSettings.thirdPersonView = tp;

        mc.gameSettings.hideGUI = hideGui;

        mc.thePlayer = mcPlayer;

        mc.renderViewEntity = viewEntity;

        EntityFX.interpPosX = mcPlayer.lastTickPosX + (mcPlayer.posX - mcPlayer.lastTickPosX) * (double)event.renderTickTime;
        EntityFX.interpPosY = mcPlayer.lastTickPosY + (mcPlayer.posY - mcPlayer.lastTickPosY) * (double)event.renderTickTime;
        EntityFX.interpPosZ = mcPlayer.lastTickPosZ + (mcPlayer.posZ - mcPlayer.lastTickPosZ) * (double)event.renderTickTime;

        minicam.unbindFramebuffer();

        GL11.glPopMatrix();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        mc.getFramebuffer().bindFramebuffer(true);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        updateMinicam = false;
    }

    public void renderMinicamOnScreen(TickEvent.RenderTickEvent event)
    {
        if(minicam == null || !init || !(Minecraft.getMinecraft().currentScreen == null || Minecraft.getMinecraft().currentScreen instanceof GuiChat) || !showMinicam)
        {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution reso = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        int width1 = 5;
        int height1 = 5;
        int width2 = width1 + (int)((float)reso.getScaledWidth() * (float)TwitchPlays.config.getInt("minicamSize") / 100F);
        int height2 = height1 + (int)((float)reso.getScaledHeight() * (float)TwitchPlays.config.getInt("minicamSize") / 100F);

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        minicam.bindFramebufferTexture();

        GL11.glTranslatef(0.0F, 0.0F, -909.999847412109261313162F);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.8F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(width1+1, height2-1, -90.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(width2-1, height2-1, -90.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(width2-1, height1+1, -90.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(width1+1, height1+1, -90.0D, 0.0D, 1.0D);
        tessellator.draw();
        Vec3 fog = mc.theWorld.getFogColor(event.renderTickTime);
        GL11.glColor4f((float)fog.xCoord, (float)fog.yCoord, (float)fog.zCoord, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(width1+2, height2-2, -90.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(width2-2, height2-2, -90.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(width2-2, height1+2, -90.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(width1+2, height1+2, -90.0D, 0.0D, 1.0D);
        tessellator.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(width1+2, height2-2, -90.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(width2-2, height2-2, -90.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(width2-2, height1+2, -90.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(width1+2, height1+2, -90.0D, 0.0D, 1.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            Minecraft mc = Minecraft.getMinecraft();
            if(mc.theWorld != null)
            {
                prevCamYaw = camYaw;
                prevCamPitch = camPitch;

                if(clock != mc.theWorld.getWorldTime() || !mc.theWorld.getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
                {
                    updateMinicam = true;
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
                    for(int i = instaTasks.size() - 1; i >= 0; i--)
                    {
                        Task task = instaTasks.get(i);
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
                            instaTasks.remove(0);
                        }
                    }
                    if(turnTime > 0)
                    {
                        camYaw += (targetYaw - oriYaw) *(1F / (float)TURN_TIME);
                        camPitch += (targetPitch - oriPitch) *(1F / (float)TURN_TIME);
                        turnTime--;
                        if(turnTime == 0)
                        {
                            camYaw = targetYaw;
                            camPitch = targetPitch;
                        }
                    }
                }
            }
        }
    }

    public boolean parseChat(WorldClient world, EntityPlayerSP player, String s, boolean isOp)//return true if task is added
    {
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
            String[] newArgs = actualArgs.toArray(new String[actualArgs.size()]);
            Task task = TaskRegistry.createTask(world, player, newArgs);
            if(task != null && task.canBeAdded(ImmutableList.copyOf(tasks)) && (task.requiresOp(newArgs) && isOp || !task.requiresOp(newArgs)))
            {
                if(task.bypassOrder(newArgs))
                {
                    instaTasks.add(task);
                }
                else
                {
                    tasks.add(task);
                }
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
                boolean isPlayer = false;
                if(msg.getFormatArgs()[0] instanceof ChatComponentText)
                {
                    isPlayer = ((ChatComponentText)msg.getFormatArgs()[0]).getUnformattedTextForChat().equals(mc.thePlayer.getCommandSenderName());
                }
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
                parseChat(mc.theWorld, mc.thePlayer, s, isPlayer);
            }
        }
    }

    //TODO commands parsed, time enlapsed, etc.

    public long clock;

    public boolean init;

    public ArrayList<Task> tasks = new ArrayList<Task>();
    public ArrayList<Task> instaTasks = new ArrayList<Task>();

    public boolean showMinicam;
    public boolean updateMinicam;
    public Framebuffer minicam;
    public EntityClientPlayerMP playerInstance;
    public float prevCamYaw;
    public float prevCamPitch;
    public float camYaw;
    public float camPitch;
    public float oriYaw;
    public float oriPitch;
    public float targetYaw;
    public float targetPitch;
    public int turnTime;
    public static final int TURN_TIME = 10;

    public ChatController chatController;

    @Override
    public void func_152903_a(ChatMessage[] messages) //on chat message
    {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.theWorld != null)
        {
            for(ChatMessage msg : messages)
            {
                if(!msg.modes.contains(ChatUserMode.TTV_CHAT_USERMODE_BANNED))
                {
                    boolean isTask = parseChat(mc.theWorld, mc.thePlayer, msg.message, msg.modes.contains(ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) || msg.modes.contains(ChatUserMode.TTV_CHAT_USERMODE_BROADCASTER) || TwitchPlays.config.getInt("allowTwitchStaff") == 1 && (msg.modes.contains(ChatUserMode.TTV_CHAT_USERMODE_STAFF) || msg.modes.contains(ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR)));
                    ChatComponentText message = new ChatComponentText("");
                    message.getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE);
                    message.appendText("<" + msg.userName + "> ");
                    ChatComponentText text = new ChatComponentText(msg.message);
                    text.getChatStyle().setColor(isTask ? EnumChatFormatting.GRAY : EnumChatFormatting.WHITE);
                    message.appendSibling(text);
                    mc.thePlayer.addChatMessage(message);
                }
            }
        }
    }

    @Override
    public void func_152904_a(ChatUserInfo[] p_152904_1_, ChatUserInfo[] p_152904_2_, ChatUserInfo[] p_152904_3_)//chat channel user change callback..?
    {

    }

    @Override
    public void func_152906_d() //On chat connect
    {

    }

    @Override
    public void func_152905_e() //On chat disconnect
    {

    }

    @Override
    public void func_152902_f() //on chat clear
    {

    }
}
