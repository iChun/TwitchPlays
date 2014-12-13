package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class TaskUnSneak extends Task {
    public TaskUnSneak(WorldClient world, EntityPlayerSP playerSP) {
        super(world, playerSP);
    }

    @Override
    public boolean parse(String... args) {
        return args.length == 1;
    }

    @Override
    public int maxActiveTime() {
        return 1;
    }

    @Override
    protected void update() {
        Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = false;
    }

    @Override
    public String getName()
    {
        return "sneak";
    }
}
