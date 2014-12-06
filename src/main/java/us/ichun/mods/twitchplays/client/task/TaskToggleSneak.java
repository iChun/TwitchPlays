package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class TaskToggleSneak extends Task {
    public TaskToggleSneak(WorldClient world, EntityPlayerSP playerSP) {
        super(world, playerSP);
    }

    @Override
    public boolean parse(String... args) {
        return true;
    }

    @Override
    public int maxActiveTime() {
        return 1;
    }

    @Override
    protected void update() {
        Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = !Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed;
    }
}
