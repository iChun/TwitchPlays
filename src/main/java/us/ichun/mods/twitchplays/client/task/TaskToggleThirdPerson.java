package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class TaskToggleThirdPerson extends Task
{
    public TaskToggleThirdPerson(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public void init()
    {
        Minecraft.getMinecraft().gameSettings.thirdPersonView++;
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView > 2)
        {
            Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
        }
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
        return "togglethirdperson";
    }
}
